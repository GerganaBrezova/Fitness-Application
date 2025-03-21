package app.fitnessapp.web;

import app.fitnessapp.web.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping("/")
    public String getIndexPage() {
        return "index";
    }

//    @GetMapping("/register")
//    public ModelAndView getRegisterPage() {
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("register");
//        modelAndView.addObject("registerRequest", new RegisterRequest());
//
//        return modelAndView;
//    }
}
