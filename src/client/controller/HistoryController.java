package client.controller;

import client.service.ApiService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.List;
import java.util.Map;


public class HistoryController {

    private final ApiService api;
    private TableView<Map<String, Object>> table;
    private Label statsLabel;

    public HistoryController(ApiService api) {
        this.api = api;
    }

    @SuppressWarnings("unchecked")
    public VBox buildView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f8f9fa;");

        Label title = new Label("📊 Prediction History & Analytics");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a237e"));

        // Statistics box
        VBox statsBox = new VBox(12);
        statsBox.setPadding(new Insets(16));
        statsBox.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 1);
            -fx-border-color: #e8eaf6;
            -fx-border-radius: 10;
            -fx-border-width: 1;
            """);
        
        statsLabel = new Label("Loading statistics...");
        statsLabel.setFont(Font.font("System", 12));
        statsLabel.setTextFill(Color.web("#666"));
        statsLabel.setWrapText(true);
        statsBox.getChildren().add(statsLabel);

        Button btnRefresh = new Button("🔄  Refresh History");
        btnRefresh.setStyle("""
            -fx-background-color: #1a237e;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 10 24;
            -fx-background-radius: 6;
            -fx-cursor: hand;
            """);
        btnRefresh.setOnAction(e -> loadHistory());

        // Table
        table = new TableView<>();
        table.setPlaceholder(new Label("📭 No records yet — submit a prediction first."));
        table.setStyle("-fx-font-size: 11;" +
                       "-fx-control-inner-background: #fafafa;");
        VBox.setVgrow(table, Priority.ALWAYS);

        // Columns for prediction history
        table.getColumns().addAll(
            col("Student", "name", 120),
            col("Email", "email", 160),
            col("Grade", "predictedGrade", 70),
            col("Risk", "dropoutRisk", 80),
            col("Placement", "placementProbability", 90),
            col("Attendance", "attendancePercent", 90),
            col("Avg Marks", "mathMarks", 80),
            col("Assignments", "assignmentsSubmitted", 90),
            col("Study Hrs", "studyHoursPerDay", 80)
        );

        root.getChildren().addAll(title, statsBox, btnRefresh, table);
        
        // Load history on startup
        loadHistory();
        
        return root;
    }

    private void loadHistory() {
        statsLabel.setText("⏳ Loading statistics...");
        statsLabel.setTextFill(Color.web("#666"));
        
        Thread.ofVirtual().start(() -> {
            try {
                List<Map<String, Object>> history = api.getPredictionHistory();
                Map<String, String> insights = api.getInsights();
                
                Platform.runLater(() -> {
                    table.getItems().setAll(history != null ? history : List.of());
                    
                    // Update statistics
                    StringBuilder statsText = new StringBuilder("📈 ");
                    int count = 0;
                    if (insights != null) {
                        for (Map.Entry<String, String> entry : insights.entrySet()) {
                            if (count > 0) statsText.append(" | ");
                            statsText.append(entry.getKey()).append(": ").append(entry.getValue());
                            count++;
                        }
                    }
                    statsLabel.setText(statsText.toString());
                    statsLabel.setTextFill(Color.web("#2e7d32"));
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    statsLabel.setText("❌ Error loading: " + ex.getMessage());
                    statsLabel.setTextFill(Color.web("#c62828"));
                });
            }
        });
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private TableColumn<Map<String, Object>, Object> col(String header, String key, int width) {
        TableColumn<Map<String, Object>, Object> col = new TableColumn<>(header);
        col.setCellValueFactory(new MapValueFactory(key));
        col.setPrefWidth(width);
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { 
                    setText(null); 
                    return; 
                }
                if (item instanceof Double d) {
                    if (key.contains("Probability")) setText(String.format("%.0f%%", d * 100));
                    else if (key.contains("Percent") || key.contains("Marks")) setText(String.format("%.1f%%", d));
                    else setText(String.format("%.2f", d));
                }
                else setText(item.toString());
            }
        });
        return col;
    }
}
