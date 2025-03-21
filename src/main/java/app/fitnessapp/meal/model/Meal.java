package app.fitnessapp.meal.model;

import app.fitnessapp.user.model.User;
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
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private String photoUrl;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int calories;

    @ManyToOne
    private User user;
}
