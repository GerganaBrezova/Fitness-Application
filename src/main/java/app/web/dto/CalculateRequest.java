package app.web.dto;

import app.user.model.UserActivityLevel;
import app.user.model.UserGender;
import app.user.model.UserGoal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateRequest {

    @NotNull
    private UserGender gender;

    @Positive
    @NotNull
    private int age;

    @NotNull
    private UserActivityLevel activityLevel;

    @Positive
    @NotNull
    private double height;

    @Positive
    @NotNull
    private double weight;

    @NotNull
    private UserGoal goal;
}
