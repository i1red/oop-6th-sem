package model.service.util;

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
public class UserClaims {
    int id;
    boolean isAdmin;
}
