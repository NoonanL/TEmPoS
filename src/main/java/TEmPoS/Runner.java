package TEmPoS;

import TEmPoS.Servlet.*;
import TEmPoS.Servlet.Brands.CreateBrandServlet;
import TEmPoS.Servlet.Brands.DeleteBrandServlet;
import TEmPoS.Servlet.Brands.EditBrandServlet;
import TEmPoS.Servlet.Brands.GetBrandsServlet;
import TEmPoS.Servlet.Configuration.BranchesServlet;
import TEmPoS.Servlet.Customer.*;
import TEmPoS.Servlet.Distributors.CreateDistributorServlet;
import TEmPoS.Servlet.Distributors.DeleteDistributorServlet;
import TEmPoS.Servlet.Product.*;
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
    private H2Departments departmentsDB;
    private H2Brands brandsDB;
    private H2Distributors distributorsDB;
    private static final int PORT = 9001;

    private Runner() {


        userDB = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
        customerDB = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));
        branchListDB = new H2BranchList(new ConnectionSupplier(ConnectionSupplier.FILE));
        productsDB = new H2Products(new ConnectionSupplier(ConnectionSupplier.FILE));
        departmentsDB = new H2Departments(new ConnectionSupplier(ConnectionSupplier.FILE));
        brandsDB = new H2Brands(new ConnectionSupplier(ConnectionSupplier.FILE));
        distributorsDB = new H2Distributors(new ConnectionSupplier(ConnectionSupplier.FILE));

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

        SearchProductsServlet searchProductsServlet = new SearchProductsServlet(productsDB,userDB);
        handler.addServlet(new ServletHolder(searchProductsServlet), "/searchProductsServlet");


        //BRANDS SERVLETS

        CreateBrandServlet createBrandServlet = new CreateBrandServlet(brandsDB,userDB);
        handler.addServlet(new ServletHolder(createBrandServlet), "/createBrandServlet");

        DeleteBrandServlet deleteBrandServlet = new DeleteBrandServlet(brandsDB,userDB);
        handler.addServlet(new ServletHolder(deleteBrandServlet), "/deleteBrandServlet");

        EditBrandServlet editBrandServlet = new EditBrandServlet(brandsDB,userDB);
        handler.addServlet(new ServletHolder(editBrandServlet), "/editBrandServlet");

        GetBrandsServlet getBrandsServlet = new GetBrandsServlet(brandsDB,userDB);
        handler.addServlet(new ServletHolder(getBrandsServlet), "/getBrandsServlet");


        //DISTRIBUTOR SERVLETS
        CreateDistributorServlet createDistributorServlet = new CreateDistributorServlet(distributorsDB,userDB);
        handler.addServlet(new ServletHolder(createDistributorServlet), "/createDistributorServlet");

        DeleteDistributorServlet deleteDistributorServlet = new DeleteDistributorServlet(distributorsDB,userDB);
        handler.addServlet(new ServletHolder(deleteDistributorServlet), "/deleteDistributorServlet");


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
