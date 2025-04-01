package app.web;

import app.security.UserAuthDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CalculateRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/calculator")
public class CalculatorController {

    private final UserService userService;

    @Autowired
    public CalculatorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getCalculatorPage(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        User user = userService.getUserById(userAuthDetails.getId());

        ModelAndView modelAndView = new ModelAndView("calculator");
        modelAndView.addObject("calculateRequest", DtoMapper.mapToCalculateRequest(user));
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @PostMapping
    public ModelAndView calculateDailyIntake(@AuthenticationPrincipal UserAuthDetails userAuthDetails,
                                             @Valid CalculateRequest calculateRequest,
                                             BindingResult bindingResult) {

        User user = userService.getUserById(userAuthDetails.getId());

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("calculator");
            modelAndView.addObject("calculateRequest", calculateRequest);
            modelAndView.addObject("user", user);
            return modelAndView;
        }

        userService.editUserAdditionalDetails(userAuthDetails.getId(), calculateRequest);
        userService.calculateUserDailyIntake(userAuthDetails.getId(), calculateRequest);

        return new ModelAndView("redirect:/calculator");
    }
}
