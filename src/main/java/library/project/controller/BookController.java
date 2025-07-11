package library.project.controller;

import library.project.dto.*;
import library.project.model.*;
import library.project.repository.BookRepository;
import library.project.repository.RoleRepository;
import library.project.service.BookService;
import library.project.service.EntryService;
import library.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("project/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final EntryService entryService;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<String> info(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("""
                    As user you can:
                        > /all              (GET)
                        > /myBookList       (GET)
                        > /addBookEntry     (POST + BookEntryRequest without Id in body)
                        > /removeBookEntry  (POST + Only Id from BookEntryRequest in body)
                        > /newBookRequest   (POST + NewBookRequest without Id in body)
                    As admin you can:
                        > /admin/all                    (GET)
                        > /admin/allEntries             (GET)
                        > /admin/addNewBook             (POST + NewBookRequest without Id in body)
                        > /admin/requests               (GET)
                        > /admin/approveRequest/{id}    (POST)
                        > /admin/editBook               (POST + NewBookRequest)
                        > /admin/deleteBook             (DELETE + Only Id from NewBookRequest in body)
                    """);
    }

    @GetMapping("/all")
    public List<Book> getAllVisibleBooks(@AuthenticationPrincipal UserDetails userDetails) {
        return bookService.findAllVisible();
    }

    @GetMapping("/myBookList")
    public List<BookEntry> getAllBookEntriesForUser(@AuthenticationPrincipal UserDetails userDetails) {
        return entryService.findAllBookEntryByUserId(userService.findByLogin(userDetails.getUsername()).get().getId());
    }

    @PostMapping("/addBookEntry")
    public ResponseEntity<BookEntry> addNewBookEntry(
            @RequestBody BookEntryRequest bookEntryRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            BookEntry bookEntry = BookEntry.builder()
                    .id(UUID.randomUUID().toString())
                    .book(bookService.findById(bookEntryRequest.getBookId()).orElseThrow())
                    .user(userService.findByLogin(userDetails.getUsername()).orElseThrow())
                    .progress(bookEntryRequest.getProgress())
                    .rating(bookEntryRequest.getRating())
                    .review(bookEntryRequest.getReview())
                    .visible(true)
                    .build();
            entryService.saveBE(bookEntry);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/removeBookEntry")
    public ResponseEntity<BookEntry> removeBookEntry(
            @RequestBody RemoveEntryRequest removeEntryRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<BookEntry> bookEntry = entryService.findByBookEntryId(removeEntryRequest.getEntryId());
        // check if the entry exists
        if (bookEntry.isEmpty()) return ResponseEntity.notFound().build();
        // check if the entry is his
        if (bookEntry.get().getUser().getLogin().equals(userDetails.getUsername())) {
            bookEntry.get().setVisible(false);
            entryService.saveBE(bookEntry.get());
            return ResponseEntity.ok(bookEntry.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/newBookRequest")
    public ResponseEntity<Book> newBookRequest(
            @RequestBody NewBookRequest newBookRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Book book = Book.builder()
                    .id(UUID.randomUUID().toString())
                    .title(newBookRequest.getTitle())
                    .author(newBookRequest.getAuthor())
                    .pubYear(newBookRequest.getPubYear())
                    .publisher(newBookRequest.getPublisher())
                    .pages(newBookRequest.getPages())
                    .visible(false)
                    .build();
            bookService.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //////////////// Admin actions /////////////////

    @GetMapping("/admin/all")
    public List<Book> getAllBooks(@AuthenticationPrincipal UserDetails userDetails) {
        return bookService.findAll();
    }

    @GetMapping("/admin/allEntries")
    public List<BookEntry> getAllBookEntries(@AuthenticationPrincipal UserDetails userDetails) {
        return entryService.findAllBE();
    }

    // add a new book
    @PostMapping("/admin/addNewBook")
    public ResponseEntity<Book> addNewBook(
            @RequestBody NewBookRequest newBookRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Book book = Book.builder()
                    .id(UUID.randomUUID().toString())
                    .title(newBookRequest.getTitle())
                    .author(newBookRequest.getAuthor())
                    .pubYear(newBookRequest.getPubYear())
                    .publisher(newBookRequest.getPublisher())
                    .pages(newBookRequest.getPages())
                    .visible(true)
                    .build();
            bookService.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // see requests for books / removed books
    @GetMapping("/admin/requests")
    public List<Book> getAllBookRequests(@AuthenticationPrincipal UserDetails userDetails) {
        return bookService.findAllInvisible();
    }

    @PostMapping("/admin/approveRequest/{id}")
    public ResponseEntity<Book> getAllBooksWithNewOne(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            book.get().setVisible(true);
            bookService.save(book.get());
            return ResponseEntity.ok(book.orElseThrow());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // edit already existing books
    @PostMapping("/admin/editBook")
    public ResponseEntity<Book> editBook(
            @RequestBody NewBookRequest newBookRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Optional<Book> book =  bookService.findById(newBookRequest.getId());
            if (book.isPresent()) {
                book.get().setTitle(newBookRequest.getTitle());
                book.get().setAuthor(newBookRequest.getAuthor());
                book.get().setPubYear(newBookRequest.getPubYear());
                book.get().setPublisher(newBookRequest.getPublisher());
                book.get().setPages(newBookRequest.getPages());
                bookService.save(book.orElseThrow());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(book.orElseThrow());
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @PostMapping("/admin/deleteBook")
    public ResponseEntity<Book> deleteBook(
            @RequestBody NewBookRequest newBookRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            Optional<Book> removedBook = bookService.deleteById(newBookRequest.getId());
            return removedBook.map(book -> ResponseEntity.status(HttpStatus.ACCEPTED).body(book)).orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
