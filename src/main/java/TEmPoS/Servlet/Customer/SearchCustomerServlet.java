package TEmPoS.Servlet.Customer;

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

public class SearchCustomerServlet extends HttpServlet {

    private H2Customer h2Customer;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public SearchCustomerServlet() {
    }

    public SearchCustomerServlet(H2Customer h2Customer, H2User h2User) {
        this.h2Customer = h2Customer;
        this.h2User = h2User;

        requiredParams.put("searchString", "String");
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
            String searchString = input.getString("searchString");


            if (h2User.isRegistered(requestUser)) {
                responseJson = h2Customer.searchCustomers(searchString);
                responseJson.put("response", "OK.");
                responseJson.put("error", "None.");
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
