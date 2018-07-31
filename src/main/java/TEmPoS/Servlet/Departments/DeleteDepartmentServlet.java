package TEmPoS.Servlet.Departments;

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

public class DeleteDepartmentServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;

    public DeleteDepartmentServlet(){}

    public DeleteDepartmentServlet(H2Departments h2Departments, H2User h2User){
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

        String id = input.getString("targetDepartmentId");
        String requestUser = input.getString("requestUser");
        int deleteId = Integer.parseInt(id);

        //System.out.println(deleteId);
        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            if(h2Departments.deleteDeparment(deleteId)){
                responseJson.put("response", "OK");
                responseJson.put("error", "None.");
            }else{
                //System.out.println("Error deleting customer");
                responseJson.put("response", "false");
                responseJson.put("error", "Failed to delete Department.");
            }
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }


}
