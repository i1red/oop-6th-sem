package model.dao;

import model.JdbcConnectionPool;
import model.mapper.Mapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DAO<T> {
    protected final Mapper<T> mapper;
    protected final String tableName;
    protected final List<String> columns;
    protected final List<String> insertColumns;

    public DAO(Mapper<T> mapper, String tableName, List<String> columns) {
        this(mapper, tableName, columns, columns.subList(1, columns.size()));
    }

    public DAO(Mapper<T> mapper, String tableName, List<String> columns, List<String> insertColumns) {
        this.tableName = tableName;
        this.mapper = mapper;
        this.columns = columns;
        this.insertColumns = insertColumns;
    }

    public T get(String column, Object value) {
        String sql = String.format("SELECT * FROM %s WHERE %s=?", this.tableName, column);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setObject(1, value);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    return mapper.fromResultSet(resultSet);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public List<T> list() {
        List<T> entities = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE", this.tableName);

        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ){
            while (resultSet.next()) {
                entities.add(mapper.fromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return entities;
    }

    public T insert(T entity) {
        String sql = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                this.tableName,
                String.join(", ", this.insertColumns),
                String.join(", ", Collections.nCopies(this.insertColumns.size(),"?"))
        );

        System.out.println(sql);
        try (
                Connection connection = JdbcConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, this.columns.toArray(String[]::new))
        ){
            this.mapper.fillPreparedStatement(entity, preparedStatement, this.insertColumns);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                if (resultSet.next()) {
                    return mapper.fromResultSet(resultSet);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
