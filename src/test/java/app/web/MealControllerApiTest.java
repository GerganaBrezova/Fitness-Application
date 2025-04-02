package app.web;

import app.meal.model.MealType;
import app.meal.service.MealService;
import app.security.UserAuthDetails;
import app.user.model.UserRole;
import app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static app.TestBuilder.testUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
public class MealControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MealService mealService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToMealPage_thenReturnMealView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());
        when(userService.getCaloriesFromMeals(any())).thenReturn(300);

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/meals").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attributeExists("user", "mealRequest", "caloriesFromMeals"));
        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).getCaloriesFromMeals(any());
    }

    @Test
    void getUnauthenticatedRequestToMealPage_thenReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/meals");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void postAuthenticatedRequestToMealPage_thenRedirectToMealView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());
        when(userService.getCaloriesFromMeals(any())).thenReturn(300);

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/meals/add")
                .with(user(principal))
                .formField("type", MealType.BREAKFAST.toString())
                .formField("description", "meat")
                .formField("calories", "300")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meals"));

        verify(mealService, times(1)).addNewMeal(any(), any());
    }

    @Test
    void postAuthenticatedRequestToMealPageWithInvalidData_thenReturnMealView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());
        when(userService.getCaloriesFromMeals(any())).thenReturn(300);

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/meals/add")
                .with(user(principal))
                .formField("type", MealType.BREAKFAST.toString())
                .formField("description", "")
                .formField("calories", "")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(model().attributeExists("user", "mealRequest", "caloriesFromMeals"));

        verify(mealService, never()).addNewMeal(any(), any());
    }

    @Test
    void deleteUnauthenticatedRequestToMealPage_thenReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/meals/add");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(mealService, never()).addNewMeal(any(), any());
    }

    @Test
    void deleteAuthenticatedRequestToMealPage_thenRedirectToMealPage() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        UUID mealId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = delete("/meals/{mealId}/delete", mealId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meals"));

        verify(mealService, times(1)).deleteMealById(eq(mealId), any());
    }

    @Test
    void deleteUnauthenticatedRequestToMealPage_thenRedirectToLoginPage() throws Exception {

        UUID mealId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = delete("/meals/{mealId}/delete", mealId).with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        verify(mealService, never()).deleteMealById(eq(mealId), any());
    }

}
