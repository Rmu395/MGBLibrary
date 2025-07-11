package library.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    private String name;
    private String login;

    /*
    {
        "name": "",
        "login": ""
    }
    */
}
