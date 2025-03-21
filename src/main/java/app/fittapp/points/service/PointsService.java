package app.fittapp.points.service;

import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.web.dto.UserRegisteredEvent;
import app.fittapp.workout_plan.model.WorkoutPlan;
import app.fittapp.workout_plan.service.WorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PointsService {

    private final UserService userService;
    private final WorkoutPlanService workoutPlanService;

    @Autowired
    public PointsService(UserService userService, WorkoutPlanService workoutPlanService) {
        this.userService = userService;
        this.workoutPlanService = workoutPlanService;
    }

//    @Async
//    @EventListener
//    public void assignWorkoutPlanOnRegistration(UserRegisteredEvent event) {
//
//        User user = userService.getUserById(event.getUserId());
//        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanByType("Beginner Plan");
//
//        user.getWorkoutPlans().add(workoutPlan);
//
//        userService.saveUser(user);
//    }
}
