import io.jsonwebtoken.Claims;
import model.Table;
import model.database.dao.UserDAO;
import model.entity.User;
import model.service.UserService;
import model.service.util.TokenService;
import model.service.util.UserClaims;

import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {
        var userDAO = new UserDAO();
        var user = new User().setUsername("abc").setPassword("abc");
        user = userDAO.insert(user);
        user.setAdmin(true);
        System.out.println(userDAO.update(Table.User.Column.IS_ADMIN, user));
    }
}
