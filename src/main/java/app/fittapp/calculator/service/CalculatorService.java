package app.fittapp.calculator.service;

import app.fittapp.user.model.UserGender;
import app.fittapp.web.dto.CalculateRequest;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculateDailyIntake(CalculateRequest calculateRequest) {

        double multiplier = switch (calculateRequest.getActivityLevel().name()) {
            case "LIGHTLY_ACTIVE" -> 1.375;
            case "MODERATELY_ACTIVE" -> 1.55;
            case "VERY_ACTIVE" -> 1.725;
            default -> 1.2;
        };

        int genderSpecificConstant = (calculateRequest.getGender() == UserGender.FEMALE) ? -161 : 5;

        double bmr = (10 * calculateRequest.getWeight()) + (6.25 * calculateRequest.getHeight()) - (5 * calculateRequest.getAge()) + genderSpecificConstant;

        return switch (calculateRequest.getGoal().name()) {
            case "GAIN_WEIGHT" -> (bmr * multiplier) + 0.2 * (bmr * multiplier);
            case "LOSS_WEIGHT" -> (bmr * multiplier) - 0.2 * (bmr * multiplier);
            default -> bmr * multiplier;
        };
    }
}
