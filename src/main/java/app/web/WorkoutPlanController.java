package app.web;

import app.security.UserAuthDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.workout_plan.model.WorkoutPlan;
import app.workout_plan.service.WorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller()
@RequestMapping("/plans")
public class WorkoutPlanController {

    private final UserService userService;
    private final WorkoutPlanService workoutPlanService;

    @Autowired
    public WorkoutPlanController(UserService userService, WorkoutPlanService workoutPlanService) {
        this.userService = userService;
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public ModelAndView getWorkoutPlansPage(@AuthenticationPrincipal UserAuthDetails userDetails) {

        User user = userService.getUserById(userDetails.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("plans");
        modelAndView.addObject("user", user);
        modelAndView.addObject("allSystemWorkoutPlans", workoutPlanService.getAllSystemWorkoutPlans());

        return modelAndView;
    }

    @PostMapping("/{workoutPlanId}/unlock")
    public String unlockPlan(@PathVariable UUID workoutPlanId, @AuthenticationPrincipal UserAuthDetails userDetails) {

        workoutPlanService.unlockWorkoutPlan(userDetails.getId(), workoutPlanId);

        return "redirect:/plans";
    }

    @GetMapping("/{workoutPlanId}/workouts")
    public ModelAndView getWorkoutPlanDetailsPage(@PathVariable UUID workoutPlanId, @AuthenticationPrincipal UserAuthDetails userDetails) {

        User user = userService.getUserById(userDetails.getId());

        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(workoutPlanId);
        workoutPlanService.sortWorkoutsByDay(workoutPlan);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("plan-details");
        modelAndView.addObject("user", user);
        modelAndView.addObject("workoutPlan", workoutPlan);

        return modelAndView;
    }
}
