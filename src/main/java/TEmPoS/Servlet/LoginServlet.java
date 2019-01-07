package TEmPoS.Servlet;


import TEmPoS.Runner;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet{

    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    public LoginServlet() {
    }

    public LoginServlet(H2User h2User) {
        this.h2User = h2User;
        requiredParams.put("username", "String");
        requiredParams.put("password", "String");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("User attempting to login.");
        /**
         * Check request is authorised
         */
        if (!ValidationFilter.authorizedRequest(request)) {
            System.out.println("Unauthorised user request from " + request.getRemoteAddr());
            //Logger.request("Unauthorised Request: " + request.getSession());
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED));
            JSONObject responseJson = new JSONObject();
            responseJson.put("auth", "false");
            responseJson.put("response", "false");
        }

        else {

            /**
             * Check input is valid
             * Must successfully convert to JSON
             * Must contain required Parameters
             */
            JSONObject input = ValidationFilter.isValid(request,requiredParams);
            JSONObject responseJson = new JSONObject();

            /**
             * If Verified input is not null:
             */
            if (input != null) {
                String username = input.getString("username");
                String password = input.getString("password");

                if (h2User.login(username, password)) {
                    responseJson.put("auth", "OK");

                    HttpSession session = request.getSession();
                    session.setAttribute("user", username);
                    session.setAttribute("mySession", session.getId());
                    //setting session expiry
                    session.setMaxInactiveInterval(30 * 1);
                    Cookie sessionId = new Cookie("session", session.getId());
                    sessionId.setMaxAge(30 * 1);
                    response.addCookie(sessionId);
                    //System.out.println(responseJson);
                    //Logger.login("User logged in: " + session.getAttribute("user").toString() + " " + session.getAttribute("mySession").toString());

                } else {
                    //System.out.println(responseJson);
                    responseJson.put("auth", "false");
                }

            } else {
                responseJson.put("response", "false");
                responseJson.put("error", "Missing required fields.");
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(responseJson);
            out.flush();

        }
    }

}
