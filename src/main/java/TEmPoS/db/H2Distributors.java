package TEmPoS.db;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Distributor;
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

    public boolean createDistributor(String distributor) {
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
        JSONObject distributorList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DISTRIBUTORS_QUERY)){
            ResultSet rs = ps.executeQuery();
            distributorList = parseDistributors(rs);
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return distributorList;
    }

    public JSONObject getDistributor(int id){
        final String GET_DISTRIBUTOR_BY_ID_QUERY = "SELECT * FROM distributors WHERE id=?";
        JSONObject distributorList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DISTRIBUTOR_BY_ID_QUERY)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            distributorList = parseDistributors(rs);
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

    public boolean editDistributor(Distributor distributor) {
        String query = "UPDATE distributors SET distributor =? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, distributor.getName());
            ps.setInt(2, Integer.parseInt(distributor.getId()));
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existingDistributor(String query, String field) throws SQLException{
        final String GET_DISTRIBUTOR_QUERY = "SELECT * FROM distributors WHERE " + field + "=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DISTRIBUTOR_QUERY)) {
            ps.setString(1, query);
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

    private JSONObject parseDistributors(ResultSet rs) throws SQLException {
        JSONObject distributorList = new JSONObject();
        while (rs.next()) {
            Distributor distributor = new Distributor();
            distributor.setId(rs.getString(1));
            distributor.setName(rs.getString(2));
            distributorList.put(distributor.getId() , distributor.toJson());
        }
        return distributorList;
    }


    public boolean propagate(String oldVal, Distributor distributor){
        String query = "UPDATE brands SET distributor =? WHERE distributor=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, distributor.getName());
            ps.setString(2, oldVal);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
