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

public class CreateUserServlet extends HttpServlet {

    private H2User h2User;

    public CreateUserServlet(){}

    public CreateUserServlet(H2User h2User) {
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
        String newUsername = input.getString("username");
        String password = input.getString("password");
        String adminStatus = input.getString("isAdmin");
        System.out.println("Creating new user with username " + newUsername + ", admin status " + adminStatus + ".");

        if(h2User.register(newUsername,password,adminStatus)){
            System.out.println("New user created.");
            responseJson.put("auth", "OK");
        }else{
            System.out.println("Error creating user");
            responseJson.put("auth", "false");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
