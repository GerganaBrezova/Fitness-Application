package app.post.model;

import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    private LocalDateTime postedOn;

    @ManyToOne(optional = false)
    private User author;

    @ManyToMany
    private Set<User> likedBy = new HashSet<>();
}
