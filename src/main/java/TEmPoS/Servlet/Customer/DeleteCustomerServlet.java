package TEmPoS.Servlet.Customer;

import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2Customer;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class DeleteCustomerServlet extends HttpServlet {

    private H2Customer h2Customer;
    private H2User h2User;

    public DeleteCustomerServlet(){}

    public DeleteCustomerServlet(H2Customer h2Customer, H2User h2User){
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
        String id = input.getString("targetCustomerId");
        String requestUser = input.getString("requestUser");
        //System.out.println(id);
        //System.out.println(requestUser);
        int deleteId = Integer.parseInt(id);

        //System.out.println(deleteId);
        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            if(h2Customer.deleteCustomerById(deleteId)){
                responseJson.put("response", "OK");
                responseJson.put("error", "None.");
            }else{
                responseJson.put("response", "false");
                responseJson.put("error", "Failed to delete customer.");
            }
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
