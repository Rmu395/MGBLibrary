package library.project.repository;

import library.project.model.BookEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookEntryRepository extends JpaRepository<BookEntry, String> {
    // Methods findAll(), findById(), save(), deleteById() fromJpaRepository.
    Optional<BookEntry> findByBookId(String bookId);    //TODO: check if visible in use case
    List<BookEntry> findByUserId(String userId);
    List<BookEntry> findByUserIdAndVisibleTrue(String userId);
}
