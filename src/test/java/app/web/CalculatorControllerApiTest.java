package app.web;

import app.security.UserAuthDetails;
import app.user.model.*;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import static app.TestBuilder.testUser;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
public class CalculatorControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToCalculatorPage_thenReturnCalculatorView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/calculator")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("user", "calculateRequest"));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUnauthenticatedRequestToCalculatorPage_thenRedirectToLogin() throws Exception {

        MockHttpServletRequestBuilder request = get("/calculator");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void postAuthenticatedRequestToCalculatorPage_thenRedirectToCalculatorView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/calculator")
                .with(user(principal))
                .formField("age", "21")
                .formField("gender", UserGender.FEMALE.toString())
                .formField("activityLevel", UserActivityLevel.MODERATELY_ACTIVE.toString())
                .formField("height", "170")
                .formField("weight", "60")
                .formField("goal", UserGoal.LOSS_WEIGHT.toString())
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/calculator"));

        verify(userService, times(1)).editUserAdditionalDetails(eq(principal.getId()), any());
        verify(userService, times(1)).calculateUserDailyIntake(eq(principal.getId()), any());

        //eq(...) => make principal.getId() a matcher
    }

    @Test
    void postUnauthenticatedRequestToCalculatorPage_thenRedirectToLogin() throws Exception {

        MockHttpServletRequestBuilder request = post("/calculator").with(csrf());
        ;

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void postAuthenticatedRequestToCalculatorPageWhenInvalidData_thenReturnCalculatorPage() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/calculator")
                .with(user(principal))
                .formField("age", "")
                .formField("gender", UserGender.FEMALE.toString())
                .formField("activityLevel", UserActivityLevel.MODERATELY_ACTIVE.toString())
                .formField("height", "")
                .formField("weight", "")
                .formField("goal", UserGoal.LOSS_WEIGHT.toString())
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("user", "calculateRequest"));

        verify(userService, never()).editUserAdditionalDetails(eq(principal.getId()), any());
        verify(userService, never()).calculateUserDailyIntake(eq(principal.getId()), any());

        //eq(...) => make principal.getId() a matcher
    }
}
