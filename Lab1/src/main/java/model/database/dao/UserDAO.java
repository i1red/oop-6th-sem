package model.database.dao;

import model.entity.User;
import model.database.dao.mapper.UserMapper;
import model.database.Table;

import java.sql.Connection;


public class UserDAO extends DAO<User> {
    public UserDAO(Connection connection) {
        super(new UserMapper(), Table.User.NAME, Table.User.COLUMNS, connection);
    }
}
