package library.project.service;

import library.project.model.BookEntry;
import library.project.model.GameEntry;

import java.util.List;
import java.util.Optional;

public interface EntryService {
    // Books
    List<BookEntry> findAllBE();
    BookEntry saveBE(BookEntry bookEntry);
    List<BookEntry> findAllBookEntryByUserId(String id);
    Optional<BookEntry> findByBookEntryId(String id);

    // Games
    List<GameEntry> findAllGE();
    GameEntry saveGE(GameEntry gameEntry);
    List<GameEntry> findAllGameEntryByUserId(String id);
    Optional<GameEntry> findByGameEntryId(String id);
}
