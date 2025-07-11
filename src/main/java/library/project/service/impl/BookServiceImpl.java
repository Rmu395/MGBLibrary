package library.project.service.impl;

import library.project.model.Book;
import library.project.repository.BookRepository;
import library.project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findAllVisible() {
        return bookRepository.findByVisibleTrue();
    }

    @Override
    public List<Book> findAllInvisible() {
        return bookRepository.findByVisibleFalse();
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> deleteById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            book.get().setVisible(false);
            return book;
        }
        else return Optional.empty();
    }
}
