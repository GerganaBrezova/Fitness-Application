package app.web;

import app.exceptions.EmailAlreadyExists;
import app.exceptions.UsernameAlreadyExists;
import app.user.service.UserService;
import app.web.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToIndexEndpoint_thenReturnIndexView() throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getRequestToRegisterEndpoint_thenReturnRegisterView() throws Exception {

        MockHttpServletRequestBuilder request = get("/register");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void postRequestToRegisterEndpoint_happyPath() throws Exception {

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "Gery11")
                .formField("password", "123456")
                .formField("confirmPassword", "123456")
                .formField("email", "gery11@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpoint_whenUsernameAlreadyExist_thenRedirectToRegisterViewWithFlashParameter() throws Exception {

        when(userService.register(any())).thenThrow(new UsernameAlreadyExists("Username already exist!"));

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "Gery11")
                .formField("password", "123456")
                .formField("confirmPassword", "123456")
                .formField("email", "gery11@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("usernameAlreadyExistMessage"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpoint_whenEmailAlreadyExist_thenRedirectToRegisterViewWithFlashParameter() throws Exception {

        when(userService.register(any())).thenThrow(new EmailAlreadyExists("Username already exist!"));

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "Gery11")
                .formField("password", "123456")
                .formField("confirmPassword", "123456")
                .formField("email", "gery11@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("emailAlreadyExistMessage"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpoint_whenInvalidData_thenRedirectToRegisterView() throws Exception {

        when(userService.register(any())).thenThrow(new EmailAlreadyExists("Username already exist!"));

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "")
                .formField("password", "")
                .formField("confirmPassword", "")
                .formField("email", "gery11@gmail.com")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                        .andExpect(view().name("register"));
        verify(userService, never()).register(any());
    }

    @Test
    void getRequestToLoginEndpoint_thenReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/login");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest"));
    }

    @Test
    void getRequestToLoginEndpoint_withErrorParameter_thenReturnLoginViewAndErrorMessageAttribute() throws Exception {

        MockHttpServletRequestBuilder request = get("/login")
                .param("error", "");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("loginRequest", "errorMessage"));
    }

//    @Test
//    void getAuthenticatedRequestToHome_thenReturnHomeView() throws Exception {
//
//        when(userService.getUserById(any())).thenReturn(testUser());
//
//        UUID userId = UUID.randomUUID();
//        UserAuthDetails userAuthDetails = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);
//        MockHttpServletRequestBuilder request = get("/home")
//                .with(user(userAuthDetails));
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(view().name("home"))
//                .andExpect(model().attributeExists("user"));
//        verify(userService, times(1)).getUserById(any());
//    }

}
