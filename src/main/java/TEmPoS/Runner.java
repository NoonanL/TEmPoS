package TEmPoS;

import TEmPoS.Servlet.*;
import TEmPoS.Servlet.Configuration.BranchesServlet;
import TEmPoS.Servlet.Customer.*;
import TEmPoS.Servlet.Product.CreateProductServlet;
import TEmPoS.Servlet.Product.DeleteProductServlet;
import TEmPoS.Servlet.Product.EditProductServlet;
import TEmPoS.Servlet.Product.GetProductsServlet;
import TEmPoS.Servlet.User.*;
import TEmPoS.db.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Runner {

    private H2User userDB;
    private H2Customer customerDB;
    private H2BranchList branchListDB;
    private H2Products productsDB;
    private static final int PORT = 9001;

    private Runner() {


        userDB = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
        customerDB = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));
        branchListDB = new H2BranchList(new ConnectionSupplier(ConnectionSupplier.FILE));
        productsDB = new H2Products(new ConnectionSupplier(ConnectionSupplier.FILE));


    }

    private void start() throws Exception {

        Server server = new Server(PORT);

        /*
        servlet handler controls the context, ie where web resources are located.
         */
        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "TEmPoS/src/main/resources/webapp");


        // LOGIN SERVLETS

        LoginServlet login = new LoginServlet(userDB);
        handler.addServlet(new ServletHolder(login), "/loginServlet");

        IsAdminServlet isAdminServlet = new IsAdminServlet(userDB);
        handler.addServlet(new ServletHolder(isAdminServlet), "/isAdminServlet");


        //USER SERVLETS

        CreateUserServlet createUserServlet = new CreateUserServlet(userDB);
        handler.addServlet(new ServletHolder(createUserServlet), "/createUserServlet");

        GetUsersServlet getUsersServlet = new GetUsersServlet(userDB);
        handler.addServlet(new ServletHolder(getUsersServlet), "/getUsersServlet");

        DeleteUserServlet deleteUserServlet = new DeleteUserServlet(userDB);
        handler.addServlet(new ServletHolder(deleteUserServlet), "/deleteUserServlet");

        EditUserServlet editUserServlet = new EditUserServlet(userDB);
        handler.addServlet(new ServletHolder(editUserServlet), "/editUserServlet");


        //CUSTOMER SERVLETS

        CreateCustomerServlet createCustomerServlet = new CreateCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(createCustomerServlet), "/createCustomerServlet");

        DeleteCustomerServlet deleteCustomerServlet = new DeleteCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(deleteCustomerServlet), "/deleteCustomerServlet");

        EditCustomerServlet editCustomerServlet = new EditCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(editCustomerServlet), "/editCustomerServlet");

        GetCustomersServlet getCustomersServlet = new GetCustomersServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(getCustomersServlet), "/getCustomersServlet");

        SearchCustomerServlet searchCustomerServlet = new SearchCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(searchCustomerServlet), "/searchCustomerServlet");


        //CONFIGURATION SERVLETS

        BranchesServlet branchesServlet = new BranchesServlet(branchListDB,userDB);
        handler.addServlet(new ServletHolder(branchesServlet), "/branchesServlet");


        //PRODUCT SERVLETS

        CreateProductServlet createProductServlet = new CreateProductServlet(productsDB,userDB);
        handler.addServlet(new ServletHolder(createProductServlet), "/createProductServlet");

        DeleteProductServlet deleteProductServlet = new DeleteProductServlet(productsDB,userDB);
        handler.addServlet(new ServletHolder(deleteProductServlet), "/deleteProductServlet");

        EditProductServlet editProductServlet = new EditProductServlet(productsDB,userDB);
        handler.addServlet(new ServletHolder(editProductServlet), "/editProductServlet");

        GetProductsServlet getProductsServlet = new GetProductsServlet(productsDB,userDB);
        handler.addServlet(new ServletHolder(getProductsServlet), "/getProductsServlet");

        /*
        sets default servlet path.
         */
        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");


        /*
        starts server
         */
        server.start();
        System.out.println("Server started, will run until terminated");
        server.join();
    }

    /*
    main program start here
    */
    public static void main(String[] args) {
        try {
            System.out.println("starting");
            new Runner().start();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


}
