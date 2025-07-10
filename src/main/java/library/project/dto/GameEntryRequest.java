package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntryRequest {
    private String gameId;  // based on this, find game to link to and from auth - user
    private int hours;
    private String rating;
    private String review;
}
