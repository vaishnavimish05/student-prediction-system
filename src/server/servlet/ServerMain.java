package server.servlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import server.dao.DatabaseManager;
import java.io.File;
public class ServerMain {

    public static void start(int port) throws Exception {
        System.out.println("[SERVER] Starting Student Prediction Server on port " + port);
        DatabaseManager.initSchema();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector(); // trigger connector creation

        File docBase = new File(System.getProperty("java.io.tmpdir"), "student-prediction-webapp");
        docBase.mkdirs();
        Context ctx = tomcat.addContext("", docBase.getAbsolutePath());
        Tomcat.addServlet(ctx, "PredictionServlet", new PredictionServlet());
        ctx.addServletMappingDecoded("/api/*", "PredictionServlet");

        tomcat.start();
        System.out.println("[SERVER] Server started → http://localhost:" + port + "/api/health");
        
    }
}
