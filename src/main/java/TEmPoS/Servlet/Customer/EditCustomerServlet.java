package TEmPoS.Servlet.Customer;

import TEmPoS.Model.Customer;
import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Customer;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditCustomerServlet extends HttpServlet {

    private H2Customer h2Customer;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public EditCustomerServlet(){}

    public EditCustomerServlet(H2Customer h2Customer, H2User h2User){
        this.h2Customer = h2Customer;
        this.h2User = h2User;

        requiredParams.put("id", "String");
        requiredParams.put("firstname", "String");
        requiredParams.put("surname", "String");
        requiredParams.put("marketingStatus", "String");
        requiredParams.put("email", "String");
        requiredParams.put("city", "String");
        requiredParams.put("postcode", "String");
        requiredParams.put("town", "String");
        requiredParams.put("street", "String");
        requiredParams.put("mobile", "String");
        requiredParams.put("country", "String");
        requiredParams.put("requestUser", "String");

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

            String requestUser = input.getString("requestUser");

            Customer newCustomer = new Customer();
            newCustomer.setId(input.getString("id"));
            newCustomer.setTitle(input.getString("title"));
            newCustomer.setFirstname(input.getString("firstname"));
            newCustomer.setSurname(input.getString("surname"));
            newCustomer.setMarketingStatus(input.getString("marketingStatus"));
            newCustomer.setEmail(input.getString("email"));
            newCustomer.setCity(input.getString("city"));
            newCustomer.setPostcode(input.getString("postcode"));
            newCustomer.setTown(input.getString("town"));
            newCustomer.setStreet(input.getString("street"));
            newCustomer.setMobile(input.getString("mobile"));
            newCustomer.setCountry(input.getString("country"));

            if (h2User.isRegistered(requestUser)) {
                if (h2Customer.editCustomer(newCustomer)) {
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");
                } else {
                    responseJson.put("response", "false");
                    responseJson.put("error", "Failed to edit customer.");
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
