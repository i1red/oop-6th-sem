import model.entity.User;
import model.service.sign.PasswordTransformException;
import model.service.sign.TokenService;
import model.service.sign.TokenValidationException;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws TokenValidationException {

        System.out.println(TokenService.parseAccessToken(TokenService.createAccessToken(new User().setId(1))).getId());
    }
}
