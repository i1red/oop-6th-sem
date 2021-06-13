package model.service;

import model.Table;
import model.database.dao.UserDAO;
import model.entity.User;
import model.database.dao.DAO;
import model.service.util.PasswordService;
import model.service.util.exception.PasswordTransformException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserService {
    private final DAO<User> userDAO = new UserDAO();

    public User register(User user) throws ServerError, IllegalArgumentException {
        User userWithEncryptedPassword;
        try {
            userWithEncryptedPassword = user.withPassword(PasswordService.encryptPassword(user.getPassword()));
        } catch (PasswordTransformException e) {
            throw new ServerError("Failed to encrypt password", e);
        }

        User insertedUser;
        try {
            insertedUser = userDAO.insert(userWithEncryptedPassword);
        } catch (SQLException e) {
            throw new IllegalArgumentException("Cannot create user with such username", e);
        }

        return user.withId(insertedUser.getId());
    }

    public User login(User user) throws ServerError, IllegalArgumentException {
        User userWithEncryptedPassword;

        try {
            userWithEncryptedPassword = user.withPassword(PasswordService.encryptPassword(user.getPassword()));
        } catch (PasswordTransformException e) {
            throw new ServerError("Failed to encrypt password", e);
        }

        List<User> filteredUsers;
        try {
            filteredUsers = userDAO.filter(
                    Arrays.asList(Table.User.Column.USERNAME, Table.User.Column.PASSWORD),
                    userWithEncryptedPassword
            );
        } catch (SQLException e) {
            throw new ServerError("Failed database query", e);
        }

        if (filteredUsers.size() == 0) {
            throw new IllegalArgumentException("Incorrect username or password");
        }

        return user.withId(filteredUsers.get(0).getId());
    }
}
