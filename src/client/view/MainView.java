package client.view;

import client.controller.HistoryController;
import client.controller.InputFormController;
import client.controller.ResultsController;
import client.model.StudentModel;
import client.service.ApiService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import shared.PredictionResult;


public class MainView {

    private final ResultsController resultsCtrl;
    private final TabPane           tabPane;

    public MainView(StudentModel model, ApiService api) {
        resultsCtrl = new ResultsController();
        InputFormController inputCtrl   = new InputFormController(model, api, this);
        HistoryController   historyCtrl = new HistoryController(api);

        Tab tabInput   = new Tab("📝 Student Input",  inputCtrl.buildView());
        Tab tabResults = new Tab("🎯 Results",        resultsCtrl.buildView());
        Tab tabHistory = new Tab("📜 History",        historyCtrl.buildView());

        for (Tab t : new Tab[]{tabInput, tabResults, tabHistory}) t.setClosable(false);

        tabPane = new TabPane(tabInput, tabResults, tabHistory);
        tabPane.setStyle("-fx-font-size: 13px;");
        tabPane.setTabMinWidth(120);
    }

    
    public void showResults(PredictionResult result) {
        resultsCtrl.showResult(result);
        tabPane.getSelectionModel().select(1);
    }

   
    public BorderPane buildRoot(boolean serverEmbedded) {
        BorderPane root = new BorderPane();

       
        HBox header = buildHeader(serverEmbedded);
        root.setTop(header);
        root.setCenter(tabPane);

        
        Label footer = new Label("Student Prediction System  •  JavaFX + Servlet + Rule-Based AI  •  MVC Architecture");
        footer.setFont(Font.font("System", 11));
        footer.setTextFill(Color.web("#999"));
        footer.setPadding(new Insets(6, 16, 6, 16));
        root.setBottom(footer);

        return root;
    }

    private HBox buildHeader(boolean serverEmbedded) {
        HBox header = new HBox();
        header.setPadding(new Insets(14, 20, 14, 20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #1a237e;");

        Label appName = new Label("🎓 Student Prediction System");
        appName.setFont(Font.font("System", FontWeight.BOLD, 17));
        appName.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String modeText = serverEmbedded ? "🟢 Server: Embedded" : "🔵 Server: External";
        Label serverMode = new Label(modeText);
        serverMode.setFont(Font.font("System", 11));
        serverMode.setTextFill(Color.web("#90caf9"));

        header.getChildren().addAll(appName, spacer, serverMode);
        return header;
    }
}
