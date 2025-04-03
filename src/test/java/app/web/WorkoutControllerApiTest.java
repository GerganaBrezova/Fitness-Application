package app.web;

import app.security.UserAuthDetails;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.workout.model.CompletedWorkout;
import app.workout.model.Workout;
import app.workout.service.WorkoutService;
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

@WebMvcTest(WorkoutController.class)
public class WorkoutControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WorkoutService workoutService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToWorkoutsHistoryPage_thenReturnWorkoutsHistoryView() throws Exception {

        when(userService.getUserById(any())).thenReturn(testUser());

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/workouts/completed").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("workouts-history"))
                .andExpect(model().attributeExists("user", "completedWorkouts"));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void getUnauthenticatedRequestToWorkoutsHistoryPage_thenReturnWorkoutsHistoryView() throws Exception {

        MockHttpServletRequestBuilder request = get("/workouts/completed");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(userService, never()).getUserById(any());
    }

    @Test
    void postAuthenticatedRequestToWorkoutsHistoryPage_thenRedirectToWorkoutsHistoryView() throws Exception {

        UUID workoutID = UUID.randomUUID();
        Workout firstWorkout = Workout.builder()
                .id(workoutID)
                .build();

        when(userService.getUserById(any())).thenReturn(testUser());
        when(workoutService.getWorkoutById(workoutID)).thenReturn(firstWorkout);

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/workouts/{workoutId}/completed", workoutID)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts/completed"));

        verify(userService, times(1)).getUserById(userId);
        verify(workoutService, times(1)).getWorkoutById(workoutID);
    }

    @Test
    void postUnauthenticatedRequestToWorkoutsHistoryPage_thenRedirectToLoginView() throws Exception {

        UUID workoutID = UUID.randomUUID();

        MockHttpServletRequestBuilder request = post("/workouts/{workoutId}/completed", workoutID).with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        verify(userService, never()).getUserById(any());
        verify(workoutService, never()).getWorkoutById(workoutID);
    }

    @Test
    void deleteAuthenticatedRequestToWorkoutsHistoryPageByAdmin_thenRedirectToWorkoutsHistoryView() throws Exception {

        UUID completedWorkoutID = UUID.randomUUID();
        CompletedWorkout completedWorkout = CompletedWorkout.builder()
                .id(completedWorkoutID)
                .build();

        when(workoutService.getCompletedWorkoutById(completedWorkoutID)).thenReturn(completedWorkout);

        UUID userId = UUID.randomUUID();
        UserAuthDetails principal = new UserAuthDetails(userId, "Gery11", "123456", "gery@gmail.com", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = delete("/workouts/completed/{completedWorkoutId}", completedWorkoutID)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/workouts/completed"));

       verify(workoutService, times(1)).deleteCompletedWorkout(completedWorkout);
       verify(workoutService, times(1)).getCompletedWorkoutById(completedWorkoutID);
    }

    @Test
    void deleteUnauthenticatedRequestToWorkoutsHistoryPageByAdmin_thenRedirectToLoginView() throws Exception {

        UUID completedWorkoutID = UUID.randomUUID();

        MockHttpServletRequestBuilder request = delete("/workouts/completed/{completedWorkoutId}", completedWorkoutID).with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

        verify(workoutService, never()).getCompletedWorkoutById(completedWorkoutID);
        verify(workoutService, never()).deleteCompletedWorkout(any(CompletedWorkout.class));
    }
}
