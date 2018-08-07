package TEmPoS.Servlet.Product;

import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Products;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SearchProductsServlet extends HttpServlet {

    private H2Products h2Products;
    private H2User h2User;
    private ArrayList<String> requiredParams = new ArrayList<>();


    public SearchProductsServlet(){}

    public SearchProductsServlet(H2Products h2Products, H2User h2User){
        this.h2Products = h2Products;
        this.h2User = h2User;

        requiredParams.add("searchString");
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

        if(inputChecker.isValid()) {

            String requestUser = input.getString("requestUser");
            String searchString = input.getString("searchString");


            if (h2User.isRegistered(requestUser)) {
                responseJson = h2Products.searchProducts(searchString);
                responseJson.put("response", "OK");
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
