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

public class CreateDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;

    public CreateDepartmentServlet(){}

    public CreateDepartmentServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
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
        Department newDepartment = new Department();

        newDepartment.setDepartment(input.getString("department"));

        String requestUser = input.getString("requestUser");

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            try {
                if(h2Departments.existingDepartment(newDepartment.getDepartment())){
                    responseJson.put("response", "false");
                    responseJson.put("error", "Department name not unique.");
                }else{
                    if(h2Departments.createDepartment(newDepartment)){
                        //System.out.println("New product created.");
                        responseJson.put("response", "OK");
                    }else{
                        //System.out.println("Error creating product");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to create new department.");
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
