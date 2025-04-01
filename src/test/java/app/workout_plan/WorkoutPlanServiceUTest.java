package app.workout_plan;

import app.exceptions.InsufficientAmountOfPoints;
import app.exceptions.WorkoutPlanNotFound;
import app.user.model.User;
import app.user.service.UserService;
import app.workout.model.Workout;
import app.workout_plan.model.WorkoutPlan;
import app.workout_plan.repository.WorkoutPlanRepository;
import app.workout_plan.service.WorkoutPlanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanServiceUTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    //Get all system workout plans
    @Test
    void whenGetAllSystemWorkoutPlans_returnsAllSystemWorkoutPlansSorted() {

        WorkoutPlan firstWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Beginner Plan")
                .build();

        WorkoutPlan secondWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Intermediate Plan")
                .build();

        WorkoutPlan thirdWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Advanced Plan")
                .build();

        WorkoutPlan forthWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Elite Plan")
                .build();

        List<WorkoutPlan> expectedSortedWorkoutPlans = List.of(firstWorkoutPlan, secondWorkoutPlan, thirdWorkoutPlan, forthWorkoutPlan);

        when(workoutPlanRepository.findAll()).thenReturn(expectedSortedWorkoutPlans);

        List<WorkoutPlan> actualSortedWorkoutPlans = workoutPlanService.getAllSystemWorkoutPlans();

        assertEquals(actualSortedWorkoutPlans.size(), expectedSortedWorkoutPlans.size());
        assertThat(actualSortedWorkoutPlans).isEqualTo(expectedSortedWorkoutPlans);
    }

    //Initialize Workout Plan
    @ParameterizedTest
    @MethodSource("workoutPlanArguments")
    void successfullyInitializeWorkoutPlan_withGivenParameters(String type, String name, String targetAudience, String workoutFocus, int pointsNeeded) {

        workoutPlanService.initWorkoutPlan(type, name, targetAudience, workoutFocus, pointsNeeded);

        ArgumentCaptor<WorkoutPlan> captor = ArgumentCaptor.forClass(WorkoutPlan.class); // -> "catch" the WorkoutPlan object that is passed to save()
        verify(workoutPlanRepository).save(captor.capture()); // -> checks that save() was called exactly once

        WorkoutPlan savedWorkoutPlan = captor.getValue();

        assertThat(savedWorkoutPlan.getType()).isEqualTo(type);
        assertThat(savedWorkoutPlan.getName()).isEqualTo(name);
        assertThat(savedWorkoutPlan.getTargetAudience()).isEqualTo(targetAudience);
        assertThat(savedWorkoutPlan.getWorkoutFocus()).isEqualTo(workoutFocus);
        assertThat(savedWorkoutPlan.getPointsNeeded()).isEqualTo(pointsNeeded);
        verify(workoutPlanRepository, times(1)).save(any(WorkoutPlan.class));
    }

    private static Stream<Arguments> workoutPlanArguments() {
        return Stream.of(Arguments.of("Beginner Plan", "Foundations", "Beginners", "Building strength", 100));
    }

    //Get workout plan by ID
    @Test
    void throwsExceptionWhen_tryingToGetWorkoutPlan_withNonExistingId() {

        when(workoutPlanRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkoutPlanNotFound.class, () -> workoutPlanService.getWorkoutPlanById(any()));
    }

    @Test
    void returnsCorrectWorkoutPlan_whenTryingToGetWorkoutPlanWithExistingId() {

        UUID workoutPlanId = UUID.randomUUID();
        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .id(workoutPlanId)
                .build();

        when(workoutPlanRepository.findById(workoutPlanId)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlan actualPlan = workoutPlanService.getWorkoutPlanById(workoutPlanId);

        assertNotNull(actualPlan);
        assertThat(actualPlan.getId()).isEqualTo(workoutPlanId);
        verify(workoutPlanRepository).findById(workoutPlanId);
    }

    //Unlock workout plan
    @Test
    void throwsExceptionWhen_tryingToUnlockNewWorkoutPlan_withNotEnoughPoints() {

        UUID userId = UUID.randomUUID();
        UUID workoutPlanId = UUID.randomUUID();

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .id(workoutPlanId)
                .pointsNeeded(300)
                .build();

        User user = User.builder()
                .id(userId)
                .points(200)
                .build();

        when(workoutPlanRepository.findById(workoutPlanId)).thenReturn(Optional.of(workoutPlan));
        when(userService.getUserById(userId)).thenReturn(user);

        InsufficientAmountOfPoints exception = assertThrows(InsufficientAmountOfPoints.class,
                () -> workoutPlanService.unlockWorkoutPlan(userId, workoutPlanId));

        assertEquals("Not enough points: 100 remains.", exception.getMessage());
    }

    @Test
    void whenUnlockingWorkoutPlan_withEnoughPoints_thenWorkoutPlanUnlocked_andUserPointsAreReducedProperly() {

        UUID userId = UUID.randomUUID();
        UUID workoutPlanId = UUID.randomUUID();

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .id(workoutPlanId)
                .pointsNeeded(300)
                .build();

        User user = User.builder()
                .id(userId)
                .points(400)
                .workoutPlans(new ArrayList<>())
                .build();

        when(workoutPlanRepository.findById(workoutPlanId)).thenReturn(Optional.of(workoutPlan));
        when(userService.getUserById(userId)).thenReturn(user);

        workoutPlanService.unlockWorkoutPlan(userId, workoutPlanId);

        assertThat(user.getWorkoutPlans()).contains(workoutPlan);
        assertThat(user.getPoints()).isEqualTo(100);
        verify(userService, times(1)).saveUser(user);
    }

    //TODO: test the event

    //Get workout plan by title
    @Test
    void throwsExceptionWhen_tryingToGetWorkoutPlan_withNonExistingType() {

        when(workoutPlanRepository.findByType(any())).thenReturn(Optional.empty());

        assertThrows(WorkoutPlanNotFound.class, () -> workoutPlanService.getWorkoutPlanByType(any()));
    }

    @Test
    void returnsCorrectWorkoutPlan_whenTryingToGetWorkoutPlanWithExistingType() {

        WorkoutPlan firstWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Beginner Plan")
                .build();

        WorkoutPlan secondWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Intermediate Plan")
                .build();

        WorkoutPlan thirdWorkoutPlan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .type("Advanced Plan")
                .build();

        List<WorkoutPlan> workoutPlans = List.of(firstWorkoutPlan, secondWorkoutPlan, thirdWorkoutPlan);

        when(workoutPlanRepository.findByType("Beginner Plan")).thenReturn(Optional.of(firstWorkoutPlan));

        WorkoutPlan actualPlan = workoutPlanService.getWorkoutPlanByType("Beginner Plan");

        assertNotNull(actualPlan);
        assertThat(actualPlan.getId()).isEqualTo(firstWorkoutPlan.getId());
    }

    //Sort workout plan's workouts
    @Test
    void testIfWorkoutsAreProperlySorted() {

        Workout firstWorkout = Workout.builder()
                .day(1)
                .build();

        Workout secondWorkout = Workout.builder()
                .day(2)
                .build();

        Workout thirdWorkout = Workout.builder()
                .day(3)
                .build();

        WorkoutPlan plan = WorkoutPlan.builder()
                .id(UUID.randomUUID())
                .workouts(new ArrayList<>(List.of(secondWorkout, thirdWorkout, firstWorkout)))
                .build();

        List<Workout> expectedWorkouts = List.of(firstWorkout, secondWorkout, thirdWorkout);

        workoutPlanService.sortWorkoutsByDay(plan);

        assertThat(plan.getWorkouts()).isEqualTo(expectedWorkouts);
    }
}










