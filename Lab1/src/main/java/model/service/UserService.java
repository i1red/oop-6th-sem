package model.service;

import model.database.dao.UserDAO;
import model.entity.User;
import model.database.dao.DAO;

public class UserService {
    DAO<User> userDAO = new UserDAO();


}
