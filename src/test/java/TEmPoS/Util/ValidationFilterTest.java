package TEmPoS.Util;

import TEmPoS.Model.Customer;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ValidationFilterTest {

    Customer testCustomer = new Customer();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
   public void validationTestTrue(){

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

        JSONObject testJson = testCustomer.toJson();

        ArrayList<String> required = new ArrayList<>();

        required.add("title");

        System.out.println(testJson);

//        for(Iterator it = testJson.keys(); ((Iterator) it).hasNext();){
//            System.out.println(it.next());
//        }
       ValidationFilter test = new ValidationFilter(required,testJson);
    }
}
