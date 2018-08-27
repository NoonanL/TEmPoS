package TEmPoS.Servlet.Product;

import TEmPoS.Model.Product;
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

public class EditProductServlet extends HttpServlet {

    private H2Products h2Products;
    private H2User h2User;
    private ArrayList<String> requiredParams = new ArrayList<>();
    private Map<String, String> requiredMap = new HashMap<>();

    public EditProductServlet(){}

    public EditProductServlet(H2Products h2Products, H2User h2User){
        this.h2Products = h2Products;
        this.h2User = h2User;

//        requiredParams.add("id");
//        requiredParams.add("SKU");
//        requiredParams.add("name");
//        requiredParams.add("RRP");
//        requiredParams.add("cost");
//        requiredParams.add("department");
//        requiredParams.add("brand");
//        requiredParams.add("description");
//        requiredParams.add("requestUser");

        requiredMap.put("id", "integer");
        requiredMap.put("SKU", "String");
        requiredMap.put("name", "String");
        requiredMap.put("RRP", "double");
        requiredMap.put("cost", "double");
        requiredMap.put("department", "String");
        requiredMap.put("brand", "String");
        requiredMap.put("description", "String");
        requiredMap.put("requestUser", "String");

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

        ValidationFilter inputChecker = new ValidationFilter(requiredMap, input);

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
