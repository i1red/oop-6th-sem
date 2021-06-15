import model.database.JdbcConnectionPool;
import model.database.Table;
import model.database.dao.BankAccountDAO;
import model.database.dao.UserDAO;
import model.database.exception.IntegrityConstraintViolation;
import model.entity.BankAccount;
import model.entity.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestDAO {
    @Before
    @After
    public void cleanDataBase() throws SQLException{
        String sql = String.format("TRUNCATE %s, %s, %s, %s",
                Table.Payment.NAME, Table.Card.NAME, Table.BankAccount.NAME, Table.User.NAME);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                Statement statement = connection.createStatement();
        ){
            statement.executeUpdate(sql);
        }
    }

    @Test
    public void testInsertUser() throws IntegrityConstraintViolation {
        User user = new User().setUsername("test_user").setPassword("test_password").setAdmin(true);

        User insertedUser = new UserDAO().insert(user);

        assertNotNull(insertedUser.getId());
        assertEquals(user.getUsername(), insertedUser.getUsername());
        assertEquals(user.getPassword(), insertedUser.getPassword());
        assertEquals(user.isAdmin(), insertedUser.isAdmin());
    }

    @Test
    public void testGetUser() throws IntegrityConstraintViolation {
        var userDAO = new UserDAO();
        User user = userDAO.insert(new User().setUsername("test_user").setPassword("test_password"));
        User gotUser = userDAO.get(user.getId()).get();

        assertEquals(user, gotUser);
    }

    @Test(expected = IntegrityConstraintViolation.class)
    public void testForeignKeyViolation() throws IntegrityConstraintViolation {
        new BankAccountDAO().insert(new BankAccount().setNumber("number").setCustomerId(1));
    }

    @Test
    public void testFilterBankAccount() throws IntegrityConstraintViolation {
        var userDAO = new UserDAO();
        var bankAccountDAO = new BankAccountDAO();
        User firstUser = userDAO.insert(new User().setUsername("user1").setPassword("password"));
        User secondUser = userDAO.insert(new User().setUsername("user2").setPassword("password"));

        BankAccount firstBankAccount = bankAccountDAO.insert(new BankAccount().setCustomerId(firstUser.getId()).setNumber("number1"));
        BankAccount secondBankAccount = bankAccountDAO.insert(new BankAccount().setCustomerId(secondUser.getId()).setNumber("number2"));

        List<BankAccount> firstUserAccounts = bankAccountDAO.filter(Table.BankAccount.Column.USER_ID, firstUser.getId());
        List<BankAccount> secondUserAccounts = bankAccountDAO.filter(
                Arrays.asList(Table.BankAccount.Column.USER_ID, Table.BankAccount.Column.IS_BLOCKED),
                secondBankAccount
        );

        assertEquals(1, firstUserAccounts.size());
        assertEquals(firstBankAccount, firstUserAccounts.get(0));

        assertEquals(1, secondUserAccounts.size());
        assertEquals(secondBankAccount, secondUserAccounts.get(0));

    }
}
