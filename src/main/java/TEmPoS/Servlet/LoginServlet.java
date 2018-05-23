package TEmPoS.Servlet;

import TEmPoS.db.H2User;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.Map;
import org.json.JSONObject;

public class LoginServlet extends HttpServlet{

    private H2User h2User;

    public LoginServlet() {
    }

    public LoginServlet(H2User h2User) {
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
        String username = input.getString("username");
        String password = input.getString("password");
        System.out.println("User " + username + " attempting to log in.");

        if(h2User.login(username,password)){
            System.out.println("Login Successful");
            responseJson.put("auth", "OK");
        }else{
            System.out.println("Login Failed");
            responseJson.put("auth", "false");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

    private boolean doLogin(HttpServletRequest request, String userName, String password) throws IOException {
        if (h2User.login(userName, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", userName);
            return true;
        }
        return false;
    }

    public JSONObject requestParamsToJSON(ServletRequest req) {
        JSONObject jsonObj = new JSONObject();
        Map<String,String[]> params = req.getParameterMap();
        for (Map.Entry<String,String[]> entry : params.entrySet()) {
            String v[] = entry.getValue();
            Object o = (v.length == 1) ? v[0] : v;
            jsonObj.put(entry.getKey(), o);
        }
        return jsonObj;
    }
}
