package TEmPoS.Servlet.Departments;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Department;
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
import java.util.Map;

public class CreateDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public CreateDepartmentServlet(){}

    public CreateDepartmentServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
        this.h2User = h2User;

        requiredParams.put("department", "String");
        requiredParams.put("requestUser", "String");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            Department newDepartment = new Department();
            newDepartment.setDepartment(input.getString("department"));
            String requestUser = input.getString("requestUser");

            if (h2User.isRegistered(requestUser)) {
                try {
                    if (h2Departments.existingDepartment(newDepartment.getDepartment())) {
                        responseJson.put("response", "false");
                        responseJson.put("error", "Department name not unique.");
                    } else {
                        if (h2Departments.createDepartment(newDepartment)) {
                            responseJson.put("response", "OK");
                            responseJson.put("error", "None.");
                        } else {
                            //System.out.println("Error creating product");
                            responseJson.put("response", "false");
                            responseJson.put("error", "Failed to create new department.");
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
