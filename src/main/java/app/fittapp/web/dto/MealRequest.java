package app.fittapp.web.dto;

import app.fittapp.meal.model.MealType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealRequest {

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Please select meal type.")
    private MealType type;

    @URL(message = "Please enter a valid URL format.")
    private String photoUrl;

    @Size(max = 40, message = "Description must be less than 40 symbols.")
    @NotBlank(message = "Description can not be empty.")
    private String description;

    @Positive(message = "Calories cannot must be positive.")
    @NotNull(message = "Calories can not be empty.")
    private int calories;
}
