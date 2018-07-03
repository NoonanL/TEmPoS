package TEmPoS.db;

import TEmPoS.Model.Customer;
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
    Customer testCustomer = new Customer();

    @Before
    public void setUp() {
        db = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));

        testCustomer.setTitle("Mr");
        testCustomer.setFirstname("Testy");
        testCustomer.setSurname("Testerson");
        testCustomer.setStreet("69 Boner Avenue");
        testCustomer.setTown("Humpsville");
        testCustomer.setPostcode("IV5 7DU");
        testCustomer.setCity("Glasgow");
        testCustomer.setMobile("07807168580");
        testCustomer.setEmail("happyEndings@naughtybois.com");
        testCustomer.setMarketingStatus("True");

    }

    @After
    public void tearDown() {
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
        if(db.createCustomer(testCustomer)){
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
        if(db.editCustomer(1, testCustomer)){
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
        db.createCustomer(testCustomer);
        Customer newTest = testCustomer;
        newTest.setSurname("DeleteMeTestCustomerTesterson");
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

    @Test
    public void searchCustomers() {
        System.out.println(db.searchCustomers("e").toString());
        JSONObject customers = db.searchCustomers("e");
        for (Iterator it = customers.keys(); it.hasNext(); ) {
            String json = it.next().toString();
            //Skip connection response object.
            JSONObject userJson = (customers.getJSONObject(json));
            System.out.println(userJson);

        }
    }
}