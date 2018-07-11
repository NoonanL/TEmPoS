package TEmPoS.Servlet.Configuration;

import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2BranchList;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BranchesServlet extends HttpServlet {

    private H2BranchList h2BranchList;
    private H2User h2User;

    public BranchesServlet(){}

    public BranchesServlet(H2BranchList h2BranchList, H2User h2User){
        this.h2BranchList = h2BranchList;
        this.h2User = h2User;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        String requestUser = input.getString("requestUser");

        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)) {
            responseJson = h2BranchList.getBranchList();
        }


        //System.out.println(responseJson);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();

    }
}
