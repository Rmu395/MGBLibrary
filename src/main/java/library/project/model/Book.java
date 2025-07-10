package library.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "pub_year", nullable = false)
    private String pubYear;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "pages", nullable = false)
    private int pages;

    @Column(name = "visible", nullable = false)
    private boolean visible;
}
