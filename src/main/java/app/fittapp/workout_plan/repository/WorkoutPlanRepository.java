package app.fittapp.workout_plan.repository;

import app.fittapp.workout_plan.model.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, UUID> {

    WorkoutPlan findByType(String title);
}
