package TEmPoS.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSupplier {


    /**
     * Set file directory for database, in this case inside my dropbox directory for testing purposes.
     * On deployment server might make sense to just have this as the root.
     */
    public static final String MEMORY = "jdbc:h2:mem:TEmPoSdb";
    public static final String FILE = "jdbc:h2:~/\\Dropbox (Personal)\\Honours Project\\Code\\Development\\TEmPoS/TEmPoSdb";

        private final String db;

        public ConnectionSupplier(String db) {
            this.db = db;
        }

        public Connection provide() {
            try {
                // the driver class must be loaded
                // so that DriverManager can find the loaded class
                Class.forName("org.h2.Driver");
                return DriverManager.getConnection(db, "sa", "");
            } catch (SQLException | ClassNotFoundException e) {
                throw new ConnectionSupplierException(e);
            }
        }

        public class ConnectionSupplierException extends RuntimeException {
            ConnectionSupplierException(Exception e) {
                super(e);
            }
        }

}
