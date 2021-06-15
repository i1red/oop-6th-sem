package model.database.dao;

import model.entity.User;
import model.database.dao.mapper.UserMapper;
import model.database.Table;


public class UserDAO extends DAO<User> {
    public UserDAO() {
        super(new UserMapper(), Table.User.NAME, Table.User.COLUMNS);
    }
}
