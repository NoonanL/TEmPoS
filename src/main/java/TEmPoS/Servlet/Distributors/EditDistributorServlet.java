package TEmPoS.Servlet.Distributors;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Distributor;
import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditDistributorServlet extends HttpServlet {

    private H2User h2User;
    private H2Distributors h2Distributors;
    private Map<String, String> requiredParams = new HashMap<>();

    public EditDistributorServlet(){}

    public EditDistributorServlet(H2Distributors h2Distributors, H2User h2User){
        this.h2Distributors = h2Distributors;
        this.h2User = h2User;

        requiredParams.put("id", "integer");
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

            String id = input.getString("id");
            String requestUser = input.getString("requestUser");

            JSONObject oldValJson = h2Distributors.getDistributor(Integer.parseInt(id));
            for (Iterator it = oldValJson.keys(); it.hasNext(); ) {
                String json = it.next().toString();
                if(!json.equals("connection") && !json.equals("error") && !json.equals("response")) {
                    JSONObject userJson = (oldValJson.getJSONObject(json));
                    oldVal = userJson.getString("name");

                }
            }
            //System.out.println("Old value is " + oldVal + ", and new value will be " + newVal + ".");

            Distributor distributor = new Distributor();
            distributor.setId(id);
            distributor.setName(input.getString("distributor"));

            if (h2User.isRegistered(requestUser)) {
                try {
                    if (h2Distributors.existingDistributor(distributor.getName(), "distributor")) {
                        responseJson.put("response", "false");
                        responseJson.put("error", "Distributor already exists!");
                    } else {
                        if (h2Distributors.editDistributor(distributor) && h2Distributors.propagate(oldVal, distributor)) {
                            //System.out.println("New user created.");
                            responseJson.put("response", "OK");
                            responseJson.put("error", "None.");
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
