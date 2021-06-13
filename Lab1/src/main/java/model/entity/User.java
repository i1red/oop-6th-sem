package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    Integer id;
    String username;
    String password;
    boolean isAdmin = false;
}
