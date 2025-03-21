package app.fitnessapp.user.model;

import app.fitnessapp.meal.model.Meal;
import app.fitnessapp.post.model.Post;
import app.fitnessapp.workout.model.CompletedWorkout;
import app.fitnessapp.workout_plan.model.WorkoutPlan;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    private String name;

    private String profilePictureUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private int points;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Column(nullable = false)
    private BigDecimal height;

    @Column(nullable = false)
    private BigDecimal weight;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGoal goal;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserActivityLevel activityLevel;

    private double dailyIntake;

    @OneToMany(mappedBy = "user")
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<CompletedWorkout> completedWorkouts = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Meal> meals = new ArrayList<>();

}