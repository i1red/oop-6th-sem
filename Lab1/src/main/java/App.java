import model.Table;
import model.dao.DAO;
import model.dao.UserDAO;
import model.entity.User;

import java.sql.SQLException;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws InterruptedException, SQLException {
        DAO<User> userDAO = new UserDAO();

        System.out.println(userDAO.filter(
                Arrays.asList(Table.User.Column.IS_ADMIN, Table.User.Column.PASSWORD),
                new User().setAdmin(false).setPassword("abc")
        ));
    }
}
