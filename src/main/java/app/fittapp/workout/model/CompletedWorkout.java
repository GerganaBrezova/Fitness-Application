package app.fittapp.workout.model;

import app.fittapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class CompletedWorkout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int day; // 1

    @Column(nullable = false)
    private String type; // BeginnerPlan

    @Column(nullable = false)
    private LocalDateTime completedOn;

    @ManyToOne
    private User user;
}
