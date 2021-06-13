package model.database;

import model.JdbcConnectionPool;
import model.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RefreshTokenDBClient {
    public static boolean insert(String token) throws SQLException {
        String sql = String.format("INSERT INTO %s (%s) VALUES (?)",
                Table.RefreshToken.NAME, Table.RefreshToken.Column.VALUE);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, token);
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public static boolean contains(String token) throws SQLException{
        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Table.RefreshToken.NAME, Table.RefreshToken.Column.VALUE);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Table.RefreshToken.COLUMNS.toArray(String[]::new))
        ){
            preparedStatement.setString(1, token);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                return resultSet.next();
            }
        }
    }

    public static boolean delete(String token) throws SQLException{
        String sql = String.format("DELETE FROM %s WHERE %s=?",
                Table.RefreshToken.NAME, Table.RefreshToken.Column.VALUE);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, token);
            return preparedStatement.executeUpdate() == 1;
        }
    }
}
