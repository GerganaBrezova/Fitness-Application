package app.meal.service;

import app.exceptions.ExcessiveNumberOfMeals;
import app.exceptions.MealNotFound;
import app.meal.model.Meal;
import app.meal.repository.MealRepository;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.MealRequest;
import org.springframework.stereotype.Service;
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

        if (user.getMeals().size() == 3) {
            throw new ExcessiveNumberOfMeals("Cannot add more than 3 meals.");
        }

        Meal meal = Meal.builder()
                .mealType(mealRequest.getType())
                .photoUrl(mealRequest.getPhotoUrl())
                .description(mealRequest.getDescription())
                .calories(mealRequest.getCalories())
                .user(user)
                .build();

        mealRepository.save(meal);

        user.getMeals().add(meal);
        userService.saveUser(user);
    }

    public void deleteMealById(UUID mealId, User user) {

        Meal meal = getMealById(mealId);

        user.getMeals().remove(meal);

        mealRepository.deleteById(mealId);

        userService.saveUser(user);
    }

    public Meal getMealById(UUID mealId) {
        return mealRepository.findById(mealId).orElseThrow(() -> new MealNotFound("Meal with id %s was not found.".formatted(mealId)));
    }

    public void deleteAllMeals() {
        mealRepository.deleteAll();
    }
}
