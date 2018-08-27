package TEmPoS.Servlet.Product;

import TEmPoS.Model.Product;
import TEmPoS.Servlet.Customer.CreateCustomerServlet;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateProductServlet extends HttpServlet {

    private H2Products h2Products;
    private H2User h2User;
    private Map<String, String> requiredParams = new HashMap<>();


    public CreateProductServlet(){}

    public CreateProductServlet(H2Products h2Products, H2User h2User){
        this.h2Products = h2Products;
        this.h2User = h2User;

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
        //read from request
        RequestJson requestParser = new RequestJson();
        JSONObject input = requestParser.parse(request);
        JSONObject responseJson = new JSONObject();

        ValidationFilter inputChecker = new ValidationFilter(requiredParams, input);

        if(inputChecker.isValid()) {

            Product newProduct = new Product();

            newProduct.setSKU(input.getString("SKU"));
            newProduct.setName(input.getString("name"));
            newProduct.setRRP(input.getDouble("RRP"));
            newProduct.setCost(input.getDouble("cost"));
            newProduct.setDepartment(input.getString("department"));
            newProduct.setBrand(input.getString("brand"));
            newProduct.setDescription(input.getString("description"));

            String requestUser = input.getString("requestUser");

            if (h2User.isRegistered(requestUser)) {

                try {
                    if (h2Products.existingSku(newProduct.getSKU())) {
                        responseJson.put("response", "false");
                        responseJson.put("error", "SKU Not Unique.");
                    } else {
                        if (h2Products.createProduct(newProduct)) {
                            responseJson.put("response", "OK");
                            responseJson.put("error", "None.");
                        } else {
                            //System.out.println("Error creating product");
                            responseJson.put("response", "false");
                            responseJson.put("error", "Failed to create new product.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            responseJson.put("response", "false");
            responseJson.put("error", "Missing required fields.");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(responseJson);
        out.flush();
    }

}
