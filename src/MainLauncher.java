import client.model.StudentModel;
import client.service.ApiService;
import client.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.servlet.ServerMain;


public class MainLauncher extends Application {

    private static final int SERVER_PORT = 8080;
    private static boolean serverStarted = false;

    public static void main(String[] args) {
        // JavaFX requires main() to simply delegate to launch()
        launch(args);
    }

    @Override
    public void init() {
        // init() runs BEFORE the JavaFX thread — safe to start Tomcat here
        try {
            ServerMain.start(SERVER_PORT);
            serverStarted = true;
        } catch (Exception e) {
            System.err.println("[LAUNCHER] Could not start embedded server: " + e.getMessage());
            System.err.println("[LAUNCHER] App will attempt to connect to an external server.");
        }
    }

    @Override
    public void start(Stage stage) {
        String serverUrl = "http://localhost:" + SERVER_PORT;

        ApiService   api   = new ApiService(serverUrl);
        StudentModel model = new StudentModel();
        MainView     view  = new MainView(model, api);

        // Show a warning if server couldn't start
        if (!serverStarted) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING,
                "Embedded server failed to start.\n" +
                "Make sure port 8080 is free, or run a server separately.\n" +
                "The app will still open — connect manually.",
                javafx.scene.control.ButtonType.OK
            );
            alert.setHeaderText("Server Warning");
            alert.showAndWait();
        }

        Scene scene = new Scene(view.buildRoot(serverStarted), 1050, 720);
        stage.setTitle("Student Prediction System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }
}
