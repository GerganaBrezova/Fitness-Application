package app.fittapp.workout.service;

import app.fittapp.exceptions.DomainException;
import app.fittapp.user.model.User;
import app.fittapp.user.repository.UserRepository;
import app.fittapp.user.service.UserService;
import app.fittapp.workout.model.CompletedWorkout;
import app.fittapp.workout.model.Workout;
import app.fittapp.workout.repository.CompletedWorkoutRepository;
import app.fittapp.workout.repository.WorkoutRepository;
import app.fittapp.workout_plan.model.WorkoutPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final CompletedWorkoutRepository completedWorkoutRepository;
    private final UserService userService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, CompletedWorkoutRepository completedWorkoutRepository, UserService userService) {
        this.workoutRepository = workoutRepository;
        this.completedWorkoutRepository = completedWorkoutRepository;
        this.userService = userService;
    }

    public void initWorkout(int day, String type, String imagePath, List<String> exercises, WorkoutPlan plan) {

        Workout workout = Workout.builder()
                .day(day)
                .type(type)
                .imagePath(imagePath)
                .exercises(exercises)
                .workoutPlan(plan)
                .build();

        workoutRepository.save(workout);
    }

    public List<Workout> getAllSystemWorkouts() {
        return workoutRepository.findAll();
    }

    public void initCompletedWorkout(User user, Workout workout) {
        CompletedWorkout completedWorkout = CompletedWorkout.builder()
                .day(workout.getDay())
                .type(workout.getWorkoutPlan().getType())
                .completedOn(LocalDateTime.now())
                .user(user)
                .build();

        user.setPoints(user.getPoints() + 5);
        userService.saveUser(user);

        completedWorkoutRepository.save(completedWorkout);
    }

    public Workout getWorkoutById(UUID workoutId) {
        return workoutRepository.findById(workoutId).orElseThrow(() -> new DomainException("Workout with id %s was not found.".formatted(workoutId)));
    }

    public CompletedWorkout getCompletedWorkoutById(UUID completedWorkoutId) {
        return completedWorkoutRepository.findById(completedWorkoutId).orElseThrow(() -> new DomainException("Completed workout with id %s was not found.".formatted(completedWorkoutId)));
    }

    public void deleteCompletedWorkout(CompletedWorkout completedWorkout) {
        completedWorkoutRepository.delete(completedWorkout);
    }

    public List<CompletedWorkout> getCompletedWorkoutsByUser(User user) {
        return completedWorkoutRepository.findByUserOrderByCompletedOnDesc(user);
    }
}