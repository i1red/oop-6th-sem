package model.mapper;

import java.util.List;

import model.entity.User;
import model.table.UserTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements Mapper<User> {

    @Override
    public User fromResultSet(ResultSet resultSet) throws SQLException {
        return new User()
                .setId(resultSet.getInt(UserTable.Column.ID))
                .setUsername(resultSet.getString(UserTable.Column.USERNAME))
                .setPassword(resultSet.getString(UserTable.Column.PASSWORD))
                .setAdmin(resultSet.getBoolean(UserTable.Column.IS_ADMIN));
    }

    @Override
    public void fillPreparedStatement(User user, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException {
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            switch (columnNames.get(columnIndex)) {
                case UserTable.Column.ID -> preparedStatement.setInt(columnIndex + 1, user.getId());
                case UserTable.Column.USERNAME -> preparedStatement.setString(columnIndex + 1, user.getUsername());
                case UserTable.Column.PASSWORD -> preparedStatement.setString(columnIndex + 1, user.getPassword());
                case UserTable.Column.IS_ADMIN -> preparedStatement.setBoolean(columnIndex + 1, user.isAdmin());
                default -> throw new IllegalArgumentException(String.format("Column %s does not exist", columnNames.get(columnIndex)));
            }
        }
    }
}
