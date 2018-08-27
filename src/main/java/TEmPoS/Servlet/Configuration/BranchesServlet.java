package TEmPoS.Servlet.Configuration;

import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2BranchList;
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

public class BranchesServlet extends HttpServlet {

    private H2BranchList h2BranchList;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public BranchesServlet(){}

    public BranchesServlet(H2BranchList h2BranchList, H2User h2User){
        this.h2BranchList = h2BranchList;
        this.h2User = h2User;

        requiredParams.put("requestUser", "String");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            String requestUser = input.getString("requestUser");

                if (h2User.isRegistered(requestUser)) {

                    responseJson = h2BranchList.getBranchList();
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");
            }
        }else {
            //System.out.println("Error deleting customer");
            responseJson.put("response", "false");
            responseJson.put("error", "Missing required fields.");
        }

        //System.out.println(responseJson);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();

    }
}
