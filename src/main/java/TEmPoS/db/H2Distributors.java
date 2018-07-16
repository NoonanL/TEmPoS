package TEmPoS.db;

import TEmPoS.Model.Brand;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Distributors extends H2Base {


    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Distributors(ConnectionSupplier connectionSupplier) {
        super(connectionSupplier.provide());
        try {
            initTable();
        } catch (Exception e) {
            LOG.error("Can't find database driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes SQL table, creates one if it doesn't already exist.
     */
    private void initTable() {
        loadResource("/schema.sql");
    }

    public boolean createDistributor(String distributor) throws SQLException {
        String query = "INSERT into distributors (distributor) VALUES(?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, distributor);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getDistributors(){
        final String GET_DISTRIBUTORS_QUERY = "SELECT * FROM distributors";
        JSONObject distributorList = new JSONObject();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DISTRIBUTORS_QUERY)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                distributorList.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distributorList;
    }

    public boolean deleteDistributor(int id){
        final String DELETE_DISTRIBUTOR_QUERY = "DELETE FROM distributors WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_DISTRIBUTOR_QUERY)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existingDistributor(String distributor) throws SQLException{
        final String GET_DISTRIBUTOR_QUERY = "SELECT * FROM distributors WHERE distributor=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DISTRIBUTOR_QUERY)) {
            ps.setString(1, distributor);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE distributors")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
