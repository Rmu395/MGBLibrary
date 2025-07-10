package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBookRequest {
    // ONLY ADMIN
    private String title;
    private String author;
    private String pubYear;
    private String publisher;
    private String pages;
}
