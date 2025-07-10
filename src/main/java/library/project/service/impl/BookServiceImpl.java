package library.project.service.impl;

import library.project.model.Book;
import library.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//TODO: WELL TO DO EVERYTHING
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    @Override
    public List<Book> findAll() {
        return List.of();
    }

    @Override
    public List<Book> findAllVisible() {
        return List.of();
    }

    @Override
    public Optional<Book> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public Optional<Book> deleteById(String id) {
        return Optional.empty();
    }
}
