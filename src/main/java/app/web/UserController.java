package app.web;

import app.exceptions.UserNotFound;
import app.security.UserAuthDetails;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.EditRequest;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUsersPage(@AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        ModelAndView modelAndView = new ModelAndView();

        List<User> users = userService.getAllUsers();
        User loggedUser = userService.getUserById(userAuthDetails.getId());

        modelAndView.setViewName("users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("loggedUser", loggedUser);
        modelAndView.addObject("searchPerformed", false);

        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView searchUsers(@RequestParam(required = false) String username, @AuthenticationPrincipal UserAuthDetails userAuthDetails) {

        boolean searchPerformed = (username != null && !username.trim().isEmpty());

        ModelAndView modelAndView = new ModelAndView("users");
        modelAndView.addObject("searchPerformed", searchPerformed);

        if (searchPerformed) {
            try {
                User searchedUser = userService.getUserByUsername(username);
                modelAndView.addObject("searchedUser", searchedUser);
            } catch (UserNotFound e) {
                modelAndView.addObject("userNotFoundMessage", e.getMessage());
            }
        }

        List<User> users = userService.getAllUsers();
        modelAndView.addObject("users", users);

        User loggedUser = userService.getUserById(userAuthDetails.getId());
        modelAndView.addObject("loggedUser", loggedUser);

        return modelAndView;
    }

    @PutMapping("/{userId}/role-change")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserRole(@PathVariable UUID userId) {

        userService.changeRoles(userId);

        return "redirect:/users";
    }

    @PutMapping("/{userId}/status-change")
    @PreAuthorize("hasRole('ADMIN')")
    public String changeUserStatus(@PathVariable UUID userId) {

        userService.changeStatus(userId);

        return "redirect:/users";
    }

    @GetMapping("/{id}/profile-edit")
    public ModelAndView getEditProfilePage(@PathVariable UUID id) {

        User user = userService.getUserById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("editRequest", DtoMapper.mapToEditRequest(user));

        return modelAndView;
    }

    @PutMapping("/{id}/profile-edit")
    public ModelAndView editUserProfile(@PathVariable UUID id, @Valid EditRequest editRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            User user = userService.getUserById(id);

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("edit-profile");
            modelAndView.addObject("user", user);
            modelAndView.addObject("editRequest", editRequest);

            return modelAndView;
        }

        userService.editUserDetails(editRequest, id);

        return new ModelAndView("redirect:/home");
    }
}
