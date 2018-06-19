package TEmPoS;

import TEmPoS.Servlet.*;
import TEmPoS.Servlet.Customer.CreateCustomerServlet;
import TEmPoS.Servlet.Customer.DeleteCustomerServlet;
import TEmPoS.Servlet.Customer.EditCustomerServlet;
import TEmPoS.Servlet.Customer.GetCustomersServlet;
import TEmPoS.Servlet.User.*;
import TEmPoS.db.ConnectionSupplier;
import TEmPoS.db.H2Customer;
import TEmPoS.db.H2User;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Runner {

    private H2User userDB;
    private H2Customer customerDB;
    private static final int PORT = 9001;

    private Runner() {


        userDB = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));
        customerDB = new H2Customer(new ConnectionSupplier(ConnectionSupplier.FILE));

    }

    private void start() throws Exception {

        Server server = new Server(PORT);

        /*
        servlet handler controls the context, ie where web resources are located.
         */
        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "TEmPoS/src/main/resources/webapp");


        LoginServlet login = new LoginServlet(userDB);
        handler.addServlet(new ServletHolder(login), "/loginServlet");

        IsAdminServlet isAdminServlet = new IsAdminServlet(userDB);
        handler.addServlet(new ServletHolder(isAdminServlet), "/isAdminServlet");

        CreateUserServlet createUserServlet = new CreateUserServlet(userDB);
        handler.addServlet(new ServletHolder(createUserServlet), "/createUserServlet");

        GetUsersServlet getUsersServlet = new GetUsersServlet(userDB);
        handler.addServlet(new ServletHolder(getUsersServlet), "/getUsersServlet");

        DeleteUserServlet deleteUserServlet = new DeleteUserServlet(userDB);
        handler.addServlet(new ServletHolder(deleteUserServlet), "/deleteUserServlet");

        EditUserServlet editUserServlet = new EditUserServlet(userDB);
        handler.addServlet(new ServletHolder(editUserServlet), "/editUserServlet");

        CreateCustomerServlet createCustomerServlet = new CreateCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(createCustomerServlet), "/createCustomerServlet");

        DeleteCustomerServlet deleteCustomerServlet = new DeleteCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(deleteCustomerServlet), "/deleteCustomerServlet");

        EditCustomerServlet editCustomerServlet = new EditCustomerServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(editCustomerServlet), "/editCustomerServlet");

        GetCustomersServlet getCustomersServlet = new GetCustomersServlet(customerDB,userDB);
        handler.addServlet(new ServletHolder(getCustomersServlet), "/getCustomersServlet");

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
