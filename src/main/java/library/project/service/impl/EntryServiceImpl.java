package library.project.service.impl;

import library.project.model.BookEntry;
import library.project.model.GameEntry;
import library.project.repository.BookEntryRepository;
import library.project.repository.GameEntryRepository;
import library.project.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final BookEntryRepository bookEntryRepository;
    private final GameEntryRepository gameEntryRepository;

    @Override
    public List<BookEntry> findAllBE() {
        return bookEntryRepository.findAll();
    }

    @Override
    public BookEntry saveBE(BookEntry bookEntry) {
        return bookEntryRepository.save(bookEntry);
    }

    @Override
    public List<BookEntry> findAllBookEntryByUserId(String id) {
        return bookEntryRepository.findByUserIdAndVisibleTrue(id);
    }

    @Override
    public Optional<BookEntry> findByBookEntryId(String id) {
        return bookEntryRepository.findById(id);
    }

    @Override
    public List<GameEntry> findAllGE() {
        return gameEntryRepository.findAll();
    }

    @Override
    public GameEntry saveGE(GameEntry gameEntry) {
        return gameEntryRepository.save(gameEntry);
    }

    @Override
    public List<GameEntry> findAllGameEntryByUserId(String id) {
        return gameEntryRepository.findByUserIdAndVisibleTrue(id);
    }

    @Override
    public Optional<GameEntry> findByGameEntryId(String id) {
        return gameEntryRepository.findById(id);
    }
}
