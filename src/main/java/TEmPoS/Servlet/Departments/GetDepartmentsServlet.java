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

public class GetDepartmentsServlet extends HttpServlet {

    private H2Departments h2Departments;
    private H2User h2User;

    public GetDepartmentsServlet(){}

    public GetDepartmentsServlet(H2Departments h2Departments, H2User h2User){
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
        String requestUser = input.getString("requestUser");


        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            responseJson = h2Departments.getDepartments();
            responseJson.put("response", "OK");
            responseJson.put("error", "None.");
        }


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
