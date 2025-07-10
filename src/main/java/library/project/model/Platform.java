package library.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "platforms")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Platform {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "platforms")
    @JsonIgnore
    private Set<Game> games;
}
