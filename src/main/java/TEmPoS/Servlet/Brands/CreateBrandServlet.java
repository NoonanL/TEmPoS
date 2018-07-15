package TEmPoS.Servlet.Brands;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Product;
import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2Brands;
import TEmPoS.db.H2Products;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CreateBrandServlet extends HttpServlet {

    private H2Brands h2Brands;
    private H2User h2User;

    public CreateBrandServlet(){}

    public CreateBrandServlet(H2Brands h2Brands, H2User h2User){
        this.h2Brands = h2Brands;
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        Brand newBrand = new Brand();

        newBrand.setBrand(input.getString("brand"));
        newBrand.setDistributor(input.getString("distributor"));

        String requestUser = input.getString("requestUser");

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){

            try {
                if(h2Brands.existingBrand(newBrand.getBrand())){
                    responseJson.put("response", "false");
                    responseJson.put("error", "Brand name not unique.");
                }else{
                    if(h2Brands.createBrand(newBrand)){
                        //System.out.println("New product created.");
                        responseJson.put("response", "OK");
                    }else{
                        //System.out.println("Error creating product");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to create new brand.");
                    }
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
