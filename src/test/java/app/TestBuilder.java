package app;

import app.user.model.User;
import app.user.model.UserRole;
import app.workout_plan.model.WorkoutPlan;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TestBuilder {

    public static User testUser() {

        User user = User.builder()
                .username("Gery11")
                .email("gery@gmail.com")
                .password("123456")
                .points(100)
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .build();

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
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
