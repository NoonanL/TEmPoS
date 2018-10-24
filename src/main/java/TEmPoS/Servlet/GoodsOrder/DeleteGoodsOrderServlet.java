package TEmPoS.Servlet.GoodsOrder;

import TEmPoS.Model.PurchaseOrder;
import TEmPoS.Util.Logger;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2GoodsOrder;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeleteGoodsOrderServlet extends HttpServlet {

    private H2GoodsOrder h2GoodsOrder;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public DeleteGoodsOrderServlet(){}

    public DeleteGoodsOrderServlet(H2GoodsOrder h2GoodsOrder, H2User h2User){
        this.h2GoodsOrder = h2GoodsOrder;
        this.h2User = h2User;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
