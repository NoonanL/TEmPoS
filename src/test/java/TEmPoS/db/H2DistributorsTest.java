package TEmPoS.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class H2DistributorsTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);
    private H2Distributors db;

    @Before
    public void setUp() throws Exception {
        db = new H2Distributors(new ConnectionSupplier(ConnectionSupplier.FILE));
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
    public void createDistributor() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Creating Distributors");
        System.out.println("=====================================");
        if(db.createDistributor("EMD") &&
                db.createDistributor("REMO") &&
                db.createDistributor("Westside")
                ){
            System.out.println("Distributors successfuly added");
        }else{
            System.out.println("Distributors creation failed");
        }
    }

    @Test
    public void getDistributors() {
        System.out.println("=====================================");
        System.out.println("Getting Brands");
        System.out.println("=====================================");
        System.out.println(db.getDistributors().toString());
    }

    @Test
    public void deleteDistributor() {
        System.out.println("=====================================");
        System.out.println("Testing delete Distributors by id");
        System.out.println("=====================================");
        if(db.deleteDistributor(1)){
            System.out.println("Distributors successfully deleted");
        }else{
            System.out.println("Failed to delete distributor");
        }
    }

    @Test
    public void existingDistributor() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Testing if distributor already exists");
        System.out.println("=====================================");
        if(db.existingDistributor("EMD", "name")){
            System.out.println("Distributors already exists!!");
        }else{
            System.out.println("Distributors is unique.");
        }
    }
}