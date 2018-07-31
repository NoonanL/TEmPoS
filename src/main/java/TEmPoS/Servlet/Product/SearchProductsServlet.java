package TEmPoS.Servlet.Product;

import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2Products;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SearchProductsServlet extends HttpServlet {

    private H2Products h2Products;
    private H2User h2User;

    public SearchProductsServlet(){}

    public SearchProductsServlet(H2Products h2Products, H2User h2User){
        this.h2Products = h2Products;
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
        String requestUser = input.getString("requestUser");
        String searchString = input.getString("searchString");

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            responseJson = h2Products.searchProducts(searchString);
            responseJson.put("response", "OK");
            responseJson.put("error", "None.");
        }


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
