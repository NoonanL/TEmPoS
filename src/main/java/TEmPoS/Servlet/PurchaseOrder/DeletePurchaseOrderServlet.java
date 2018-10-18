package TEmPoS.Servlet.PurchaseOrder;

import TEmPoS.Util.Logger;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Brands;
import TEmPoS.db.H2PurchaseOrder;
import TEmPoS.db.H2User;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DeletePurchaseOrderServlet extends HttpServlet {
    private H2PurchaseOrder h2purchaseOrder;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public DeletePurchaseOrderServlet(){}

    public DeletePurchaseOrderServlet(H2PurchaseOrder h2purchaseOrder, H2User h2User){
        this.h2purchaseOrder = h2purchaseOrder;
        this.h2User = h2User;

        requiredParams.put("targetPurchaseOrderId", "integer");
        requiredParams.put("requestUser", "String");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
