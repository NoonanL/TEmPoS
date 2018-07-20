package TEmPoS.Servlet.Distributors;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Distributor;
import TEmPoS.Util.RequestJson;
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

public class EditDistributorServlet extends HttpServlet {

    private H2User h2User;
    private H2Distributors h2Distributors;

    public EditDistributorServlet(){}

    public EditDistributorServlet(H2Distributors h2Distributors, H2User h2User){
        this.h2Distributors = h2Distributors;
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
        String id = input.getString("targetDistributorId");
        String requestUser = input.getString("requestUser");

        Distributor distributor = new Distributor();
        distributor.setId(id);
        distributor.setName(input.getString("name"));

        //int editId = Integer.parseInt(id); - No longer needed if we pass the whole object eh?

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)) {
            try {
                if (h2Distributors.existingDistributor(distributor.getName())) {
                    responseJson.put("error", "Distributor already exists!");
                } else {
                    if (h2Distributors.editDistributor(distributor)) {
                        //System.out.println("New user created.");
                        responseJson.put("response", "OK");
                    } else {
                        //System.out.println("Error creating user");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Error editing Distributor.");
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
