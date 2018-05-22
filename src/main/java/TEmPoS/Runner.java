package TEmPoS;

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
