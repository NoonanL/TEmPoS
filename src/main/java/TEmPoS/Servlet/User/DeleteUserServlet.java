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

public class DeleteUserServlet extends HttpServlet {

    private H2User h2User;

    public DeleteUserServlet(){}

    public DeleteUserServlet(H2User h2User){
        this.h2User = h2User;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        String targetUser = input.getString("targetUser");
        String requestUser = input.getString("requestUser");
        //System.out.println("User " + requestUser + " attempting to delete account with username " + targetUser + ".");

        JSONObject responseJson = new JSONObject();
        if(h2User.isAdmin(requestUser)){
            //System.out.println(requestUser + " is an Admin and may attempt to delete...");
            h2User.deleteUser(targetUser);
            responseJson.put("response", "OK");
            //System.out.println(targetUser + " deleted.");
        }else{
            //System.out.println(requestUser + " is not an admin.");
            responseJson.put("response", "false");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();

    }
}
