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

public class DeleteUserServlet extends HttpServlet {

    private H2User h2User;

    public DeleteUserServlet(){}

    public DeleteUserServlet(H2User h2User){
        this.h2User = h2User;
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
        String targetUser = input.getString("targetUser");
        String requestUser = input.getString("requestUser");
        //String password = input.getString("password");
        System.out.println("User " + requestUser + " attempting to delete account with username " + targetUser + ".");
        if(h2User.isAdmin(requestUser)){
            System.out.println(requestUser + " is an Admin and may attempt to delete...");
            h2User.deleteUser(targetUser);
            responseJson.put("response", "OK");
            System.out.println(targetUser + " deleted.");
        }else{
            System.out.println(requestUser + " is not an admin.");
            responseJson.put("response", "false");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();

    }
}
