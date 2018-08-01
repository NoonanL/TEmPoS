package TEmPoS.Servlet.Brands;

import TEmPoS.Model.Brand;
import TEmPoS.Util.RequestJson;
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

public class EditBrandServlet extends HttpServlet {

    private H2Brands h2Brands;
    private H2User h2User;

    public EditBrandServlet(){}

    public EditBrandServlet(H2Brands h2Brands, H2User h2User){
        this.h2Brands = h2Brands;
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        String id = input.getString("id");
        String requestUser = input.getString("requestUser");

        Brand newBrand = new Brand();
        newBrand.setId(id);
        newBrand.setBrand(input.getString("brand"));
        newBrand.setDistributor(input.getString("distributor"));

        //int editId = Integer.parseInt(id); - No longer needed if we pass the whole object eh?

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)) {
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

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
