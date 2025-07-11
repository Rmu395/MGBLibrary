package library.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameEntry {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "hours")
    private int hours;

    @Column(name = "rating")
    private String rating;  // something like "1/20" or "6/9" if they want, or even "good"

    @Column(name = "review")
    private String review;

    @Column(name = "visible")
    private boolean visible;
}
