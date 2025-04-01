package app.workout.model;

import app.workout_plan.model.WorkoutPlan;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int day; // 1

    @Column(nullable = false)
    private String type; // Full-body Strength

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    @ElementCollection
    private List<String> exercises;

    @ManyToOne(optional = false)
    private WorkoutPlan workoutPlan;
}