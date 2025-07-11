package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformRequest {
    private String gameId;
    private String platformName;

    /*
    {
        "gameId": "",
        "platformName": ""
    }
    */
}
