package TEmPoS.db;

import TEmPoS.Model.Department;
import TEmPoS.Model.Distributor;
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

    public boolean createDepartment(Department department) {
        String query = "INSERT into departments (department) VALUES(?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, department.getDepartment());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editDepartment(Department department) {
        String query = "UPDATE departments SET department =? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, department.getDepartment());
            ps.setInt(2, Integer.parseInt(department.getId()));
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
            departmentList = parseDepartments(rs);
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

    private JSONObject parseDepartments(ResultSet rs) throws SQLException {
        JSONObject departmentList = new JSONObject();
        while (rs.next()) {
            Department department = new Department();
            department.setId(rs.getString(1));
            department.setDepartment(rs.getString(2));
            departmentList.put(department.getId() , department.toJson());
        }
        return departmentList;
    }

    public boolean existingDepartment(String department) throws SQLException{
        final String GET_DEPARTMENT_QUERY = "SELECT * FROM departments WHERE department=?";
        try (PreparedStatement ps = getConnection().prepareStatement(GET_DEPARTMENT_QUERY)) {
            ps.setString(1, department);
            ResultSet rs = ps.executeQuery();
            return rs.next();
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
