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

public class EditCustomerServlet extends HttpServlet {

    private H2Customer h2Customer;
    private H2User h2User;
    private ArrayList<String> requiredParams = new ArrayList<>();

    public EditCustomerServlet(){}

    public EditCustomerServlet(H2Customer h2Customer, H2User h2User){
        this.h2Customer = h2Customer;
        this.h2User = h2User;

        requiredParams.add("id");
        requiredParams.add("title");
        requiredParams.add("firstname");
        requiredParams.add("surname");
        requiredParams.add("marketingStatus");
        requiredParams.add("email");
        requiredParams.add("city");
        requiredParams.add("postcode");
        requiredParams.add("town");
        requiredParams.add("street");
        requiredParams.add("mobile");
        requiredParams.add("country");
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
