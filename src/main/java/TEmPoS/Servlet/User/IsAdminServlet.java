package TEmPoS.Servlet.User;

import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IsAdminServlet extends HttpServlet {

    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public IsAdminServlet(){}

    public IsAdminServlet(H2User h2User) {
        this.h2User = h2User;

        requiredParams.put("username", "String");
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

            String requestUser = input.getString("requestUser");
            String username = input.getString("username");

            if (h2User.isAdmin(requestUser)) {
                if (h2User.isAdmin(username)) {
                    //System.out.println("Is an Admin");
                    responseJson.put("response", "OK");
                    responseJson.put("isAdmin", "true");
                    responseJson.put("error", "None.");
                } else {
                    //System.out.println("Is not an admin");
                    responseJson.put("response", "false");
                    responseJson.put("error", "None.");
                    responseJson.put("isAdmin", "false");
                }
            } else {
                responseJson.put("response", "false");
                responseJson.put("error", "Request user is not an admin.");
            }
        }else {
            responseJson.put("response", "false");
            responseJson.put("error", "Missing required fields.");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
