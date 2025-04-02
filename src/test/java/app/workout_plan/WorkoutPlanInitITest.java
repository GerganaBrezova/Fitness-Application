package app.workout_plan;

import app.workout_plan.model.WorkoutPlan;
import app.workout_plan.service.WorkoutPlanInit;
import app.workout_plan.service.WorkoutPlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@Transactional
class WorkoutPlanInitTest {

    @Autowired
    private WorkoutPlanInit workoutPlanInit;

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Test
    void testWorkoutPlansAreInitialized() throws Exception {

        workoutPlanInit.run();

        List<WorkoutPlan> workoutPlans = workoutPlanService.getAllSystemWorkoutPlans();

        assertEquals(3, workoutPlans.size(), "Expected 3 workout plans to be initialized.");
        assertTrue(workoutPlans.stream().anyMatch(plan -> plan.getType().equals("Beginner Plan")));
        assertTrue(workoutPlans.stream().anyMatch(plan -> plan.getType().equals("Intermediate Plan")));
        assertTrue(workoutPlans.stream().anyMatch(plan -> plan.getType().equals("Advanced Plan")));
    }

    @Test
    void testWorkoutPlansNotInitializedWhenPlansExist() throws Exception {

        workoutPlanInit.run();

        List<WorkoutPlan> workoutPlans = workoutPlanService.getAllSystemWorkoutPlans();

        assertEquals(3, workoutPlans.size());

    }
}
