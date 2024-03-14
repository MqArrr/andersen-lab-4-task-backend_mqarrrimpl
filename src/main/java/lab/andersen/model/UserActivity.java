package lab.andersen.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity implements Serializable {

    private int id;
    private int userId;
    private String description;
    private Timestamp dateTime;
    private String userName;

    public UserActivity(int id, int userId, String description, Timestamp dateTime) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.dateTime = dateTime;
    }
}