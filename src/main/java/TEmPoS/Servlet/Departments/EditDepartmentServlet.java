package TEmPoS.Servlet.Departments;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Department;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public EditDepartmentServlet(){}

    public EditDepartmentServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
        this.h2User = h2User;

        requiredParams.put("id", "integer");
        requiredParams.put("department", "String");
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
                String oldVal = "";
                String requestUser = input.getString("requestUser");

                JSONObject oldValJson = h2Departments.getDepartment(Integer.parseInt(input.getString("id")));
                for (Iterator it = oldValJson.keys(); it.hasNext(); ) {
                    String json = it.next().toString();
                    if (!json.equals("connection") && !json.equals("error") && !json.equals("response")) {
                        JSONObject userJson = (oldValJson.getJSONObject(json));
                        oldVal = userJson.getString("department");

                    }
                }

                Department department = new Department();
                department.setId(input.getString("id"));
                department.setDepartment(input.getString("department"));


                if (h2User.isRegistered(requestUser)) {
                    try {
                        if (h2Departments.existingDepartment(department.getDepartment())) {
                            responseJson.put("response", "false");
                            responseJson.put("error", "Brand already exists!");
                        } else {
                            if (h2Departments.editDepartment(department) && h2Departments.propagate(oldVal, department)) {
                                responseJson.put("response", "OK");
                                responseJson.put("error", "None.");
                            } else {
                                //System.out.println("Error creating user");
                                responseJson.put("response", "false");
                                responseJson.put("error", "Error editing Department.");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
