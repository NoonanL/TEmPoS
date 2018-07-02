package TEmPoS.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.fail;

public class RebuildUsers {


    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2UserTest.class);

    private H2User db;


    @Before
    public void setUp() {
        db = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
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
    public void register(){
        System.out.println("=====================================");
        System.out.println("Testing User Registration - will now fail because database with test user already exists");
        System.out.println("=====================================");
        if(db.register("Liam", "password", "Y")){
            System.out.println("Liam successfuly registered");
        }else{
            System.out.println("registration failed");
        }
    }

}
