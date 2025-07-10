package library.project.service;

import library.project.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    List<Book> findAllVisible();
    Optional<Book> findById(String id);
    Book save(Book book);
    Optional<Book> deleteById(String id);
}
