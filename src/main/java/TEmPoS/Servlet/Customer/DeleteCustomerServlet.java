package TEmPoS.Servlet.Customer;

import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
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
import java.util.ArrayList;

public class DeleteCustomerServlet extends HttpServlet {

    private H2Customer h2Customer;
    private H2User h2User;
    private ArrayList<String> requiredParams = new ArrayList<>();


    public DeleteCustomerServlet(){}

    public DeleteCustomerServlet(H2Customer h2Customer, H2User h2User){
        this.h2Customer = h2Customer;
        this.h2User = h2User;

        requiredParams.add("targetCustomerId");
        requiredParams.add("requestUser");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if (inputChecker.isValid()) {
            String id = input.getString("targetCustomerId");
            String requestUser = input.getString("requestUser");
            int deleteId = Integer.parseInt(id);

            if (h2User.isRegistered(requestUser)) {
                if (h2Customer.deleteCustomerById(deleteId)) {
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");
                } else {
                    responseJson.put("response", "false");
                    responseJson.put("error", "Failed to delete customer.");
                }
            }
        }else{
            responseJson.put("response", "false");
            responseJson.put("error", "Missing required fields.");
        }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseJson);
            out.flush();

    }
}
