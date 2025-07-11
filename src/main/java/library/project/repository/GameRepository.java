package library.project.repository;

import library.project.model.Book;
import library.project.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, String> {
    // Methods findAll(), findById(), save(), deleteById() fromJpaRepository.
    List<Game> findByVisibleTrue();
    List<Game> findByVisibleFalse();
}
