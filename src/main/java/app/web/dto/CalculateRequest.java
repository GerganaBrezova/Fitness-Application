package app.web.dto;

import app.user.model.UserActivityLevel;
import app.user.model.UserGender;
import app.user.model.UserGoal;
import jakarta.validation.constraints.NotBlank;
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

    @Positive(message = "Age must be positive!")
    @NotNull(message = "Age is required!")
    private Integer age;

    @NotNull
    private UserActivityLevel activityLevel;

    @Positive(message = "Height must be positive!")
    @NotNull(message = "Height is required!")
    private Double height;

    @Positive(message = "Weight must be positive!")
    @NotNull(message = "Weight is required!")
    private Double weight;

    @NotNull
    private UserGoal goal;
}
