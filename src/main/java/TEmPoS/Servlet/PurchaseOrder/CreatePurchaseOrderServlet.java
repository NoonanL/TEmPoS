package TEmPoS.Servlet.PurchaseOrder;

import TEmPoS.Model.Brand;
import TEmPoS.Model.PurchaseOrder;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CreatePurchaseOrderServlet extends HttpServlet {

    private H2PurchaseOrder h2PurchaseOrder;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public CreatePurchaseOrderServlet(){}

    public CreatePurchaseOrderServlet(H2PurchaseOrder h2PurchaseOrder, H2User h2User){
        this.h2PurchaseOrder = h2PurchaseOrder;
        this.h2User = h2User;

        requiredParams.put("productId", "String");
        requiredParams.put("SKU", "String");
        requiredParams.put("quantity", "String");
        requiredParams.put("branchId", "String");
        requiredParams.put("UID", "String");
        requiredParams.put("requestUser", "String");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * Check request is authorised
         */
        if (!ValidationFilter.authorizedRequest(request)) {
            System.out.println("Unauthorised user request from " + request.getRemoteAddr());
            Logger.request("Unauthorised Request: " + request.getSession());
            response.sendError((HttpServletResponse.SC_UNAUTHORIZED));
        } else {

            /**
             * Check input is valid
             * Must successfully convert to JSON
             * Must contain required Parameters
             */
            JSONObject input = ValidationFilter.isValid(request, requiredParams);
            JSONObject responseJson = new JSONObject();

            /**
             * If Verified input is not null:
             */
            if (input != null) {

                PurchaseOrder newOrder = new PurchaseOrder();
                newOrder.setProductId(input.getString("productId"));
                newOrder.setSKU(input.getString("SKU"));
                newOrder.setQuantity(Integer.parseInt(input.getString("quantity")));
                newOrder.setBranchId(input.getString("branchId"));
                newOrder.setStatus("Created");

                String requestUser = input.getString("requestUser");

                if (h2User.isRegistered(requestUser)) {
                    try {
                        if (h2PurchaseOrder.existingOrder(newOrder.getUID())) {
                            responseJson.put("response", "false");
                            responseJson.put("error", "Purchase Order not unique.");
                        } else {
                            if (h2PurchaseOrder.createPurchaseOrder(newOrder)) {
                                responseJson.put("response", "OK");
                                responseJson.put("error", "None.");
                            } else {
                                responseJson.put("response", "false");
                                responseJson.put("error", "Failed to create new purchase order.");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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