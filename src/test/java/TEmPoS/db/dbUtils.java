package TEmPoS.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.fail;

public class dbUtils {


    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(H2UserTest.class);

    private H2User db;
    private H2Customer customerDb;
    private H2BranchList branchList;


    @Before
    public void setUp() {
        db = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
        customerDb = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));
        branchList = new H2BranchList(new ConnectionSupplier(ConnectionSupplier.FILE));
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
        System.out.println("Re-Registering Liam's admin account");
        System.out.println("=====================================");
        if(db.register("Liam", "password", "Y")){
            System.out.println("Liam successfuly registered");
        }else{
            System.out.println("registration failed");
        }
    }

    @Test
    public void resetCustomers(){
        System.out.println("=====================================");
        System.out.println("Deleting customer table");
        System.out.println("=====================================");
        if(customerDb.deleteTable()){
            System.out.println("Customer table deleted");
        }else{
            System.out.println("Failed to delete table");
        }
    }

    @Test
    public void setBranches() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Adding branches to table");
        System.out.println("=====================================");
        branchList.createBranch("Branch01");
        branchList.createBranch("Branch02");
        branchList.createBranch("Warehouse");

    }


}
