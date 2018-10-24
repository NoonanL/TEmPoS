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

        requiredParams.put("goodsOrderId", "integer");
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
                String id = input.getString("goodsOrderId");
                String requestUser = input.getString("requestUser");
                int deleteId = Integer.parseInt(id);

                if (h2User.isRegistered(requestUser)) {
                    if (h2GoodsOrder.deleteGoodsOrderById(deleteId)) {
                        responseJson.put("response", "OK");
                        responseJson.put("error", "None.");
                    } else {
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to delete customer.");
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
