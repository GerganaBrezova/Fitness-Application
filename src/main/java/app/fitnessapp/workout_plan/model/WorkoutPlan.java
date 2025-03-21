package app.fitnessapp.workout_plan.model;

import app.fitnessapp.user.model.User;
import app.fitnessapp.workout.model.Workout;
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
    private String pointsNeeded;

    @OneToMany (mappedBy = "workoutPlan", fetch = FetchType.EAGER)
    List<Workout> workouts = new ArrayList<>();

    @ManyToOne
    private User user;
}
