package library.project.service.impl;

import library.project.model.Game;
import library.project.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO: WELL TO DO EVERYTHING
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    @Override
    public List<Game> findAll() {
        return List.of();
    }

    @Override
    public List<Game> findAllVisible() {
        return List.of();
    }

    @Override
    public Optional<Game> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Game save(Game game) {
        return null;
    }

    @Override
    public Optional<Game> deleteById(String id) {
        return Optional.empty();
    }
}
