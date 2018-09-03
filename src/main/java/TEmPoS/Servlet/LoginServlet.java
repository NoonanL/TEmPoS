package TEmPoS.Servlet;

import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet{

    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();


    public LoginServlet() {
    }

    public LoginServlet(H2User h2User) {
        this.h2User = h2User;

        requiredParams.put("username", "String");
        requiredParams.put("password", "String");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            String username = input.getString("username");
            String password = input.getString("password");

            if (h2User.login(username, password)) {
                responseJson.put("auth", "OK");
                HttpSession session = request.getSession();
                System.out.println("User session: " + session.getId());
                session.setAttribute("user", username);
                session.setAttribute("mySession", session.getId());
                //setting session to expiry in 30 mins
                session.setMaxInactiveInterval(30*60);
                Cookie userName = new Cookie("user", username);
                userName.setMaxAge(30*60);
                response.addCookie(userName);
            } else {
                responseJson.put("auth", "false");
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
