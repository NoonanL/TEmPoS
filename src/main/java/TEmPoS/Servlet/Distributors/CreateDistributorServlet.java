package TEmPoS.Servlet.Distributors;

import TEmPoS.Model.Brand;
import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2Brands;
import TEmPoS.db.H2Distributors;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CreateDistributorServlet extends HttpServlet {


    private H2Distributors h2Distributors;
    private H2User h2User;

    public CreateDistributorServlet(){}

    public CreateDistributorServlet(H2Distributors h2Distributors, H2User h2User){
        this.h2Distributors = h2Distributors;
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

        String requestUser = input.getString("requestUser");

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){

            try {
                if(h2Distributors.existingDistributor(input.getString("distributor"), "name")){
                    responseJson.put("response", "false");
                    responseJson.put("error", "Distributor already exists.");
                }else{
                    if(h2Distributors.createDistributor(input.getString("distributor"))){
                        responseJson.put("response", "OK");
                        responseJson.put("error", "None.");
                    }else{
                        //System.out.println("Error creating product");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to create new distributor.");
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
