package TEmPoS.Servlet.Stock;

import TEmPoS.Util.Logger;
import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Stock;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GetStockServlet extends HttpServlet {

    private H2Stock h2Stock;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public GetStockServlet(){}

    public GetStockServlet(H2Stock h2Stock, H2User h2User){
        this.h2Stock = h2Stock;
        this.h2User = h2User;

        requiredParams.put("productId", "integer");
        requiredParams.put("branchId", "String");
        requiredParams.put("requestUser", "String");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * Check request is authorised
         */
        if (!ValidationFilter.authorizedRequest(request)) {
            System.out.println("Unauthorised user request from " + request.getRemoteAddr());
            Logger.request("Unauthorised Request: " + request.getSession());
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED));
        } else {

            /**
             * Check input is valid
             * Must successfully convert to JSON
             * Must contain required Parameters
             */
            JSONObject input = ValidationFilter.isValid(request, requiredParams);
            JSONObject responseJson = new JSONObject();

            /**
             * If Verified input is not null:
             */
            if (input != null) {

                int productId = input.getInt("productId");
                String branchId = input.getString("branchId");
                String requestUser = input.getString("requestUser");

                if (h2User.isRegistered(requestUser)) {
                    int stockLevel = h2Stock.getStockLevel(productId, branchId);
                    responseJson.put("stockLevel", stockLevel);
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");

//                    responseJson.put("response", "false");
//                    responseJson.put("error", "Failed to create new stock listing.");

                }
            } else {
                responseJson.put("response", "false");
                responseJson.put("error", "Missing required fields.");
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseJson);
            out.flush();
        }
    }

}
