package TEmPoS.Servlet.User;

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

        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        String requestUser = input.getString("requestUser");
        String newUsername = input.getString("username");
        String password = input.getString("password");
        String adminStatus = input.getString("isAdmin");
        //System.out.println("Creating new user with username " + newUsername + ", admin status " + adminStatus + ".");

        JSONObject responseJson = new JSONObject();
        if(h2User.isAdmin(requestUser)){
            if(h2User.register(newUsername,password,adminStatus)){
                responseJson.put("response", "OK");
                responseJson.put("error", "None.");
            }else{
                responseJson.put("response", "false");
                responseJson.put("error", "Error creating user.");
            }
        }


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
