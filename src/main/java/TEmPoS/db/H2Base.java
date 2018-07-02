package TEmPoS.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class H2Base implements AutoCloseable{

    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2Base.class);

    private Connection connection;

    H2Base(Connection connection) {
        this.connection = connection;
    }

    @Override
    public synchronized void close()throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    protected static void execute(Connection connection, String cmd) {
        try {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute(cmd);
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected void errIfClosed() {
        if (getConnection() == null) {
            throw new NullPointerException("H2 connection is closed");
        }
    }

    public void loadResource(String name) {
        try {

            String cmd = new Scanner(getClass().getResource(name).openStream()).useDelimiter("\\Z").next();

            PreparedStatement ps = getConnection().prepareStatement(cmd);
            ps.execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
