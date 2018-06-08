package TEmPoS.Servlet;

import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class IsAdminServlet extends HttpServlet {

    private H2User h2User;

    public IsAdminServlet(){}

    public IsAdminServlet(H2User h2User) {
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //read from request
        JSONObject input = new RequestJson().parse(request);
        String username = input.getString("username");
        //System.out.println("Is user " + username + " an Admin...");

        JSONObject responseJson = new JSONObject();
        if(h2User.isAdmin(username)){
            //System.out.println("Is an Admin");
            responseJson.put("isAdmin", "true");
        }else{
            //System.out.println("Is not an admin");
            responseJson.put("isAdmin", "false");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
