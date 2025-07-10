package library.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users;
}
