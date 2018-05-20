package TEmPoS.Servlet;

import TEmPoS.db.H2User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

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
        String username = request.getParameter("userName");
        String password = request.getParameter("password");
        System.out.println("User " + username + " trying to log in with password " + password + ".");

    }

    private boolean doLogin(HttpServletRequest request, String userName, String password) throws IOException {
        if (h2User.login(userName, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", userName);
            return true;
        }
        return false;
    }
}
