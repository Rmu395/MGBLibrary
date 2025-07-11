package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntryRequest {
    private String bookId;  // based on this, find book to link to and from auth - user
    private int progress;
    private String rating;
    private String review;

    /*
    {
        "bookId": "",
        "progress": ,
        "rating": "",
        "review": ""
    }
    */
}
