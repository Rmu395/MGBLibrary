package library.project.repository;

import library.project.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform, String> {
    Optional<Platform> findByName(String name);
}
