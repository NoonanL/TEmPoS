package TEmPoS.Servlet.Brands;

import TEmPoS.Model.Brand;
import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Brands;
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
import java.util.HashMap;
import java.util.Map;

public class CreateBrandServlet extends HttpServlet {

    private H2Brands h2Brands;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public CreateBrandServlet(){}

    public CreateBrandServlet(H2Brands h2Brands, H2User h2User){
        this.h2Brands = h2Brands;
        this.h2User = h2User;

        requiredParams.put("brand", "String");
        requiredParams.put("distributor", "String");
        requiredParams.put("requestUser", "String");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            Brand newBrand = new Brand();
            newBrand.setBrand(input.getString("brand"));
            newBrand.setDistributor(input.getString("distributor"));

            String requestUser = input.getString("requestUser");

            if (h2User.isRegistered(requestUser)) {
                try {
                    if (h2Brands.existingBrand(newBrand.getBrand())) {
                        responseJson.put("response", "false");
                        responseJson.put("error", "Brand name not unique.");
                    } else {
                        if (h2Brands.createBrand(newBrand)) {
                            responseJson.put("response", "OK");
                            responseJson.put("error", "None.");
                        } else {
                            responseJson.put("response", "false");
                            responseJson.put("error", "Failed to create new brand.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
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
