import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.UserDAO;
import model.entity.BankAccount;
import model.entity.User;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            new BankAccountDAO().update(Table.BankAccount.Column.IS_BLOCKED, new BankAccount().setId(100).setBlocked(true));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
