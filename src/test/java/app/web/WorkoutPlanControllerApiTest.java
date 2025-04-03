package app.web;

import app.post.model.Post;
import app.security.UserAuthDetails;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.workout.model.Workout;
import app.workout_plan.model.WorkoutPlan;
import app.workout_plan.service.WorkoutPlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static app.TestBuilder.testUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkoutPlanController.class)
public class WorkoutPlanControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToPlansPage_thenReturnPlansView() throws Exception {

        WorkoutPlan firstWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .build();

        WorkoutPlan secondWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .build();

        List<WorkoutPlan> allSystemWorkoutPlans = List.of(firstWorkoutPlan, secondWorkoutPlan);
        when(workoutPlanService.getAllSystemWorkoutPlans()).thenReturn(allSystemWorkoutPlans);
        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/plans").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("plans"))
                .andExpect(model().attributeExists("user", "allSystemWorkoutPlans"));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUnauthenticatedRequestToPlansPage_thenReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/plans");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(userService, never()).getUserById(any());
    }

    @Test
    void postAuthenticatedRequestToPlansPage_thenRedirectToPlansView() throws Exception {

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        UUID workoutPlanId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/plans/{workoutPlanId}/unlock", workoutPlanId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/plans"));

        verify(workoutPlanService, times(1)).unlockWorkoutPlan(userId, workoutPlanId);
    }

    @Test
    void postUnauthenticatedRequestToPlansPage_thenRedirectToLoginView() throws Exception {

        UUID workoutPlanId = UUID.randomUUID();
        MockHttpServletRequestBuilder request = post("/plans/{workoutPlanId}/unlock", workoutPlanId).with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        verify(workoutPlanService, never()).unlockWorkoutPlan(any(), any());
    }

    @Test
    void getAuthenticatedRequestToPlansDetailsPage_thenReturnPlansDetailsView() throws Exception {

        UUID workoutPlanId = UUID.randomUUID();
        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .id(workoutPlanId)
                .build();

        when(userService.getUserById(any())).thenReturn(testUser());
        when(workoutPlanService.getWorkoutPlanById(workoutPlanId)).thenReturn(workoutPlan);

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/plans/{workoutPlanId}/workouts", workoutPlanId)
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("plan-details"))
                .andExpect(model().attributeExists("user", "workoutPlan"));
        verify(userService, times(1)).getUserById(userId);
        verify(workoutPlanService, times(1)).getWorkoutPlanById(workoutPlanId);
    }

    @Test
    void getUnauthenticatedRequestToPlansDetailsPage_thenReturnLoginView() throws Exception {

        UUID workoutPlanId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = get("/plans/{workoutPlanId}/workouts", workoutPlanId);

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(userService, never()).getUserById(any());
        verify(workoutPlanService, never()).getWorkoutPlanById(any());
    }
}
