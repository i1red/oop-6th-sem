package model.database.dao;

import model.database.exception.IntegrityConstraintViolation;
import model.database.exception.SQLExceptionWrapper;
import model.database.dao.mapper.Mapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DAO<T> {
    protected Logger logger;
    protected final Mapper<T> mapper;
    protected final String tableName;
    protected final List<String> columns;
    protected final List<String> primaryKeyColumns;
    protected final List<String> insertColumns;
    protected final Connection connection;

    public DAO(Mapper<T> mapper, String tableName, List<String> columns, Connection connection) {
        this(mapper, tableName, columns, columns.subList(0, 1), columns.subList(1, columns.size()), connection);
    }

    public DAO(Mapper<T> mapper, String tableName, List<String> columns, List<String> primaryKeyColumns, List<String> insertColumns, Connection connection) {
        this.tableName = tableName;
        this.mapper = mapper;
        this.columns = columns;
        this.primaryKeyColumns = primaryKeyColumns;
        this.insertColumns = insertColumns;

        this.connection = connection;

        this.logger = LogManager.getLogger(this.getClass());
    }

    public Optional<T> get(Object value, Object... values) {
        String sql = String.format(
                "SELECT * FROM %s WHERE %s",
                this.tableName,
                String.join(" AND ", this.primaryKeyColumns.stream().map(column -> String.format("%s=?", column)).toArray(String[]::new))
        );

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setObject(1, value);
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 2, values[i]);
            }

            logger.info(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    return Optional.of(mapper.fromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            SQLExceptionWrapper.wrapExceptionOnQuery(e);
        }

        return Optional.empty();
    }

    public T insert(T entity) throws IntegrityConstraintViolation {
        T insertedEntity = null;
        String sql = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                this.tableName,
                String.join(", ", this.insertColumns),
                String.join(", ", Collections.nCopies(this.insertColumns.size(),"?"))
        );

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql, this.columns.toArray(String[]::new))
        ){
            mapper.fillPreparedStatement(entity, preparedStatement, this.insertColumns);

            logger.info(preparedStatement);

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                insertedEntity =  mapper.fromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            SQLExceptionWrapper.wrapExceptionOnUpdate(e);
        }

        return insertedEntity;
    }

    public T update(String column, T entityUpdated) throws IntegrityConstraintViolation, IllegalArgumentException {
        return this.update(Collections.singletonList(column), entityUpdated, null);
    }

    public T update(String column, T entityUpdated, List<String> filterColumns) throws IntegrityConstraintViolation, IllegalArgumentException {
        return this.update(Collections.singletonList(column), entityUpdated, filterColumns);
    }

    public T update(List<String> columns, T entityUpdated, List<String> filterColumns) throws IntegrityConstraintViolation, IllegalArgumentException {
        if (filterColumns == null) {
            filterColumns = this.primaryKeyColumns;
        }

        T updatedEntity = null;
        String sql = String.format(
                "UPDATE %s SET %s WHERE %s",
                this.tableName,
                columns.stream().map(column -> String.format("%s=?", column)).collect(Collectors.joining(", ")),
                String.join(" AND ", filterColumns.stream().map(column -> String.format("%s=?", column)).toArray(String[]::new))
        );

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql, this.columns.toArray(String[]::new))
        ){
            mapper.fillPreparedStatement(entityUpdated, preparedStatement,
                    Stream.concat(columns.stream(), filterColumns.stream()).collect(Collectors.toList()));

            logger.info(preparedStatement);

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                resultSet.next();
                updatedEntity = mapper.fromResultSet(resultSet);
            } catch (SQLException e) {
                throw new IllegalArgumentException("No entity to update. Check filter columns", e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            SQLExceptionWrapper.wrapExceptionOnUpdate(e);
        }

        return updatedEntity;
    }

    public List<T> filter(String column, Object value) {
        List<T> entities = new ArrayList<>();
        String sql = String.format(
                "SELECT * FROM %s WHERE %s=?",
                this.tableName, column
        );

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setObject(1, value);

            logger.info(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    entities.add(mapper.fromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            SQLExceptionWrapper.wrapExceptionOnQuery(e);
        }

        return entities;
    }

    public List<T> filter(List<String> columns, T entityFilter) {
        List<T> entities = new ArrayList<>();
        String sql = String.format(
                "SELECT * FROM %s WHERE %s",
                this.tableName,
                columns.stream().map(column -> String.format("%s=?", column)).collect(Collectors.joining(" AND "))
        );

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            mapper.fillPreparedStatement(entityFilter, preparedStatement, columns);

            logger.info(preparedStatement);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()) {
                    entities.add(mapper.fromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            SQLExceptionWrapper.wrapExceptionOnQuery(e);
        }

        return entities;
    }

    public List<T> list() {
        List<T> entities = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", this.tableName);

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ){
            logger.info(sql);

            while (resultSet.next()) {
                entities.add(mapper.fromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            SQLExceptionWrapper.wrapExceptionOnQuery(e);
        }

        return entities;
    }
}
