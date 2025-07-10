package library.project.service.impl;

import library.project.model.BookEntry;
import library.project.model.GameEntry;
import library.project.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//TODO: WELL TO DO EVERYTHING
@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    @Override
    public List<BookEntry> findAllBookEntryByUserId(String id) {
        return List.of();
    }

    @Override
    public List<GameEntry> findAllGameEntryByUserId(String id) {
        return List.of();
    }
}
