package app.fittapp.web;

import app.fittapp.meal.model.Meal;
import app.fittapp.meal.service.MealService;
import app.fittapp.security.UserAuthDetails;
import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.web.dto.MealRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public String addMeal(@AuthenticationPrincipal UserAuthDetails userAuthDetails, @Valid MealRequest mealRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "meals";
        }

        User user = userService.getUserById(userAuthDetails.getId());

        mealService.addNewMeal(mealRequest, user);

        return "redirect:/meals";
    }
}
