package model.mapper;

import java.util.List;

import model.entity.User;
import model.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements Mapper<User> {

    @Override
    public User fromResultSet(ResultSet resultSet) throws SQLException {
        return new User()
                .setId(resultSet.getInt(Table.User.Column.ID))
                .setUsername(resultSet.getString(Table.User.Column.USERNAME))
                .setPassword(resultSet.getString(Table.User.Column.PASSWORD))
                .setAdmin(resultSet.getBoolean(Table.User.Column.IS_ADMIN));
    }

    @Override
    public void fillPreparedStatement(User user, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case Table.User.Column.ID -> preparedStatement.setInt(columnIndex + 1, user.getId());
                case Table.User.Column.USERNAME -> preparedStatement.setString(columnIndex + 1, user.getUsername());
                case Table.User.Column.PASSWORD -> preparedStatement.setString(columnIndex + 1, user.getPassword());
                case Table.User.Column.IS_ADMIN -> preparedStatement.setBoolean(columnIndex + 1, user.isAdmin());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
