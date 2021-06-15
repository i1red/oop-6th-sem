package model.service;

import model.database.JdbcConnectionPool;
import model.database.Table;
import model.database.dao.UserDAO;
import model.database.exception.IntegrityConstraintViolation;
import model.database.exception.SQLError;
import model.entity.User;
import model.database.dao.DAO;
import model.service.util.PasswordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserService {
    public User register(User user) throws IllegalArgumentException {
        User userWithEncryptedPassword = user.withPassword(PasswordUtil.encryptPassword(user.getPassword()));

        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            return user.withId(new UserDAO(connection).insert(userWithEncryptedPassword).getId());
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Cannot create user with such username", e);
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }

    public User login(User user) throws IllegalArgumentException {
        User userWithEncryptedPassword = user.withPassword(PasswordUtil.encryptPassword(user.getPassword()));

        try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
            List<User> filteredUsers = new UserDAO(connection).filter(
                    Arrays.asList(Table.User.Column.USERNAME, Table.User.Column.PASSWORD),
                    userWithEncryptedPassword
            );

            if (filteredUsers.size() == 0) {
                throw new IllegalArgumentException("Incorrect username or password");
            }

            return user.withId(filteredUsers.get(0).getId());
        } catch (SQLException e) {
            throw new SQLError(e.getMessage());
        }
    }
}
