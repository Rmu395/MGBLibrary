package library.project.service;

import library.project.model.BookEntry;
import library.project.model.GameEntry;

import java.util.List;

public interface EntryService {
    // Books
    List<BookEntry> findAllBookEntryByUserId(String id);

    // Games
    List<GameEntry> findAllGameEntryByUserId(String id);
}
