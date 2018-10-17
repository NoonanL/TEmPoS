package TEmPoS.Servlet.Product;

import TEmPoS.Model.Product;
import TEmPoS.Util.Logger;
import TEmPoS.Util.RequestJson;
import TEmPoS.Util.ValidationFilter;
import TEmPoS.db.H2Products;
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

public class EditProductServlet extends HttpServlet {

    private H2Products h2Products;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();

    public EditProductServlet(){}

    public EditProductServlet(H2Products h2Products, H2User h2User){
        this.h2Products = h2Products;
        this.h2User = h2User;

        requiredParams.put("id", "integer");
        requiredParams.put("SKU", "String");
        requiredParams.put("name", "String");
        requiredParams.put("RRP", "double");
        requiredParams.put("cost", "double");
        requiredParams.put("department", "String");
        requiredParams.put("brand", "String");
        requiredParams.put("description", "String");
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

                Product newProduct = new Product();
                newProduct.setSKU(input.getString("SKU"));
                newProduct.setName(input.getString("name"));
                newProduct.setRRP(input.getDouble("RRP"));
                newProduct.setCost(input.getDouble("cost"));
                newProduct.setDepartment(input.getString("department"));
                newProduct.setBrand(input.getString("brand"));
                newProduct.setDescription(input.getString("description"));

                String requestUser = input.getString("requestUser");
                String targerProductId = input.getString("id");
                int editId = Integer.parseInt(targerProductId);


                if (h2User.isRegistered(requestUser)) {

                    if (h2Products.editProduct(editId, newProduct)) {
                        responseJson.put("response", "OK");
                        responseJson.put("error", "None.");
                    } else {
                        //System.out.println("Error creating product");
                        responseJson.put("response", "false");
                        responseJson.put("error", "Failed to edit product.");
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
