package app.fittapp.user.model;

import app.fittapp.meal.model.Meal;
import app.fittapp.post.model.Post;
import app.fittapp.workout.model.CompletedWorkout;
import app.fittapp.workout_plan.model.WorkoutPlan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
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

    @Column(nullable = false)
    private LocalDateTime createdOn;

    private String name;

    private String profilePictureUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean isActive;

    @Column(nullable = false)
    private int points;

    @Column
    private int age;

    @Column
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Column
    private double height;

    @Column
    private double weight;

    @Column
    @Enumerated(EnumType.STRING)
    private UserGoal goal;

    @Column
    @Enumerated(EnumType.STRING)
    private UserActivityLevel activityLevel;

    private double dailyIntake;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<CompletedWorkout> completedWorkouts = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Meal> meals = new ArrayList<>();

}