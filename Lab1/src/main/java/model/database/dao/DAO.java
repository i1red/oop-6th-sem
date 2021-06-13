package model.database.dao;

import lombok.SneakyThrows;
import model.JdbcConnectionPool;
import model.Table;
import model.mapper.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DAO<T> {
    protected final Mapper<T> mapper;
    protected final String tableName;
    protected final List<String> columns;
    protected final List<String> primaryKeyColumns;
    protected final List<String> insertColumns;

    public DAO(Mapper<T> mapper, String tableName, List<String> columns) {
        this(mapper, tableName, columns, columns.subList(0, 1), columns.subList(1, columns.size()));
    }

    public DAO(Mapper<T> mapper, String tableName, List<String> columns, List<String> primaryKeyColumns, List<String> insertColumns) {
        this.tableName = tableName;
        this.mapper = mapper;
        this.columns = columns;
        this.primaryKeyColumns = primaryKeyColumns;
        this.insertColumns = insertColumns;
    }

    public T get(Object value, Object... values) throws SQLException{
        String sql = String.format(
                "SELECT * FROM %s WHERE %s",
                this.tableName,
                String.join(" AND ", this.primaryKeyColumns.stream().map(column -> String.format("%s=?", column)).toArray(String[]::new))
        );

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setObject(1, value);
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 2, values[i]);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    return mapper.fromResultSet(resultSet);
                }
            }
        }

        return null;
    }

    public T insert(T entity) throws SQLException {
        String sql = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                this.tableName,
                String.join(", ", this.insertColumns),
                String.join(", ", Collections.nCopies(this.insertColumns.size(),"?"))
        );

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, this.columns.toArray(String[]::new))
        ){
            this.mapper.fillPreparedStatement(entity, preparedStatement, this.insertColumns);
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                return mapper.fromResultSet(resultSet);
            }
        }
    }

    public T update(String column, T entityUpdated) throws SQLException {
        return this.update(Collections.singletonList(column), entityUpdated);
    }

    public T update(List<String> columns, T entityUpdated) throws SQLException {
        String sql = String.format(
                "UPDATE %s SET %s WHERE %s",
                this.tableName,
                columns.stream().map(column -> String.format("%s=?", column)).collect(Collectors.joining(", ")),
                String.join(" AND ", this.primaryKeyColumns.stream().map(column -> String.format("%s=?", column)).toArray(String[]::new))
        );

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, this.columns.toArray(String[]::new))
        ){
            this.mapper.fillPreparedStatement(entityUpdated, preparedStatement,
                    Stream.concat(columns.stream(), this.primaryKeyColumns.stream()).collect(Collectors.toList()));
            preparedStatement.executeUpdate();

            System.out.println(preparedStatement);

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                return mapper.fromResultSet(resultSet);
            }
        }
    }

    public List<T> filter(String column, Object value) throws SQLException {
        List<T> entities = new ArrayList<>();
        String sql = String.format(
                "SELECT * FROM %s WHERE %s=?",
                this.tableName, column
        );

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setObject(1, value);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    entities.add(mapper.fromResultSet(resultSet));
                }
            }
        }

        return entities;
    }

    public List<T> filter(List<String> columns, T entityFilter) throws SQLException {
        List<T> entities = new ArrayList<>();
        String sql = String.format(
                "SELECT * FROM %s WHERE %s",
                this.tableName,
                columns.stream().map(column -> String.format("%s=?", column)).collect(Collectors.joining(" AND "))
        );

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            mapper.fillPreparedStatement(entityFilter, preparedStatement, columns);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    entities.add(mapper.fromResultSet(resultSet));
                }
            }
        }

        return entities;
    }

    public List<T> list() throws SQLException {
        List<T> entities = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", this.tableName);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ){
            while (resultSet.next()) {
                entities.add(mapper.fromResultSet(resultSet));
            }
        }

        return entities;
    }
}
