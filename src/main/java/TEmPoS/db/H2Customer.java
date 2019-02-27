package TEmPoS.db;

import TEmPoS.Model.Customer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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


    /**
     * Creates a new customer in the database
     * @param customer
     * @return
     */
    public boolean createCustomer(Customer customer) {
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
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Edits an existing instance of a customer
     * @param customer the updated firstname
     * @return boolean success/failure
     */
    public boolean editCustomer(Customer customer) {
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
            ps.setInt(12, Integer.parseInt(customer.getId()));
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
    public JSONObject getCustomerById(int id){
        final String GET_USER_QUERY = "SELECT * FROM customers WHERE id=?";
        Customer customer = new Customer();
        try (PreparedStatement ps = getConnection().prepareStatement(GET_USER_QUERY)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customer.setId(rs.getString(1));
                customer.setTitle(rs.getString(2));
                customer.setFirstname(rs.getString(3));
                customer.setSurname(rs.getString(4));
                customer.setStreet(rs.getString(5));
                customer.setTown(rs.getString(6));
                customer.setPostcode(rs.getString(7));
                customer.setCity(rs.getString(8));
                customer.setCountry(rs.getString(9));
                customer.setMobile(rs.getString(10));
                customer.setEmail(rs.getString(11));
                customer.setMarketingStatus(rs.getString(12));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer.toJson();
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
            //System.out.println(customerList);
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
            Customer newCustomer = new Customer();
            newCustomer.setId(rs.getString(1));
            newCustomer.setTitle(rs.getString(2));
            newCustomer.setFirstname(rs.getString(3));
            newCustomer.setSurname(rs.getString(4));
            newCustomer.setStreet(rs.getString(5));
            newCustomer.setTown(rs.getString(6));
            newCustomer.setPostcode(rs.getString(7));
            newCustomer.setCity(rs.getString(8));
            newCustomer.setCountry(rs.getString(9));
            newCustomer.setMobile(rs.getString(10));
            newCustomer.setEmail(rs.getString(11));
            newCustomer.setMarketingStatus(rs.getString(12));

            customerList.append("customers" , newCustomer.toJson());
        }
        return customerList;
    }

    /**
     * BE CAREFUL WITH ME
     * Function to delete the customer table
     */
    public boolean deleteTable(){
        try (PreparedStatement ps = getConnection().prepareStatement("DROP TABLE customers")){
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
