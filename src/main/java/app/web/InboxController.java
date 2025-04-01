package app.web;


import app.security.UserAuthDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.InboxMessageRequest;
import ch.qos.logback.core.model.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.UUID;

@Controller
@RequestMapping("/inbox")
public class InboxController {

    private final RestTemplate restTemplate;
    private final UserService userService;

    public InboxController(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getInbox(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        UUID userId = userAuthDetails.getId();
        User user = userService.getUserById(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("inbox");

        String url = "http://localhost:8081/api/v1/inbox/" + userId;
        InboxMessageRequest[] messages = restTemplate.getForObject(url, InboxMessageRequest[].class);

        assert messages != null;
        modelAndView.addObject("messages", Arrays.asList(messages));
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
