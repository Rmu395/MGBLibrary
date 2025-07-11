package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBookRequest {
    private String id;  // this will only be used for editing already existing book or deleting one
    private String title;
    private String author;
    private String pubYear;
    private String publisher;
    private int pages;

    /*
    {
        "id": "",
        "title": "",
        "author": "",
        "pubYear": "",
        "publisher": "",
        "pages":
    }
    */
}
