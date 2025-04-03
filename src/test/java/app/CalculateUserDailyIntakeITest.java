package app;

import app.calculator.service.CalculatorService;
import app.user.model.User;
import app.user.model.UserActivityLevel;
import app.user.model.UserGender;
import app.user.model.UserGoal;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.CalculateRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class CalculateUserDailyIntakeITest {

    @Autowired
    private UserService userService;

    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void calculateProperlyUserDailyIntake() {

        CalculateRequest calculateRequest = CalculateRequest.builder()
                .gender(UserGender.FEMALE)
                .age(21)
                .activityLevel(UserActivityLevel.MODERATELY_ACTIVE)
                .height(170.0)
                .weight(60.0)
                .goal(UserGoal.LOSS_WEIGHT)
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("username")
                .email("email@gmail.com")
                .password("password")
                .confirmPassword("password")
                .build();

        User registeredUser = userService.register(registerRequest);
        double dailyIntake = calculatorService.calculateDailyIntake(calculateRequest);

        registeredUser.setDailyIntake(dailyIntake);
        userRepository.save(registeredUser);

        assertEquals(dailyIntake, registeredUser.getDailyIntake(), 0.01);
    }
}
