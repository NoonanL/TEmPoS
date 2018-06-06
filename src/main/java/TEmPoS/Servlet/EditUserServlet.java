package TEmPoS.Servlet;

import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EditUserServlet extends HttpServlet {

    private H2User h2User;

    public EditUserServlet(){}

    public EditUserServlet(H2User h2User) {
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject responseJson = new JSONObject();

        // Read from request
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        //System.out.println(data);

        JSONObject input = new JSONObject(data);
        String requestUser = input.getString("requestUser");
        String targetUser = input.getString("targetUser");
        String newUsername = input.getString("username");
        String adminStatus = input.getString("isAdmin");
        System.out.println("Editing user " + targetUser + "." +
                "New username is " + newUsername + " and adminStatus is " + adminStatus);

        if(h2User.isAdmin(requestUser)){
            if(h2User.editUser(targetUser, newUsername, adminStatus)){
                System.out.println("User successfully edited");
                responseJson.put("response", "OK");
            }else{
                System.out.println("Error editing user");
                responseJson.put("response", "false");
            }
        }else{
            System.out.println("Request user NOT an admin - Access denied.");
            responseJson.put("response", "false");
        }


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
