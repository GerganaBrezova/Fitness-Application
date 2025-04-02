package app.workout;

import app.event.UserCompletedWorkoutEventProducer;
import app.event.payload.UserCompletedWorkoutEvent;
import app.exceptions.WorkoutNotFound;
import app.user.model.User;
import app.user.service.UserService;
import app.workout.model.CompletedWorkout;
import app.workout.model.Workout;
import app.workout.repository.CompletedWorkoutRepository;
import app.workout.repository.WorkoutRepository;
import app.workout.service.WorkoutService;
import app.workout_plan.model.WorkoutPlan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceUTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private CompletedWorkoutRepository completedWorkoutRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserCompletedWorkoutEventProducer userCompletedWorkoutEventProducer;

    @InjectMocks
    private WorkoutService workoutService;

    //Initialize Workout
    @Test
    void testIfSuccessfullyInitializedWorkout() {

        int day = 1;
        String imagePath = "image_path";
        String type = "Full-body Strength";
        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .type("Beginner Plan")
                .workouts(new ArrayList<>(List.of(new Workout())))
                .build();
        List<String> exercises = List.of("Exercise1", "Exercise2");

        workoutService.initWorkout(day, type, imagePath, exercises, workoutPlan);

        ArgumentCaptor<Workout> captor = ArgumentCaptor.forClass(Workout.class);
        verify(workoutRepository).save(captor.capture());

        Workout savedWorkout = captor.getValue();

        assertNotNull(savedWorkout);
        assertEquals(day, savedWorkout.getDay());
        assertEquals(type, savedWorkout.getType());
        assertEquals(imagePath, savedWorkout.getImagePath());
        assertEquals(exercises, savedWorkout.getExercises());
        assertEquals(workoutPlan, savedWorkout.getWorkoutPlan());
        verify(workoutRepository, times(1)).save(any(Workout.class));
    }

    //Initialize Completed Workout
    @Test
    void testIfSuccessfullyInitializedCompletedWorkoutForUser_andAddingFivePointsToUser() {

        int day = 1;
        String imagePath = "image_path";
        String type = "Full-body Strength";
        WorkoutPlan workoutPlan = WorkoutPlan.builder().build();
        List<String> exercises = List.of("Exercise1", "Exercise2");

        Workout workout = Workout.builder()
                .day(day)
                .type(type)
                .imagePath(imagePath)
                .exercises(exercises)
                .workoutPlan(workoutPlan)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .points(100)
                .build();

        workoutService.initCompletedWorkout(user, workout);

        ArgumentCaptor<CompletedWorkout> captor = ArgumentCaptor.forClass(CompletedWorkout.class);
        verify(completedWorkoutRepository, times(1)).save(captor.capture());

        CompletedWorkout savedWorkout = captor.getValue();

        assertNotNull(savedWorkout);
        assertEquals(day, savedWorkout.getDay());
        assertEquals(user, savedWorkout.getUser());
        assertEquals(105, user.getPoints());

        verify(completedWorkoutRepository, times(1)).save(any(CompletedWorkout.class));
        verify(userService, times(1)).saveUser(user);

        ArgumentCaptor<UserCompletedWorkoutEvent> eventCaptor = ArgumentCaptor.forClass(UserCompletedWorkoutEvent.class);
        verify(userCompletedWorkoutEventProducer, times(1)).sendEvent(eventCaptor.capture());

        UserCompletedWorkoutEvent capturedEvent = eventCaptor.getValue();

        assertNotNull(capturedEvent);
        assertEquals(user.getId(), capturedEvent.getUserId());
    }

    //Get All System Workouts
    @Test
    void testIfAllSystemWorkoutsAreReturned() {

        Workout firstWorkout = Workout.builder()
                .id(UUID.randomUUID())
                .build();
        Workout secondWorkout = Workout.builder()
                .id(UUID.randomUUID())
                .build();

        List<Workout> workouts = List.of(firstWorkout, secondWorkout);

        when(workoutRepository.findAll()).thenReturn(workouts);

        List<Workout> actualWorkouts = workoutService.getAllSystemWorkouts();

        assertNotNull(actualWorkouts);
        assertEquals(workouts.size(), actualWorkouts.size());
        assertTrue(workouts.containsAll(actualWorkouts));
        assertTrue(actualWorkouts.containsAll(workouts));
    }

    //Get Workout By ID
    @Test
    void throwsExceptionWhen_tryingToGetWorkout_withNonExistingID() {

        when(workoutRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFound.class, () -> workoutService.getWorkoutById(any()));
    }

    @Test
    void returnsCorrectWorkoutWhen_tryingToGetWorkout_withExistingID() {

        Workout workout = Workout.builder()
                .id(UUID.randomUUID())
                .build();

        when(workoutRepository.findById(any())).thenReturn(Optional.of(workout));

        Workout actualWorkout = workoutService.getWorkoutById(any());

        assertNotNull(actualWorkout);
        assertEquals(workout, actualWorkout);
    }

    //Get Completed Workout By ID
    @Test
    void throwsExceptionWhen_tryingToGetCompletedWorkout_withNonExistingID() {

        when(completedWorkoutRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkoutNotFound.class, () -> workoutService.getCompletedWorkoutById(any()));
    }

    @Test
    void returnsCorrectWorkoutWhen_tryingToGetCompletedWorkout_withExistingID() {

        CompletedWorkout completedWorkout = CompletedWorkout.builder()
                .id(UUID.randomUUID())
                .build();

        when(completedWorkoutRepository.findById(any())).thenReturn(Optional.of(completedWorkout));

        CompletedWorkout actualCompletedWorkout = workoutService.getCompletedWorkoutById(any());

        assertNotNull(actualCompletedWorkout);
        assertEquals(completedWorkout, actualCompletedWorkout);
    }

    //Delete Completed Workout
    @Test
    void whenDeleteCompletedWorkout_thenCompletedWorkoutIsDeleted() {

        CompletedWorkout completedWorkout = CompletedWorkout.builder()
                .id(UUID.randomUUID())
                .build();

        workoutService.deleteCompletedWorkout(completedWorkout);

       verify(completedWorkoutRepository, times(1)).delete(completedWorkout);
    }
}













