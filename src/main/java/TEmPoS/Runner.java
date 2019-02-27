package TEmPoS;

import TEmPoS.MQTT.Publisher;
import TEmPoS.MQTT.Subscriber;
import TEmPoS.Servlet.*;
import TEmPoS.Servlet.Brands.CreateBrandServlet;
import TEmPoS.Servlet.Brands.DeleteBrandServlet;
import TEmPoS.Servlet.Brands.EditBrandServlet;
import TEmPoS.Servlet.Brands.GetBrandsServlet;
import TEmPoS.Servlet.Configuration.BranchesServlet;
import TEmPoS.Servlet.Customer.*;
import TEmPoS.Servlet.Departments.CreateDepartmentServlet;
import TEmPoS.Servlet.Departments.DeleteDepartmentServlet;
import TEmPoS.Servlet.Departments.EditDepartmentServlet;
import TEmPoS.Servlet.Departments.GetDepartmentsServlet;
import TEmPoS.Servlet.Distributors.CreateDistributorServlet;
import TEmPoS.Servlet.Distributors.DeleteDistributorServlet;
import TEmPoS.Servlet.Distributors.EditDistributorServlet;
import TEmPoS.Servlet.Distributors.GetDistributorsServlet;
import TEmPoS.Servlet.GoodsOrder.CreateGoodsOrderServlet;
import TEmPoS.Servlet.GoodsOrder.DeleteGoodsOrderServlet;
import TEmPoS.Servlet.GoodsOrder.EditGoodsOrderServlet;
import TEmPoS.Servlet.GoodsOrder.GetGoodsOrderServlet;
import TEmPoS.Servlet.Product.*;
import TEmPoS.Servlet.PurchaseOrder.CreatePurchaseOrderServlet;
import TEmPoS.Servlet.PurchaseOrder.DeletePurchaseOrderServlet;
import TEmPoS.Servlet.PurchaseOrder.EditPurchaseOrderServlet;
import TEmPoS.Servlet.PurchaseOrder.GetPurchaseOrdersServlet;
import TEmPoS.Servlet.Stock.*;
import TEmPoS.Servlet.Transaction.CreateTransactionServlet;
import TEmPoS.Servlet.Transaction.GetTransactionsServlet;
import TEmPoS.Servlet.User.*;
import TEmPoS.Util.TextReader;
import TEmPoS.db.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.paho.client.mqttv3.MqttException;


import java.util.ArrayList;

public class Runner {

    private H2User userDB;
    private H2Customer customerDB;
    private H2BranchList branchListDB;
    private H2Products productsDB;
    private H2Departments departmentsDB;
    private H2Brands brandsDB;
    private H2Distributors distributorsDB;
    private H2Stock stockDB;
    private H2PurchaseOrder purchaseOrderDB;
    private H2GoodsOrder goodsOrderDB;
    private H2Transactions transactionsDB;
    private static final int PORT = 9001;
    public static ArrayList<String> ipWhiteList;
    public Publisher testPublisher;

//    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private Runner() throws MqttException {

        userDB = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
        customerDB = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));
        branchListDB = new H2BranchList(new ConnectionSupplier(ConnectionSupplier.FILE));
        productsDB = new H2Products(new ConnectionSupplier(ConnectionSupplier.FILE));
        departmentsDB = new H2Departments(new ConnectionSupplier(ConnectionSupplier.FILE));
        brandsDB = new H2Brands(new ConnectionSupplier(ConnectionSupplier.FILE));
        distributorsDB = new H2Distributors(new ConnectionSupplier(ConnectionSupplier.FILE));
        stockDB = new H2Stock(new ConnectionSupplier(ConnectionSupplier.FILE));
        purchaseOrderDB = new H2PurchaseOrder(new ConnectionSupplier(ConnectionSupplier.FILE));
        goodsOrderDB = new H2GoodsOrder(new ConnectionSupplier(ConnectionSupplier.FILE));
        transactionsDB = new H2Transactions(new ConnectionSupplier(ConnectionSupplier.FILE));
        ipWhiteList = TextReader.getValidIpList();
        testPublisher = new Publisher();
    }


    private void start() throws Exception {

//        logger.debug("Server started - Testing Debug!");
//        logger.error("Error test Yo");

//        Logger rollingFileLogger = LoggerFactory.getLogger("rollingFileLogger");
//        rollingFileLogger.info("Testing rolling file logger");

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

        GetCustomerByIdServlet getCustomerByIdServlet = new GetCustomerByIdServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(getCustomerByIdServlet), "/getCustomerByIdServlet");


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

        GetProductByIdServlet getProductByIdServlet = new GetProductByIdServlet(productsDB,userDB);
        handler.addServlet(new ServletHolder(getProductByIdServlet), "/getProductByIdServlet");


        //STOCK SERVLETS

        CreateStockServlet createStockServlet = new CreateStockServlet(stockDB,userDB);
        handler.addServlet(new ServletHolder(createStockServlet), "/createStockServlet");

        GetStockServlet getStockServlet = new GetStockServlet(stockDB,userDB);
        handler.addServlet(new ServletHolder(getStockServlet), "/getStockServlet");

        IncrementStockServlet incrementStockServlet = new IncrementStockServlet(stockDB,userDB);
        handler.addServlet(new ServletHolder(incrementStockServlet), "/incrementStockServlet");

        DecrementStockServlet decrementStockServlet = new DecrementStockServlet(stockDB,userDB);
        handler.addServlet(new ServletHolder(decrementStockServlet), "/decrementStockServlet");

        EditStockServlet editStockServlet = new EditStockServlet(stockDB,userDB);
        handler.addServlet(new ServletHolder(editStockServlet), "/editStockServlet");

        GetStockByBranchServlet getStockByBranchServlet = new GetStockByBranchServlet(stockDB,userDB);
        handler.addServlet(new ServletHolder(getStockByBranchServlet), "/getStockByBranchServlet");

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

        EditDistributorServlet editDistributorServlet = new EditDistributorServlet(distributorsDB,userDB);
        handler.addServlet(new ServletHolder(editDistributorServlet), "/editDistributorServlet");

        GetDistributorsServlet getDistributorsServlet = new GetDistributorsServlet(distributorsDB,userDB);
        handler.addServlet(new ServletHolder(getDistributorsServlet), "/getDistributorsServlet");


        //DEPARTMENTS SERVLETS
        CreateDepartmentServlet createDepartmentServlet = new CreateDepartmentServlet(departmentsDB,userDB);
        handler.addServlet(new ServletHolder(createDepartmentServlet), "/createDepartmentServlet");

        DeleteDepartmentServlet deleteDepartmentServlet = new DeleteDepartmentServlet(departmentsDB,userDB);
        handler.addServlet(new ServletHolder(deleteDepartmentServlet), "/deleteDepartmentServlet");

        EditDepartmentServlet editDepartmentServlet = new EditDepartmentServlet(departmentsDB,userDB);
        handler.addServlet(new ServletHolder(editDepartmentServlet), "/editDepartmentServlet");

        GetDepartmentsServlet getDepartmentsServlet = new GetDepartmentsServlet(departmentsDB,userDB);
        handler.addServlet(new ServletHolder(getDepartmentsServlet), "/getDepartmentsServlet");

        //PURCHASE ORDER SERVLETS
        CreatePurchaseOrderServlet createPurchaseOrderServlet = new CreatePurchaseOrderServlet(purchaseOrderDB,userDB);
        handler.addServlet(new ServletHolder(createPurchaseOrderServlet), "/createPurchaseOrderServlet");

        DeletePurchaseOrderServlet deletePurchaseOrderServlet = new DeletePurchaseOrderServlet(purchaseOrderDB,userDB);
        handler.addServlet(new ServletHolder(deletePurchaseOrderServlet), "/deletePurchaseOrderServlet");

        EditPurchaseOrderServlet editPurchaseOrderServlet = new EditPurchaseOrderServlet(purchaseOrderDB,userDB);
        handler.addServlet(new ServletHolder(editPurchaseOrderServlet), "/editPurchaseOrderServlet");

        GetPurchaseOrdersServlet getPurchaseOrdersServlet = new GetPurchaseOrdersServlet(purchaseOrderDB,userDB);
        handler.addServlet(new ServletHolder(getPurchaseOrdersServlet), "/getPurchaseOrdersServlet");


        //GOODS ORDER SERVLETS
        CreateGoodsOrderServlet createGoodsOrderServlet = new CreateGoodsOrderServlet(goodsOrderDB,userDB);
        handler.addServlet(new ServletHolder(createGoodsOrderServlet), "/createGoodsOrderServlet");

        GetGoodsOrderServlet getGoodsOrderServlet = new GetGoodsOrderServlet(goodsOrderDB,userDB);
        handler.addServlet(new ServletHolder(getGoodsOrderServlet), "/getGoodsOrderServlet");

        EditGoodsOrderServlet editGoodsOrderServlet = new EditGoodsOrderServlet(goodsOrderDB,userDB);
        handler.addServlet(new ServletHolder(editGoodsOrderServlet), "/editGoodsOrderServlet");

        DeleteGoodsOrderServlet deleteGoodsOrderServlet = new DeleteGoodsOrderServlet(goodsOrderDB,userDB);
        handler.addServlet(new ServletHolder(deleteGoodsOrderServlet), "/deleteGoodsOrderServlet");

        //TRANSACTION SERVLETS
        CreateTransactionServlet createTransactionServlet = new CreateTransactionServlet(transactionsDB,userDB);
        handler.addServlet(new ServletHolder(createTransactionServlet), "/createTransactionServlet");

        GetTransactionsServlet getTransactionsServlet = new GetTransactionsServlet(transactionsDB,userDB);
        handler.addServlet(new ServletHolder(getTransactionsServlet), "/getTransactionsServlet");


        /*
        sets default servlet path.
         */
        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");

        /**
         * Start MQTT Listener:
         *
         */
        System.out.println("MQTT Subscribers starting up...");
        Publisher publisher = new Publisher();
        Subscriber testSub = new Subscriber("Test", "TEmPoS_Server_Test", "debug");
        Subscriber transactionSub = new Subscriber("Transactions", "TEmPoS_Server_Transactions", "transaction", transactionsDB, userDB, publisher);
       // Publisher publisher = new Publisher();

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
