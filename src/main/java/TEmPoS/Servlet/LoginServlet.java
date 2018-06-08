package TEmPoS.Servlet;

import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet{

    private H2User h2User;

    public LoginServlet() {
    }

    public LoginServlet(H2User h2User) {
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Read from request
        JSONObject input = new RequestJson().parse(request);
        String username = input.getString("username");
        String password = input.getString("password");
        //System.out.println("User " + username + " attempting to log in.");

        JSONObject responseJson = new JSONObject();

        if(h2User.login(username,password)){
            //System.out.println("Login Successful");
            responseJson.put("auth", "OK");
        }else{
            //System.out.println("Login Failed");
            responseJson.put("auth", "false");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
