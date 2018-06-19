package TEmPoS.Servlet.Customer;

import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2Customer;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class EditCustomerServlet extends HttpServlet {

    private H2Customer h2Customer;
    private H2User h2User;

    public EditCustomerServlet(){}

    public EditCustomerServlet(H2Customer h2Customer, H2User h2User){
        this.h2Customer = h2Customer;
        this.h2User = h2User;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        String id = input.getString("id");
        String firstname = input.getString("firstname");
        String surname = input.getString("surname");
        String requestUser = input.getString("requestUser");

        int editId = Integer.parseInt(id);

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            if(h2Customer.editCustomer(editId,firstname,surname)){
                //System.out.println("New user created.");
                responseJson.put("response", "OK");
            }else{
                //System.out.println("Error creating user");
                responseJson.put("response", "false");
            }
        }


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
