import model.dao.DAO;
import model.dao.UserDAO;
import model.entity.User;

public class App {
    public static void main(String[] args) throws InterruptedException {
        DAO<User> userDAO = new UserDAO();

        System.out.println(userDAO.insert(new User().setPassword("abc").setUsername("bga1")));
    }
}
