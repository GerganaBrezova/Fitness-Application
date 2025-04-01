package app.workout.service;

import app.workout_plan.model.WorkoutPlan;
import app.workout_plan.service.WorkoutPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class WorkoutInit implements CommandLineRunner {

    private final WorkoutService workoutService;
    private final WorkoutPlanService workoutPlanService;

    @Autowired
    public WorkoutInit(WorkoutService workoutService, WorkoutPlanService workoutPlanService) {
        this.workoutService = workoutService;
        this.workoutPlanService = workoutPlanService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!workoutService.getAllSystemWorkouts().isEmpty()) {
            return;
        }

        WorkoutPlan beginnerPlan = workoutPlanService.getWorkoutPlanByType("Beginner Plan");

        workoutService.initWorkout(
                1,
                "Full-body Strength",
                "/images/fullBody.PNG",
                List.of("Squats (3x12)", "Push-ups (3x10)", "Glute Bridges (3x15)", "Dumbbell Rows (3x12)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                2,
                "Cardio + Core",
                "/images/cardio.PNG",
                List.of("20-30 min brisk walk or light jog", "Plank (3x30 sec)", "Bicycle Crunches (3x15)", "Leg Raises (3x10)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                3,
                "Mobility & Stretching",
                "/images/stretching.PNG",
                List.of("Stretching or yoga (20-30 min)", "Foam rolling (10-15 min)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                4,
                "Upper Body Strength",
                "/images/upperBody.PNG",
                List.of("Wall Push-ups (3x10)", "Dumbbell Shoulder Press (3x12)", "Dumbbell Bicep Curls (3x12)", "Tricep Dips (3x10)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                5,
                "Lower Body Strength",
                "/images/lowerBody.PNG",
                List.of("Bodyweight Squats (3x15)", "Lunges (3x12 each leg)", "Glute Kickbacks (3x15 each leg)", "Calf Raises (3x20)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                6,
                "Stretch & Foam Roll",
                "/images/stretching.PNG",
                List.of("Gentle yoga/stretching (20 min)", "Foam rolling (10 min)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                7,
                "Cardio + Light Core",
                "/images/cardio.PNG",
                List.of("20-30 min cycling or brisk walk", "Side Plank (3x30 sec each side)", "Russian Twists (3x20)", "Leg Raises (3x12)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                8,
                "Full-body Strength Training",
                "/images/fullBody.PNG",
                List.of("Goblet Squats (3x12)", "Push-ups (3x12)", "Dumbbell Deadlifts (3x12)", "Plank Rows (3x10)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                9,
                "Mobility & Stretching",
                "/images/stretching.PNG",
                List.of("Stretching and mobility work (20 min)", "Foam rolling (10-15 min)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                10,
                "Cardio + Core",
                "/images/cardio.PNG",
                List.of("20-30 min cycling or brisk walk", "Side Plank (3x30 sec each side)", "Russian Twists (3x15)", "Mountain Climbers (3x20)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                11,
                "Upper Body Strength",
                "/images/upperBody.PNG",
                List.of("Incline Push-ups (3x12)", "Dumbbell Lateral Raises (3x12)", "Dumbbell Chest Press (3x12)", "Dumbbell Bicep Curls (3x12)"),
                beginnerPlan
        );

        workoutService.initWorkout(
                12,
                "Lower Body Strength",
                "/images/lowerBody.PNG",
                List.of("Split Squats (3x12 each leg)", "Step-ups (3x12 each leg)"),
                beginnerPlan
        );

        WorkoutPlan intermediatePlan = workoutPlanService.getWorkoutPlanByType("Intermediate Plan");

        workoutService.initWorkout(
                1,
                "Full-body Strength Training",
                "/images/fullBody.PNG",
                List.of("Squats (4x12)", "Bench Press or Chest Press (4x8)", "Dumbbell Rows (4x10)", "Plank (3x30 sec)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                2,
                "HIIT + Core",
                "/images/cardio.PNG",
                List.of("30 sec on, 30 sec off x 5 rounds:", "Jumping Jacks, Squat Jumps, Burpees", "Russian Twists (3x20)", "Bicycle Crunches (3x20)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                3,
                "Yoga or Mobility Work",
                "/images/stretching.PNG",
                List.of("Yoga session (20-30 min)", "Foam rolling (10-15 min)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                4,
                "Upper Body Strength",
                "/images/upperBody.PNG",
                List.of("Pull-ups (3x8) or Assisted Pull-ups", "Push-ups (3x15)", "Dumbbell Shoulder Press (4x10)", "Tricep Dips (3x12)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                5,
                "Lower Body Strength + Plyometrics",
                "/images/lowerBody.PNG",
                List.of("Deadlifts (4x8)", "Walking Lunges (4x12 each leg)", "Box Jumps (3x10)", "Glute Bridges (4x15)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                6,
                "Stretching & Mobility",
                "/images/stretching.PNG",
                List.of("Full-body stretch (20-30 min)", "Foam rolling (10-15 min)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                7,
                "Cardio + Core",
                "/images/cardio.PNG",
                List.of("30 min moderate cycling or running", "Side Plank (3x30 sec each side)", "Leg Raises (3x20)", "Mountain Climbers (3x30 sec)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                8,
                "Full-body Strength Training",
                "/images/fullBody.PNG",
                List.of("Front Squats (4x8)", "Bench Press (4x8)", "Dumbbell Rows (4x8)", "Russian Twists (3x20)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                9,
                "HIIT + Core",
                "/images/cardio.PNG",
                List.of("Burpees (30 sec x5)", "Jump Squats (30 sec x5)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                10,
                "Lower Body Strength",
                "/images/lowerBody.PNG",
                List.of("Bulgarian Split Squats (4x10 each leg)", "Deadlifts (4x10)", "Box Jumps (3x12)", "Calf Raises (3x20)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                11,
                "Upper Body Strength",
                "/images/upperBody.PNG",
                List.of("Pull-ups (3x8)", "Push-ups (3x20)", "Dumbbell Shoulder Press (4x10)", "Lateral Raises (3x12)"),
                intermediatePlan
        );

        workoutService.initWorkout(
                12,
                "Yoga and Foam Rolling",
                "/images/stretching.PNG",
                List.of("Yoga (20-30 min)", "Foam rolling (10-15 min)"),
                intermediatePlan
        );

        WorkoutPlan advancedPlan = workoutPlanService.getWorkoutPlanByType("Advanced Plan");

        workoutService.initWorkout(
                1,
                "Full-body Strength Training",
                "/images/fullBody.PNG",
                List.of("Deadlifts (5x5)", "Squats (5x5)", "Bench Press (5x5)", "Pull-ups (4x8)"),
                advancedPlan
        );

        workoutService.initWorkout(
                2,
                "HIIT + Plyometrics",
                "/images/cardio.PNG",
                List.of("Burpees (4x15)", "Box Jumps (4x12)", "Kettlebell Swings (4x20)", "Jump Lunges (4x12 each leg)"),
                advancedPlan
        );

        workoutService.initWorkout(
                3,
                "Mobility & Stretching",
                "/images/stretching.PNG",
                List.of("Stretching or yoga (20-30 min)", "Foam rolling (10-15 min)"),
                advancedPlan
        );

        workoutService.initWorkout(
                4,
                "Upper Body Strength",
                "/images/upperBody.PNG",
                List.of("Bench Press (5x5)", "Pull-ups (4x10)", "Dumbbell Shoulder Press (4x10)", "Dips (4x12)"),
                advancedPlan
        );

        workoutService.initWorkout(
                5,
                "Lower Body Strength + Sprints",
                "/images/lowerBody.PNG",
                List.of("Squats (5x5)", "Romanian Deadlifts (4x8)", "Box Jumps (4x12)", "Sprints (10 rounds x 30 sec work)"),
                advancedPlan
        );

        workoutService.initWorkout(
                6,
                "Endurance Conditioning",
                "/images/cardio.PNG",
                List.of("45 min steady-state cardio (running, cycling)"),
                advancedPlan
        );

        workoutService.initWorkout(
                7,
                "HIIT + Plyometrics",
                "/images/cardio.PNG",
                List.of("Sled Push (4x15 yards)", "Jumping Jacks (4x1 min)", "Burpees (4x20)", "Mountain Climbers (4x30 sec)"),
                advancedPlan
        );

        workoutService.initWorkout(
                8,
                "Full-body Strength Training",
                "/images/fullBody.PNG",
                List.of("Clean & Press (4x6)", "Squats (4x6)", "Deadlifts (4x6)", "Pull-ups (4x10)"),
                advancedPlan
        );

        workoutService.initWorkout(
                9,
                "Stretch & Foam Roll",
                "/images/stretching.PNG",
                List.of("Stretching (20 min)", "Foam rolling (10-15 min)"),
                advancedPlan
        );

        workoutService.initWorkout(
                10,
                "Lower Body Strength + Plyo",
                "/images/lowerBody.PNG",
                List.of("Front Squats (4x6)", "Kettlebell Swings (4x15)", "Jump Lunges (4x12 each leg)", "Calf Raises (4x20)"),
                advancedPlan
        );

        workoutService.initWorkout(
                11,
                "Upper Body Strength",
                "/images/upperBody.PNG",
                List.of("Incline Bench Press (4x6)", "Barbell Rows (4x6)", "Overhead Press (4x8)", "Tricep Dips (4x12)"),
                advancedPlan
        );

        workoutService.initWorkout(
                12,
                "Mobility + Stretching",
                "/images/stretching.PNG",
                List.of("Stretching or yoga (20-30 min)", "Foam rolling (10-15 min)"),
                advancedPlan
        );
    }
}









