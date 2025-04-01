package app.calculator;

import app.calculator.service.CalculatorService;
import app.user.model.UserActivityLevel;
import app.user.model.UserGender;
import app.user.model.UserGoal;
import app.web.dto.CalculateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceUTest {

    @InjectMocks
    private CalculatorService calculatorService;

    @ParameterizedTest
    @MethodSource("calculatorRequestArguments")
    void whenCalculateDailyIntake_thenReturnsCorrectCalculations(UserGender userGender,
                                                                 int age,
                                                                 UserActivityLevel userActivityLevel,
                                                                 double height,
                                                                 double weight,
                                                                 UserGoal userGoal) {
        CalculateRequest calculateRequest = CalculateRequest.builder()
                .gender(userGender)
                .age(age)
                .height(height)
                .weight(weight)
                .goal(userGoal)
                .activityLevel(userActivityLevel)
                .build();

        int genderSpecificConstant = (userGender == UserGender.FEMALE) ? -161 : 5;
        double bmr = (10 * weight) + (6.25 * height) - (5 * age) + genderSpecificConstant;

        double multiplier = switch (userActivityLevel) {
            case LIGHTLY_ACTIVE -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE -> 1.725;
            default -> 1.2;
        };

        double expectedIntake = switch (userGoal) {
            case GAIN_WEIGHT -> (bmr * multiplier) + 0.2 * (bmr * multiplier);
            case LOSS_WEIGHT -> (bmr * multiplier) - 0.2 * (bmr * multiplier);
            default -> bmr * multiplier;
        };

        double actualIntake = calculatorService.calculateDailyIntake(calculateRequest);

        assertEquals(expectedIntake, actualIntake, 0.01);
    }

    private static Stream<Arguments> calculatorRequestArguments() {
        return Stream.of(
                Arguments.of(UserGender.MALE, 30, UserActivityLevel.MODERATELY_ACTIVE, 175.0, 70.0, UserGoal.GAIN_WEIGHT),
                Arguments.of(UserGender.FEMALE, 25, UserActivityLevel.LIGHTLY_ACTIVE, 160.0, 60.0, UserGoal.LOSS_WEIGHT),
                Arguments.of(UserGender.MALE, 40, UserActivityLevel.VERY_ACTIVE, 180.0, 80.0, UserGoal.MAINTAIN_WEIGHT)
        );
    }
}
