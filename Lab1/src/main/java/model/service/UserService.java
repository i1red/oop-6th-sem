package model.service;

import model.Table;
import model.database.dao.UserDAO;
import model.database.dao.exception.IntegrityConstraintViolation;
import model.entity.User;
import model.database.dao.DAO;
import model.service.util.PasswordService;
import model.service.util.exception.PasswordTransformError;

import java.util.Arrays;
import java.util.List;

public class UserService {
    private final DAO<User> userDAO = new UserDAO();

    public User register(User user) throws IllegalArgumentException {
        User userWithEncryptedPassword = user.withPassword(PasswordService.encryptPassword(user.getPassword()));

        User insertedUser;
        try {
            insertedUser = userDAO.insert(userWithEncryptedPassword);
        } catch (IntegrityConstraintViolation e) {
            throw new IllegalArgumentException("Cannot create user with such username", e);
        }

        return user.withId(insertedUser.getId());
    }

    public User login(User user) throws IllegalArgumentException {
        User userWithEncryptedPassword = user.withPassword(PasswordService.encryptPassword(user.getPassword()));

        List<User> filteredUsers = userDAO.filter(
                Arrays.asList(Table.User.Column.USERNAME, Table.User.Column.PASSWORD),
                userWithEncryptedPassword
        );

        if (filteredUsers.size() == 0) {
            throw new IllegalArgumentException("Incorrect username or password");
        }

        return user.withId(filteredUsers.get(0).getId());
    }
}
