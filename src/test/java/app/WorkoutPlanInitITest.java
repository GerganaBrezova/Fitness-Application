//package app;
//
//import app.workout_plan.repository.WorkoutPlanRepository;
//import app.workout_plan.service.WorkoutPlanInit;
//import app.workout_plan.service.WorkoutPlanService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@SpringBootTest
//class WorkoutPlanInitITest {
//
//    @Autowired
//    private WorkoutPlanInit workoutPlanInit;
//
//    @Autowired
//    private WorkoutPlanRepository workoutPlanRepository;
//
//    @Test
//    void testRun_createsWorkoutPlansInDatabase() throws Exception {
//        workoutPlanInit.run();
//
//        assertFalse(workoutPlanRepository.findAll().isEmpty());
//
//        assertEquals(3, workoutPlanRepository.findAll().size());
//
//        assertTrue(workoutPlanRepository.findAll().stream()
//                .anyMatch(plan -> plan.getType().equals("Beginner Plan")));
//        assertTrue(workoutPlanRepository.findAll().stream()
//                .anyMatch(plan -> plan.getType().equals("Intermediate Plan")));
//        assertTrue(workoutPlanRepository.findAll().stream()
//                .anyMatch(plan -> plan.getType().equals("Advanced Plan")));
//    }
//}