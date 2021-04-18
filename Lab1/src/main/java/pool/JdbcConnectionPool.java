package pool;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;


public class JdbcConnectionPool {
    private static JdbcConnectionPool instance = null;

    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/test_jdbc";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private final int size;
    private final DataSource dataSource;
    private final LinkedList<Connection> freeConnections;

    public static JdbcConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new JdbcConnectionPool(8);
        }

        return instance;
    }

    private JdbcConnectionPool(int size) throws SQLException {
        this.dataSource = this.getDataSource();
        this.size = size;

        this.freeConnections = new LinkedList<>();
        for (int i = 0; i < this.getSize(); ++i) {
            this.freeConnections.add(this.createConnection());
        }

    }

    private DataSource getDataSource() {
        var pgDataSource = new PGSimpleDataSource();

        pgDataSource.setUrl(CONNECTION_URL);
        pgDataSource.setUser(USER);
        pgDataSource.setPassword(PASSWORD);

        return pgDataSource;
    }

    private Connection createConnection() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("close")) {
                        if (connection.isClosed()) {
                            throw new IllegalStateException("Connection has already been closed");
                        }

                        synchronized (this) {
                            if (this.freeConnections.size() < this.getSize()) {
                                this.freeConnections.add((Connection) proxy);
                            }
                            else {
                                connection.close();
                            }
                        }
                        return null;
                    }

                    return method.invoke(connection, args);
                }
        );
    }

    public synchronized Connection getConnection() throws SQLException {
        return this.freeConnections.isEmpty() ? this.createConnection() : this.freeConnections.pop();
    }

    public int getSize() {
        return this.size;
    }
}