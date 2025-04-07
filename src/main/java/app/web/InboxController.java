package app.web;

import app.inbox_message.InboxMessageClient;
import app.security.UserAuthDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.inbox_message.client.dto.InboxMessageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/inbox")
public class InboxController {

    private final InboxMessageClient inboxMessageClientClient;
    private final UserService userService;

    public InboxController(InboxMessageClient inboxClient, UserService userService) {
        this.inboxMessageClientClient = inboxClient;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getInboxPage(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        UUID userId = userAuthDetails.getId();
        User user = userService.getUserById(userId);

        List<InboxMessageRequest> messages = inboxMessageClientClient.getInboxMessages(userId);

        ModelAndView modelAndView = new ModelAndView("inbox");
        modelAndView.addObject("messages", messages);
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
