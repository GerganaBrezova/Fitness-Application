package app.fittapp.web;

import app.fittapp.user.model.User;
import app.fittapp.user.service.UserService;
import app.fittapp.web.dto.EditRequest;
import app.fittapp.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
