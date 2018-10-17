package TEmPoS.Servlet.Brands;

import TEmPoS.Util.Logger;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeleteBrandServlet extends HttpServlet {

    private H2Brands h2Brands;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public DeleteBrandServlet(){}

    public DeleteBrandServlet(H2Brands h2Brands, H2User h2User){
        this.h2Brands = h2Brands;
        this.h2User = h2User;

        requiredParams.put("targetBrandId", "integer");
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

                String id = input.getString("targetBrandId");
                String requestUser = input.getString("requestUser");
                int deleteId = Integer.parseInt(id);

                if (h2User.isRegistered(requestUser)) {
                    if (h2Brands.deleteBrand(deleteId)) {
                        //System.out.println("customer deleted.");
                        responseJson.put("response", "OK");
                        responseJson.put("error", "None");
                    } else {
                        //System.out.println("Error deleting customer");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to delete Brand.");
                    }
                }
            } else {
                //System.out.println("Error deleting customer");
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
