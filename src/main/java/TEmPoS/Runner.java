package TEmPoS;

import TEmPoS.Servlet.CreateUserServlet;
import TEmPoS.Servlet.GetUsersServlet;
import TEmPoS.Servlet.IsAdminServlet;
import TEmPoS.Servlet.LoginServlet;
import TEmPoS.db.ConnectionSupplier;
import TEmPoS.db.H2User;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Runner {

    private H2User db;
    private static final int PORT = 9001;

    private Runner() {


        db = new H2User(new ConnectionSupplier(ConnectionSupplier.FILE));

    }

    private void start() throws Exception {

        Server server = new Server(PORT);

        /*
        servlet handler controls the context, ie where web resources are located.
         */
        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "TEmPoS/src/main/resources/webapp");


        LoginServlet login = new LoginServlet(db);
        handler.addServlet(new ServletHolder(login), "/loginServlet");

        IsAdminServlet isAdminServlet = new IsAdminServlet(db);
        handler.addServlet(new ServletHolder(isAdminServlet), "/isAdminServlet");

        CreateUserServlet createUserServlet = new CreateUserServlet(db);
        handler.addServlet(new ServletHolder(createUserServlet), "/createUserServlet");

        GetUsersServlet getUsersServlet = new GetUsersServlet(db);
        handler.addServlet(new ServletHolder(getUsersServlet), "/getUsersServlet");

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