package library.project.service.impl;

import library.project.model.Game;
import library.project.repository.GameRepository;
import library.project.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    @Override
    public List<Game> findAllVisible() {
        return gameRepository.findByVisibleTrue();
    }

    @Override
    public List<Game> findAllInvisible() {
        return gameRepository.findByVisibleFalse();
    }

    @Override
    public Optional<Game> findById(String id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Optional<Game> deleteById(String id) {
        Optional<Game> game = gameRepository.findById(id);
        if (game.isPresent()) {
            game.get().setVisible(false);
            return game;
        }
        else return Optional.empty();
    }
}
