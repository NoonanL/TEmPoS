package TEmPoS.Servlet.Departments;

import TEmPoS.Model.Brand;
import TEmPoS.Model.Department;
import TEmPoS.Util.RequestJson;
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

public class EditDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;

    public EditDepartmentServlet(){}

    public EditDepartmentServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
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
        String id = input.getString("id");
        String requestUser = input.getString("requestUser");

        Department department = new Department();
        department.setId(id);
        department.setDepartment(input.getString("department"));

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)) {
            try {
                if (h2Departments.existingDepartment(department.getDepartment())) {
                    responseJson.put("error", "Brand already exists!");
                } else {
                    if (h2Departments.editDepartment(department)) {
                        //System.out.println("New user created.");
                        responseJson.put("response", "OK");
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

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
