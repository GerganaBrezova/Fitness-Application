package app.fittapp.meal.model;

import app.fittapp.user.model.User;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToOne(optional = false)
    private User user;
}
