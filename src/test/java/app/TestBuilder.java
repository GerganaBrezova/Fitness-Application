package app;

import app.user.model.User;
import app.user.model.UserRole;
import app.workout.model.CompletedWorkout;
import app.workout_plan.model.WorkoutPlan;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static User testUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("Gery11")
                .email("gery@gmail.com")
                .points(100)
                .role(UserRole.USER)
                .isActive(true)
                .completedWorkouts(new ArrayList<>(List.of(new CompletedWorkout(), new CompletedWorkout())))
                .createdOn(LocalDateTime.now())
                .build();

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Beginner Plan")
                .name("Foundations")
                .targetAudience("Beginners")
                .workoutFocus("Building Strength")
                .pointsNeeded(0)
                .build();

        user.setWorkoutPlans(new ArrayList<>(List.of(workoutPlan)));

        return user;
    }
}
