package app.workout_plan.service;

import app.exceptions.InsufficientAmountOfPoints;
import app.exceptions.WorkoutPlanNotFound;
import app.user.model.User;
import app.user.service.UserService;
import app.workout.model.Workout;
import app.workout_plan.model.WorkoutPlan;
import app.workout_plan.repository.WorkoutPlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserService userService;

    @Autowired
    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository, UserService userService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.userService = userService;
    }

    public List<WorkoutPlan> getAllSystemWorkoutPlans() {
        return workoutPlanRepository.findAll().stream().sorted(Comparator.comparingInt(this::getSortOrder))
                .collect(Collectors.toList());
    }

    private int getSortOrder(WorkoutPlan workoutPlan) {
        return switch (workoutPlan.getType()) {
            case "Beginner Plan" -> 1;
            case "Intermediate Plan" -> 2;
            case "Advanced Plan" -> 3;
            default -> 4;
        };
    }

    public void initWorkoutPlan(String type, String name, String targetAudience, String workoutFocus, int pointsNeeded) {

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .type(type)
                .name(name)
                .targetAudience(targetAudience)
                .workoutFocus(workoutFocus)
                .pointsNeeded(pointsNeeded)
                .build();

        workoutPlanRepository.save(workoutPlan);
    }

    public void unlockWorkoutPlan(UUID userId, UUID workoutPlanId) {

        WorkoutPlan workoutPlan = getWorkoutPlanById(workoutPlanId);

        User user = userService.getUserById(userId);

        if (user.getPoints() >= workoutPlan.getPointsNeeded()) {
            user.setPoints(user.getPoints() - workoutPlan.getPointsNeeded());
            user.getWorkoutPlans().add(workoutPlan);

            userService.saveUser(user);
        } else {
            throw new InsufficientAmountOfPoints("Not enough points: %d remains.".formatted(workoutPlan.getPointsNeeded() - user.getPoints()));
        }

        log.info("User %s unlocked %s successfully.".formatted(user.getUsername(), workoutPlan.getType()));
    }

    public WorkoutPlan getWorkoutPlanById(UUID workoutPlanId) {
        return workoutPlanRepository.findById(workoutPlanId).orElseThrow(() -> new WorkoutPlanNotFound("Workout plan with id %s was not found.".formatted(workoutPlanId)));
    }

//    @Async
//    @EventListener
//    public void assignWorkoutPlanOnRegistration(UserRegisteredEvent event) {
//
//        User user = userService.getUserById(event.getUserId());
//        WorkoutPlan workoutPlan = getWorkoutPlanByType("Beginner Plan");
//
//        user.getWorkoutPlans().add(workoutPlan);
//
//        userService.saveUser(user);
//
//        log.info("Assigned 'Beginner Plan' to user with id %s.".formatted(user.getId()));
//    }

    public WorkoutPlan getWorkoutPlanByType(String type) {
        return workoutPlanRepository.findByType(type).orElseThrow(() -> new WorkoutPlanNotFound("Workout plan %s was not found.".formatted(type)));
    }

    public void sortWorkoutsByDay(WorkoutPlan plan) {
        plan.getWorkouts().sort(Comparator.comparing(Workout::getDay));
    }
}
