package TEmPoS.Servlet.Departments;

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

public class DeleteDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public DeleteDepartmentServlet(){}

    public DeleteDepartmentServlet(H2Departments h2Departments, H2User h2User){
        this.h2Departments = h2Departments;
        this.h2User = h2User;

        requiredParams.put("targetDepartmentId", "String");
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


            String id = input.getString("targetDepartmentId");
            String requestUser = input.getString("requestUser");
            int deleteId = Integer.parseInt(id);

            if (h2User.isRegistered(requestUser)) {
                if (h2Departments.deleteDeparment(deleteId)) {
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");
                } else {
                    //System.out.println("Error deleting customer");
                    responseJson.put("response", "false");
                    responseJson.put("error", "Failed to delete Department.");
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
