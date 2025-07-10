package library.project.repository;

import library.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    // Methods findAll(), findById(), save(), deleteById() fromJpaRepository.
    List<Book> findByVisibleTrue();
}
