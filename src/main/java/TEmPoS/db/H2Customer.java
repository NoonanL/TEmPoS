package TEmPoS.db;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class H2Customer extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier
     */
    public H2Customer(ConnectionSupplier connectionSupplier) {
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
        execute(conn, "CREATE TABLE IF NOT EXISTS customers (id int AUTO_INCREMENT PRIMARY KEY, firstname VARCHAR(255), surname VARCHAR(255), purchaseHistory VARCHAR(1024))");
    }

    public boolean createCustomer(String firstname, String surname) throws SQLException {
        String query = "INSERT into customers (firstname, surname) VALUES(?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, firstname);
            ps.setString(2, surname);
            int count = ps.executeUpdate();
            LOG.debug("insert count = " + count);
            return count == 1;
        }
    }

    public boolean editCustomer(int targetId, String firstname, String surname) {
        final String EDIT_USER_QUERY = "UPDATE customers SET firstname =?, surname=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(EDIT_USER_QUERY)) {
            ps.setString(1, firstname);
            ps.setString(2, surname);
            ps.setInt(3, targetId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getCustomersBySurname(String searchSurname){
        final String GET_USER_QUERY = "SELECT id, firstname, surname FROM customers WHERE surname=?";
        //String details = null;
        JSONObject customerList = new JSONObject();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_USER_QUERY)){
            ps.setString(1, searchSurname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String firstname = rs.getString(2);
                String surname = rs.getString(3);
                Map<String, String> user = new LinkedHashMap<>();
                user.put("id",id);
                user.put("firstname" , firstname);
                user.put("surname" , surname);
                customerList.put(id , new JSONObject(user));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    public JSONObject getCustomerById(int id){
        final String GET_USER_QUERY = "SELECT firstname, surname FROM customers WHERE id=?";
        JSONObject customer = new JSONObject();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_USER_QUERY)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String firstname = rs.getString(1);
                String surname = rs.getString(2);
                customer.put("firstname" , firstname);
                customer.put("surname" , surname);
                customer.put("id", id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    public boolean deleteCustomerById(int id) {
        final String DELETE_USER_QUERY = "DELETE FROM customers WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_USER_QUERY)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject getCustomers(){
        final String GET_USER_QUERY = "SELECT id, firstname, surname FROM customers";
        JSONObject customerList = new JSONObject();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_USER_QUERY)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String firstname = rs.getString(2);
                String surname = rs.getString(3);
                Map<String, String> user = new LinkedHashMap<>();
                user.put("id", id);
                user.put("firstname" , firstname);
                user.put("surname" , surname);
                customerList.put(id , new JSONObject(user));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }
}
