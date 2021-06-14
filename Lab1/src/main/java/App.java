import io.jsonwebtoken.Claims;
import model.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.UserDAO;
import model.entity.BankAccount;
import model.entity.User;
import model.service.UserService;
import model.service.util.TokenService;
import model.service.util.UserClaims;

import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            new UserDAO().insert(new User().setUsername("i1red").setPassword("kek"));
        } catch (Exception e) {}
    }
}
