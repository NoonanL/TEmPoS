package TEmPoS.Servlet.PurchaseOrder;

import TEmPoS.Model.Brand;
import TEmPoS.Model.PurchaseOrder;
import TEmPoS.Servlet.Product.EditProductServlet;
import TEmPoS.Util.Logger;
import TEmPoS.Util.ValidationFilter;
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
import java.util.Iterator;
import java.util.Map;

public class EditPurchaseOrderServlet extends HttpServlet {

    private H2PurchaseOrder h2PurchaseOrder;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public EditPurchaseOrderServlet(){}

    public EditPurchaseOrderServlet(H2PurchaseOrder h2PurchaseOrder, H2User h2User){
        this.h2PurchaseOrder = h2PurchaseOrder;
        this.h2User = h2User;

        requiredParams.put("id", "String");
        requiredParams.put("productId", "String");
        requiredParams.put("SKU", "String");
        requiredParams.put("quantity", "String");
        requiredParams.put("branchId", "String");
        requiredParams.put("status", "String");
        requiredParams.put("UID", "String");
        requiredParams.put("requestUser", "String");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
                String oldVal = "";
                String requestUser = input.getString("requestUser");

                JSONObject oldValJson = h2PurchaseOrder.getPurchaseOrderByUID(Integer.parseInt(input.getString("id")));
                for (Iterator it = oldValJson.keys(); it.hasNext(); ) {
                    String json = it.next().toString();
                    if (!json.equals("connection") && !json.equals("error") && !json.equals("response")) {
                        JSONObject userJson = (oldValJson.getJSONObject(json));
                        oldVal = userJson.getString("id");

                    }
                }
                //System.out.println("Old value is " + oldVal + ", and new value will be " + newVal + ".");

                PurchaseOrder newPurchaseOrder = new PurchaseOrder();
                newPurchaseOrder.setId(input.getString("id"));
                newPurchaseOrder.setProductId(input.getString("productId"));
                newPurchaseOrder.setSKU(input.getString("SKU"));
                newPurchaseOrder.setQuantity(Integer.parseInt(input.getString("quantity")));
                newPurchaseOrder.setBranchId(input.getString("branchId"));
                newPurchaseOrder.setStatus(input.getString("status"));

                if (h2User.isRegistered(requestUser)) {
                    try {
                        if (h2PurchaseOrder.editPurchaseOrder(newPurchaseOrder)) {
                            responseJson.put("response", "OK");
                            responseJson.put("error", "None.");
                        } else {
                            //System.out.println("Error creating user");
                            responseJson.put("response", "false");
                            responseJson.put("error", "Error editing purchase order.");
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
