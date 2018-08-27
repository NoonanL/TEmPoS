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
import java.util.Iterator;
import java.util.Map;

public class EditBrandServlet extends HttpServlet {

    private H2Brands h2Brands;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public EditBrandServlet(){}

    public EditBrandServlet(H2Brands h2Brands, H2User h2User){
        this.h2Brands = h2Brands;
        this.h2User = h2User;

        requiredParams.put("id","integer");
        requiredParams.put("brand", "String");
        requiredParams.put("distributor", "String");
        requiredParams.put("requestUser", "String");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();
        String oldVal = "";

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            String requestUser = input.getString("requestUser");

            JSONObject oldValJson = h2Brands.getBrand(Integer.parseInt(input.getString("id")));
            for (Iterator it = oldValJson.keys(); it.hasNext(); ) {
                String json = it.next().toString();
                if(!json.equals("connection") && !json.equals("error") && !json.equals("response")) {
                    JSONObject userJson = (oldValJson.getJSONObject(json));
                    oldVal = userJson.getString("brand");

                }
            }
            //System.out.println("Old value is " + oldVal + ", and new value will be " + newVal + ".");

            Brand newBrand = new Brand();
            newBrand.setId(input.getString("id"));
            newBrand.setBrand(input.getString("brand"));
            newBrand.setDistributor(input.getString("distributor"));

            if (h2User.isRegistered(requestUser)) {
                try {
                    if (h2Brands.editBrand(newBrand) && h2Brands.propagate(oldVal, newBrand) ) {
                        responseJson.put("response", "OK");
                        responseJson.put("error", "None.");
                    } else {
                        //System.out.println("Error creating user");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Error editing Brand.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else {
            responseJson.put("response", "false");
            responseJson.put("error", "Missing required fields.");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
