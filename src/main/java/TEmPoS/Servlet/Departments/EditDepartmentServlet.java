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

public class EditDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;
    private ArrayList<String> requiredParams = new ArrayList<>();

    public EditDepartmentServlet(){}

    public EditDepartmentServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
        this.h2User = h2User;

        requiredParams.add("id");
        requiredParams.add("department");
        requiredParams.add("requestUser");
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

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {


            String requestUser = input.getString("requestUser");

            Department department = new Department();
            department.setId(input.getString("id"));
            department.setDepartment(input.getString("department"));


            if (h2User.isRegistered(requestUser)) {
                try {
                    if (h2Departments.existingDepartment(department.getDepartment())) {
                        responseJson.put("response", "false");
                        responseJson.put("error", "Brand already exists!");
                    } else {
                        if (h2Departments.editDepartment(department)) {
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
