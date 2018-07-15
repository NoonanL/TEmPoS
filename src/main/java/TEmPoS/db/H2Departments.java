package TEmPoS.db;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Departments extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Departments(ConnectionSupplier connectionSupplier) {
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

    public boolean createDepartment(String department) throws SQLException {
        String query = "INSERT into departments (department) VALUES(?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, department);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editDepartment(int id, String department) throws SQLException {
        String query = "UPDATE departments SET department =? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, department);
            ps.setInt(2, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getDepartments(){
        final String GET_DEPARTMENTS_QUERY = "SELECT * FROM departments";
        JSONObject departmentList = new JSONObject();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DEPARTMENTS_QUERY)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                departmentList.put(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return departmentList;
    }

    public boolean deleteDeparment(int id){
        final String DELETE_DEPARTMENT_QUERY = "DELETE FROM departments WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_DEPARTMENT_QUERY)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE departments")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
