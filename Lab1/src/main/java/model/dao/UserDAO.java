package model.dao;

import model.entity.User;
import model.mapper.UserMapper;
import model.table.UserTable;

public class UserDAO extends DAO<User> {
    public UserDAO() {
        super(new UserMapper(), UserTable.NAME, UserTable.COLUMNS);
    }
}
