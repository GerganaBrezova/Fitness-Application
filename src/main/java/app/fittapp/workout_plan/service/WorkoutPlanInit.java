package app.fittapp.workout_plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WorkoutPlanInit implements CommandLineRunner {

    private final WorkoutPlanService workoutPlanService;

    @Autowired
    public WorkoutPlanInit(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!workoutPlanService.getAllSystemWorkoutPlans().isEmpty()) {
            return;
        }

        workoutPlanService.initWorkoutPlan("Beginner Plan",
                "Foundations",
                "New to fitness or returning after a break.",
                "Building strength, learning proper form, and improving basic fitness levels.",
                0);

        workoutPlanService.initWorkoutPlan("Intermediate Plan",
                "Progression",
                "Individuals with a basic fitness level seeking a greater challenge.",
                "Developing strength, stamina, and improving skills through gradual advancement.",
                150);

        workoutPlanService.initWorkoutPlan("Advanced Plan",
                "Elite Performance",
                "Experienced individuals aiming for high-intensity performance.",
                "Advanced strength training, explosive movements, and intense conditioning.",
                200);
    }
}
