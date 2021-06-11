import model.dao.DAO;
import model.dao.UserDAO;
import model.entity.User;
import model.table.UserTable;

import java.sql.SQLException;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws InterruptedException, SQLException {
        System.out.println(System.getenv("CONNECTION_POOL_SIZE"));
        DAO<User> userDAO = new UserDAO();

        System.out.println(userDAO.filter(
                Arrays.asList(UserTable.Column.IS_ADMIN, UserTable.Column.PASSWORD),
                new User().setAdmin(false).setPassword("abc")
        ));
    }
}
