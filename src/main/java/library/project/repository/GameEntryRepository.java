package library.project.repository;

import library.project.model.GameEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameEntryRepository extends JpaRepository<GameEntry, String> {
    // Methods findAll(), findById(), save(), deleteById() fromJpaRepository.
    Optional<GameEntry> findByGameId(String gameId);    //TODO: check if visible in use case
    List<GameEntry> findByUserId(String userId);
    List<GameEntry> findByUserIdAndVisibleTrue(String userId);
}
