import pool.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class App {
    public static Runnable addTokenRunnable(String token) {
        return () -> {
            try (Connection connection = JdbcConnectionPool.getInstance().getConnection()){
                PreparedStatement ps = connection.prepareStatement("INSERT INTO tokens (value) VALUES (?)");
                ps.setString(1, token);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    }


    public static void main(String[] args) throws InterruptedException {
        var threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; ++i) {
            String token = Integer.toHexString((int) Math.pow(i * (i + new Random().nextInt(i + 10) ), 2));
            var t = new Thread(addTokenRunnable(token));
            threads.add(t);
        }

        for (Thread t: threads) {
            t.start();
        }
        for (Thread t: threads) {
            t.join();
        }
    }
}
