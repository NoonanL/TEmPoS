package TEmPoS.db;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;

import static org.junit.Assert.*;


public class H2CustomerTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);

    private H2Customer db;

    @Before
    public void setUp() throws Exception {
        db = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));
    }

    @After
    public void tearDown() throws Exception {
        try {
            db.close();
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    public void createCustomer() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Testing Customer Registration");
        System.out.println("=====================================");
        if(db.createCustomer("Test", "CustomerTesterson")){
            System.out.println("Customer successfuly registered");
        }else{
            System.out.println("Customer registration failed");
        }
    }

    @Test
    public void editCustomer() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Testing edit customer data (except password)");
        System.out.println("=====================================");
        if(db.editCustomer(1, "Test", "CustomerTesterson")){
            System.out.println("Customer successfuly edited");
        }else{
            System.out.println("Customer edit failed");
        }
    }

    @Test
    public void getCustomersBySurname() {
        System.out.println("=====================================");
        System.out.println("Testing get customer data (except password) by surname");
        System.out.println("=====================================");
        System.out.println(db.getCustomersBySurname("CustomerTesterson"));
    }

    @Test
    public void getCustomerById() {
        System.out.println("=====================================");
        System.out.println("Testing get customer data (except password) by id");
        System.out.println("=====================================");

        System.out.println(db.getCustomerById(1));
    }

    @Test
    public void deleteCustomerById() throws SQLException {
        db.createCustomer("DeleteMeTest", "DeleteMeTestCustomerTesterson");
        System.out.println(db.getCustomers());
        JSONObject users = db.getCustomersBySurname("DeleteMeTestCustomerTesterson");
        //iterate through list
        for (Iterator it = users.keys(); it.hasNext(); ) {
            String json = it.next().toString();
            JSONObject userJson = (users.getJSONObject(json));
            //if username and surname match
            if(userJson.getString("surname").equals("DeleteMeTestCustomerTesterson") && userJson.getString("firstname").equals("DeleteMeTest")){
                //delete customer with that id
                int deleteId = Integer.parseInt(userJson.getString("id"));
                db.deleteCustomerById(deleteId);
            }
        }
        System.out.println(db.getCustomers());

        //execute delete on that id
    }

    @Test
    public void getCustomers() {
        System.out.println(db.getCustomers().toString());
        JSONObject customers = db.getCustomers();
        for (Iterator it = customers.keys(); it.hasNext(); ) {
            String json = it.next().toString();
            //Skip connection response object.
            JSONObject userJson = (customers.getJSONObject(json));
            System.out.println(userJson);

        }
    }
}