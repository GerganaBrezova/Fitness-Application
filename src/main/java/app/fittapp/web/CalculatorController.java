package app.fittapp.web;

import app.fittapp.security.UserAuthDetails;
import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.web.dto.CalculateRequest;
import app.fittapp.web.mapper.DtoMapper;
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
    public String calculateDailyIntake(@AuthenticationPrincipal UserAuthDetails userAuthDetails,
                                       @Valid CalculateRequest calculateRequest,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "calculator";
        }

        userService.editUserAdditionalDetails(userAuthDetails.getId(), calculateRequest);

        userService.calculateUserDailyIntake(userAuthDetails.getId() ,calculateRequest);

        return "redirect:/calculator";
    }
}
