package app.fittapp.workout_plan.model;

import app.fittapp.user.model.User;
import app.fittapp.workout.model.Workout;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String type; // Beginner Plan

    @Column(nullable = false)
    private String name; // Foundations

    @Column(nullable = false)
    private String targetAudience;

    @Column(nullable = false)
    private String workoutFocus;

    @Column(nullable = false)
    private int pointsNeeded;

    @OneToMany (mappedBy = "workoutPlan", fetch = FetchType.EAGER)
    @Column(nullable = false)
    List<Workout> workouts = new ArrayList<>();

    @ManyToMany(mappedBy = "workoutPlans", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();
}
