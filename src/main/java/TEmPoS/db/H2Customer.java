package TEmPoS.db;

import TEmPoS.Model.Customer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class H2Customer extends H2Base {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(H2Customer.class);


    /**
     * establish connection to database
     * @param connectionSupplier     */
    public H2Customer(ConnectionSupplier connectionSupplier) {
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


    public boolean createCustomer(Customer customer) throws SQLException {
        String query = "INSERT into customers (title, firstname, surname, street, town, postcode, city, country, mobile, email, marketingStatus) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setString(1, customer.getTitle());
            ps.setString(2, customer.getFirstname());
            ps.setString(3, customer.getSurname());
            ps.setString(4, customer.getStreet());
            ps.setString(5, customer.getTown());
            ps.setString(6, customer.getPostcode());
            ps.setString(7, customer.getCity());
            ps.setString(8, customer.getCountry());
            ps.setString(9, customer.getMobile());
            ps.setString(10, customer.getEmail());
            ps.setString(11, customer.getMarketingStatus());
            int count = ps.executeUpdate();
            LOG.debug("insert count = " + count);
            return count == 1;
        }
    }

    /**
     * Edits an existing instance of a customer
     * @param targetId the target customer to edit
     * @param customer the updated firstname
     * @return boolean success/failure
     */
    public boolean editCustomer(int targetId, Customer customer) {
        final String EDIT_USER_QUERY = "UPDATE customers SET title =?, firstname=?, surname=?, street=?, town=?, postcode=?, city=?, country=?, mobile=?, email=?, marketingStatus=? WHERE id=?";
        try (PreparedStatement ps = getConnection().prepareStatement(EDIT_USER_QUERY)) {
            ps.setString(1, customer.getTitle());
            ps.setString(2, customer.getFirstname());
            ps.setString(3, customer.getSurname());
            ps.setString(4, customer.getStreet());
            ps.setString(5, customer.getTown());
            ps.setString(6, customer.getPostcode());
            ps.setString(7, customer.getCity());
            ps.setString(8, customer.getCountry());
            ps.setString(9, customer.getMobile());
            ps.setString(10, customer.getEmail());
            ps.setString(11, customer.getMarketingStatus());
            ps.setInt(12, targetId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * performs a search on customer table on the surname field only
     * @param searchSurname the surname to search for
     * @return a jsonObject of customers matching the search criteria
     */
    JSONObject getCustomersBySurname(String searchSurname){
        final String GET_USER_QUERY = "SELECT * FROM customers WHERE surname=?";
        //String details = null;
        JSONObject customerList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_USER_QUERY)){
            ps.setString(1, searchSurname);
            ResultSet rs = ps.executeQuery();
            customerList = parseCustomers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    /**
     * gets an individual customer by their unique id
     * @param id the id to fetch
     * @return a json objecting representing a customer
     */
    JSONObject getCustomerById(int id){
        final String GET_USER_QUERY = "SELECT * FROM customers WHERE id=?";
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

    /**
     * deletes a customer record by their id
     * @param id the id of the customer to delete
     * @return boolean for success/failure
     */
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

    /**
     * search for customers by their firstname or surname
     * @param searchString the search criteria to look for
     * @return a json object representing customers who match the search criteria
     */
    public JSONObject searchCustomers(String searchString){
        final String SEARCH_CUSTOMER_QUERY = "SELECT * FROM customers WHERE firstname LIKE ? OR surname LIKE ? OR postcode LIKE ?";
        JSONObject customerList;
        String searchWildcard = "%" + searchString + "%";
        try (PreparedStatement ps = getConnection().prepareStatement(SEARCH_CUSTOMER_QUERY)){
            ps.setString(1, searchWildcard);
            ps.setString(2, searchWildcard);
            ps.setString(3, searchWildcard);
            ResultSet rs = ps.executeQuery();
            customerList = parseCustomers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    /**
     * gets all customer data
     * @return a json object representing all the customers in the table
     */
    public JSONObject getCustomers(){
        final String GET_USER_QUERY = "SELECT * FROM customers";
        JSONObject customerList;
        try (PreparedStatement ps = getConnection().prepareStatement(GET_USER_QUERY)){
            ResultSet rs = ps.executeQuery();
            customerList = parseCustomers(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }

    /**
     * util function to parse customers from a resultset
     * @param rs the resultset to parse
     * @return a jsonObject representing customers parsed from the resultset
     * @throws SQLException
     */
    private JSONObject parseCustomers(ResultSet rs) throws SQLException {
        JSONObject customerList = new JSONObject();
        while (rs.next()) {
            String id = rs.getString(1);
            String title = rs.getString(2);
            String firstname = rs.getString(3);
            String surname = rs.getString(4);
            String street = rs.getString(5);
            String town = rs.getString(6);
            String postcode = rs.getString(7);
            String city = rs.getString(8);
            String country = rs.getString(9);
            String mobile = rs.getString(10);
            String email = rs.getString(11);
            String marketingStatus = rs.getString(12);
            Map<String, String> user = new LinkedHashMap<>();

            // This is ridiculous. Fix me.

            user.put("id", id);
            user.put("title", title);
            user.put("firstname" , firstname);
            user.put("surname" , surname);
            user.put("street", street);
            user.put("town", town);
            user.put("postcode", postcode);
            user.put("city", city);
            user.put("country", country);
            user.put("mobile", mobile);
            user.put("email", email);
            user.put("marketingStatus", marketingStatus);
            customerList.put(id , new JSONObject(user));
        }
        return customerList;
    }

}
