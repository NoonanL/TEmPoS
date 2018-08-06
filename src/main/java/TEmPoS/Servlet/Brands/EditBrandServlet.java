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

public class EditBrandServlet extends HttpServlet {

    private H2Brands h2Brands;
    private H2User h2User;
    private ArrayList<String> requiredParams = new ArrayList<>();

    public EditBrandServlet(){}

    public EditBrandServlet(H2Brands h2Brands, H2User h2User){
        this.h2Brands = h2Brands;
        this.h2User = h2User;

        requiredParams.add("id");
        requiredParams.add("brand");
        requiredParams.add("distributor");
        requiredParams.add("requestUser");

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

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            String requestUser = input.getString("requestUser");
            Brand newBrand = new Brand();
            newBrand.setId(input.getString("id"));
            newBrand.setBrand(input.getString("brand"));
            newBrand.setDistributor(input.getString("distributor"));

            if (h2User.isRegistered(requestUser)) {
                try {
                    if (h2Brands.editBrand(newBrand)) {
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
