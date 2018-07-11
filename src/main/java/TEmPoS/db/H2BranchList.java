package TEmPoS.db;

import TEmPoS.Model.Customer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class H2BranchList extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2User.class);

    /**
     * establish connection to database
     * @param connectionSupplier
     */
    public H2BranchList(ConnectionSupplier connectionSupplier) {
        super(connectionSupplier.provide());
        try {
            initTable(getConnection());
        } catch (Exception e) {
            LOG.error("Can't find database driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes SQL table, creates one if it doesn't already exist.
     * @param conn connection to the database file
     * @throws SQLException
     */
    private void initTable(Connection conn) throws SQLException {
        loadResource("/schema.sql");
    }


    public boolean createBranch(String branchId) throws SQLException {
        String query = "INSERT into branchList (branch) VALUES(?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, branchId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getBranchList(){
        final String GET_BRANCHES_QUERY = "SELECT * FROM branchList";
        JSONObject branchList = new JSONObject();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_BRANCHES_QUERY)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString(2));
                branchList.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return branchList;
    }

    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE branchList")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
