package app.fittapp.workout.repository;

import app.fittapp.user.model.User;
import app.fittapp.workout.model.CompletedWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompletedWorkoutRepository extends JpaRepository<CompletedWorkout, UUID> {

    List<CompletedWorkout> findByUserOrderByCompletedOnDesc(User user);
}
