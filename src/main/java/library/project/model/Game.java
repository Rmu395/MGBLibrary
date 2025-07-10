package library.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "developer", nullable = false)
    private String developer;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "visible", nullable = false)
    private boolean visible;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "platforms",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "platform_id")
    )
    private Set<Platform> platforms;
}
