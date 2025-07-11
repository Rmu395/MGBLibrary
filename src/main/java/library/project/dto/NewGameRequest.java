package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewGameRequest {
    private String id;
    private String title;
    private String developer;
    private String publisher;

    /*
    {
        "id": "",
        "title": "",
        "developer": "",
        "publisher": ""
    }
    */
}
