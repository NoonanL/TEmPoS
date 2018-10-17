package TEmPoS.Servlet.Departments;

import TEmPoS.Util.Logger;
import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Departments;
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

public class GetDepartmentsServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public GetDepartmentsServlet(){}

    public GetDepartmentsServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
        this.h2User = h2User;

        requiredParams.put("requestUser", "String");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

                String requestUser = input.getString("requestUser");

                if (h2User.isRegistered(requestUser)) {
                    responseJson = h2Departments.getDepartments();
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");
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
