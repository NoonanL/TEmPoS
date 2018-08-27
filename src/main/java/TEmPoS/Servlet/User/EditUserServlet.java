package TEmPoS.Servlet.User;

import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
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

public class EditUserServlet extends HttpServlet {

    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();


    public EditUserServlet(){}

    public EditUserServlet(H2User h2User) {
        this.h2User = h2User;

        requiredParams.put("targetUser", "String");
        requiredParams.put("username", "String");
        requiredParams.put("isAdmin", "String");
        requiredParams.put("requestUser", "String");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //read from request
        JSONObject input = new RequestJson().parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            String requestUser = input.getString("requestUser");
            String targetUser = input.getString("targetUser");
            String newUsername = input.getString("username");
            String adminStatus = input.getString("isAdmin");
            //System.out.println("Editing user " + targetUser + "." +
            //"New username is " + newUsername + " and adminStatus is " + adminStatus);


            if (h2User.isAdmin(requestUser)) {
                if (h2User.editUser(targetUser, newUsername, adminStatus)) {
                    responseJson.put("response", "OK");
                    responseJson.put("error", "None.");
                } else {
                    responseJson.put("response", "false");
                    responseJson.put("error", "Error editing user.");
                }
            } else {
                //System.out.println("Request user NOT an admin - Access denied.");
                responseJson.put("response", "false");
                responseJson.put("error", "User is not an admin.");
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
