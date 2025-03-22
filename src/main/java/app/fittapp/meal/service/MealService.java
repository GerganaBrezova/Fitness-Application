package app.fittapp.meal.service;

import app.fittapp.exceptions.DomainException;
import app.fittapp.meal.model.Meal;
import app.fittapp.meal.repository.MealRepository;
import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.web.dto.MealRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserService userService;

    public MealService(MealRepository mealRepository, UserService userService) {
        this.mealRepository = mealRepository;
        this.userService = userService;
    }

    public void addNewMeal(MealRequest mealRequest, User user) {

        if (user.getMeals().size() >= 3) {
            throw new DomainException("Cannot add more than 3 meals.");
        }

        Meal meal = Meal.builder()
                .mealType(mealRequest.getType())
                .photoUrl(mealRequest.getPhotoUrl())
                .description(mealRequest.getDescription())
                .calories(mealRequest.getCalories())
                .user(user)
                .build();

        mealRepository.save(meal);
    }

    public void deleteMealById(UUID mealId, User user) {

        Meal meal = getMealById(mealId);

        user.getMeals().remove(meal);

        mealRepository.deleteById(mealId);

        userService.saveUser(user);
    }

    private Meal getMealById(UUID mealId) {
        return mealRepository.findById(mealId).orElseThrow(() -> new DomainException("Meal with id %s was not found.".formatted(mealId)));
    }
}
