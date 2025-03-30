package app.fittapp.web;

import app.fittapp.security.UserAuthDetails;
import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.workout.model.CompletedWorkout;
import app.fittapp.workout.model.Workout;
import app.fittapp.workout.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @GetMapping("/completed")
    public ModelAndView getWorkoutsHistoryPage(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        User user = userService.getUserById(userAuthDetails.getId());

        List<CompletedWorkout> completedWorkouts = user.getCompletedWorkouts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("workouts-history");
        modelAndView.addObject("user", user);
        modelAndView.addObject("completedWorkouts", completedWorkouts);

        return modelAndView;
    }

    @PostMapping("/{workoutId}/completed")
    public String addWorkoutToCompleted(@PathVariable UUID workoutId, @AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        User user = userService.getUserById(userAuthDetails.getId());

        Workout workout = workoutService.getWorkoutById(workoutId);

        workoutService.initCompletedWorkout(user, workout);

        return "redirect:/workouts/completed";
    }

    @DeleteMapping("/completed/{completedWorkoutId}")
    public String deleteWorkoutFromCompleted(@PathVariable UUID completedWorkoutId) {

        CompletedWorkout completedWorkout = workoutService.getCompletedWorkoutById(completedWorkoutId);

        workoutService.deleteCompletedWorkout(completedWorkout);

        return "redirect:/workouts/completed";
    }
}
