package TEmPoS.db;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;

import static org.junit.Assert.fail;

public class H2BranchListTest {

    static final Logger LOG = LoggerFactory.getLogger(H2CustomerTest.class);

    private H2BranchList db;

    @Before
    public void setUp() {
        db = new H2BranchList(new ConnectionSupplier(ConnectionSupplier.FILE));
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
    public void createBranches() throws SQLException {
        System.out.println("=====================================");
        System.out.println("Creating Branches");
        System.out.println("=====================================");
        if(db.createBranch("branch01") &&
            db.createBranch("branch02") &&
            db.createBranch("branch03")
                ){
            System.out.println("Branches successfuly added");
        }else{
            System.out.println("Branch creation failed failed");
        }
    }

    @Test
    public void getBranches() {
        System.out.println("=====================================");
        System.out.println("Getting Branches");
        System.out.println("=====================================");
        //System.out.println(db.getBranchList().toString());
        JSONObject branches = db.getBranchList();
        for (Iterator it = branches.keys(); it.hasNext(); ) {
//            String json = it.next().toString();
////            //Skip connection response object.
////            JSONObject userJson = (customers.getJSONObject(json));
            System.out.println(branches.getString(it.next().toString()));
//
        }
    }

    @Test
    public void deleteBranchTable(){
        System.out.println("=====================================");
        System.out.println("Deleting branchList table");
        System.out.println("=====================================");
        if(db.deleteTable()){
            System.out.println("Branch List deleted");
        }else{
            System.out.println("Failed to delete table");
        }
    }

}
