package TEmPoS.Servlet.Transaction;

import TEmPoS.Model.Customer;
import TEmPoS.Model.Transaction;
import TEmPoS.Util.Logger;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Transactions;
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

public class CreateTransactionServlet extends HttpServlet {

    private H2Transactions h2Transactions;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public CreateTransactionServlet(){}

    public CreateTransactionServlet(H2Transactions h2Transactions, H2User h2User){
        this.h2Transactions = h2Transactions;
        this.h2User = h2User;

        requiredParams.put("customerId", "String");
        requiredParams.put("productId", "String");
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

                String requestUser = input.getString("requestUser");

                if (h2User.isRegistered(requestUser)) {

                    Transaction newTransaction = new Transaction();
                    newTransaction.setCustomerId(input.getString("customerId"));
                    newTransaction.setCustomerName(input.getString("customerName"));
                    newTransaction.setProductId(input.getString("productId"));
                    newTransaction.setProductName(input.getString("productName"));

                    if (h2Transactions.createTransaction(newTransaction)) {
                        responseJson.put("response", "OK");
                        responseJson.put("error", "None.");
                    } else {
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to create new transaction.");
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