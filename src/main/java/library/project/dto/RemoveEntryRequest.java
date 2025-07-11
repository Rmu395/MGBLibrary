package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveEntryRequest {
    private String entryId;

    /*
    {
        "entryId": ""
    }
    */
}
