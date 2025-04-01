package app.web;

import app.meal.model.Meal;
import app.meal.service.MealService;
import app.security.UserAuthDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.MealRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/meals")
public class MealController {

    private final UserService userService;
    private final MealService mealService;

    @Autowired
    public MealController(UserService userService, MealService mealService) {
        this.userService = userService;
        this.mealService = mealService;
    }

    @GetMapping
    public ModelAndView getMealsPage(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        User user = userService.getUserById(userAuthDetails.getId());
        int caloriesFromMeals = userService.getCaloriesFromMeals(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("meals");

        modelAndView.addObject("user", user);
        modelAndView.addObject("mealRequest", new MealRequest());
        modelAndView.addObject("caloriesFromMeals", caloriesFromMeals);

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addMeal(@AuthenticationPrincipal UserAuthDetails userAuthDetails, @Valid MealRequest mealRequest, BindingResult bindingResult) {

        User user = userService.getUserById(userAuthDetails.getId());
        int caloriesFromMeals = userService.getCaloriesFromMeals(user);

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("meals");
            modelAndView.addObject("user", user);
            modelAndView.addObject("mealRequest", mealRequest);
            modelAndView.addObject("caloriesFromMeals", caloriesFromMeals);
            return modelAndView;
        }

        mealService.addNewMeal(mealRequest, user);
        return new ModelAndView("redirect:/meals");
    }

    @DeleteMapping("/{mealId}/delete")
    public String deleteMeal(@PathVariable UUID mealId, @AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        User user = userService.getUserById(userAuthDetails.getId());

        mealService.deleteMealById(mealId, user);

        return "redirect:/meals";
    }
}
