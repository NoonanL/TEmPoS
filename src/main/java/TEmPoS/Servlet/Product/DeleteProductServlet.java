package TEmPoS.Servlet.Product;

import TEmPoS.Model.Product;
import TEmPoS.Util.RequestJson;
import TEmPoS.db.H2Products;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DeleteProductServlet extends HttpServlet {

    private H2Products h2Products;
    private H2User h2User;

    public DeleteProductServlet(){}

    public DeleteProductServlet(H2Products h2Products, H2User h2User){
        this.h2Products = h2Products;
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

        String id = input.getString("targetProductId");
        String requestUser = input.getString("requestUser");
        int deleteId = Integer.parseInt(id);

        //System.out.println(deleteId);
        JSONObject responseJson = new JSONObject();
        if(h2User.isRegistered(requestUser)){
            if(h2Products.deleteProductById(deleteId)){
                responseJson.put("response", "OK");
                responseJson.put("error", "None.");
            }else{
                //System.out.println("Error deleting customer");
                responseJson.put("response", "false");
                responseJson.put("error", "Failed to delete product.");
            }
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }
}
