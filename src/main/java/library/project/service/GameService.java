package library.project.service;

import library.project.model.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {
    List<Game> findAll();
    List<Game> findAllVisible();
    List<Game> findAllInvisible();
    Optional<Game> findById(String id);
    Game save(Game game);
    Optional<Game> deleteById(String id);
}
