package client.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.Map;

/**
 * HISTORY VIEW CONTROLLER — Beautiful display of all past predictions
 * Shows detailed records with filtering and statistics
 */
public class HistoryViewController {
    
    private VBox root;
    private TableView<Map<String, String>> table;
    private Label statsLabel;
    
    public ScrollPane buildView() {
        root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");
        
        // ── Header ────────────────────────────────────────────────────────
        VBox headerBox = createHeaderBox();
        
        // ── Stats Cards ────────────────────────────────────────────────────
        HBox statsBox = createStatsBox();
        
        // ── History Table ─────────────────────────────────────────────────
        VBox tableBox = createTableBox();
        
        root.getChildren().addAll(headerBox, statsBox, tableBox);
        
        // Wrap in ScrollPane
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        return scrollPane;
    }
    
    private VBox createHeaderBox() {
        VBox header = new VBox(8);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: rgba(255,255,255,0.95); " +
                       "-fx-background-radius: 12; " +
                       "-fx-border-radius: 12;");
        
        Label title = new Label("📊 Prediction History");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#667eea"));
        
        Label subtitle = new Label("View all student predictions and track patterns");
        subtitle.setFont(Font.font("System", 13));
        subtitle.setTextFill(Color.web("#666"));
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private HBox createStatsBox() {
        HBox stats = new HBox(12);
        stats.setAlignment(Pos.CENTER_LEFT);
        
        stats.getChildren().addAll(
            createStatCard("👥 Total Students", "0", "#667eea"),
            createStatCard("📈 Avg Attendance", "0%", "#00c853"),
            createStatCard("⚠️ At Risk", "0%", "#ff5252"),
            createStatCard("✅ Pass Rate", "0%", "#00897b")
        );
        
        return stats;
    }
    
    private VBox createStatCard(String label, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle("-fx-background-color: " + color + "; " +
                     "-fx-background-radius: 12; " +
                     "-fx-border-radius: 12;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(180);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("System", 12));
        labelText.setTextFill(Color.web("#ffffff", 0.8));
        
        Label valueText = new Label(value);
        valueText.setFont(Font.font("System", FontWeight.BOLD, 20));
        valueText.setTextFill(Color.WHITE);
        
        card.getChildren().addAll(labelText, valueText);
        return card;
    }
    
    private VBox createTableBox() {
        VBox tableContainer = new VBox(12);
        tableContainer.setPadding(new Insets(20));
        tableContainer.setStyle("-fx-background-color: rgba(255,255,255,0.95); " +
                               "-fx-background-radius: 12; " +
                               "-fx-border-radius: 12;");
        
        // ── Search & Filter ────────────────────────────────────────────
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name or email...");
        searchField.setPrefWidth(250);
        searchField.setStyle("-fx-padding: 8; -fx-font-size: 12;");
        
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.setItems(FXCollections.observableArrayList("All", "PASS", "FAIL", "HIGH Risk", "LOW Risk"));
        filterCombo.setValue("All");
        filterCombo.setPrefWidth(150);
        
        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-padding: 8 16; -fx-background-color: #667eea; -fx-text-fill: white; " +
                           "-fx-font-size: 12; -fx-border-radius: 6;");
        
        filterBox.getChildren().addAll(
            new Label("Filter:"), searchField, filterCombo, refreshBtn
        );
        
        // ── Table ──────────────────────────────────────────────────────
        table = new TableView<>();
        table.setPrefHeight(400);
        table.setStyle("-fx-font-size: 11;");
        
        createTableColumns();
        
        tableContainer.getChildren().addAll(filterBox, table);
        return tableContainer;
    }
    
    private void createTableColumns() {
        // Name column
        TableColumn<Map<String, String>, String> nameCol = new TableColumn<>("Student Name");
        nameCol.setPrefWidth(120);
        nameCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            p.getValue().getOrDefault("name", "")));
        
        // Email column
        TableColumn<Map<String, String>, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(150);
        emailCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            p.getValue().getOrDefault("email", "")));
        
        // Grade column
        TableColumn<Map<String, String>, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setPrefWidth(60);
        gradeCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            p.getValue().getOrDefault("predictedGrade", "")));
        
        // Marks column
        TableColumn<Map<String, String>, String> marksCol = new TableColumn<>("Avg Marks");
        marksCol.setPrefWidth(80);
        marksCol.setCellValueFactory(p -> {
            String math = p.getValue().getOrDefault("mathMarks", "0");
            String sci = p.getValue().getOrDefault("scienceMarks", "0");
            String eng = p.getValue().getOrDefault("englishMarks", "0");
            return new javafx.beans.property.SimpleStringProperty(
                String.format("%.1f%%", (Double.parseDouble(math) + Double.parseDouble(sci) + Double.parseDouble(eng)) / 3));
        });
        
        // Attendance column
        TableColumn<Map<String, String>, String> attCol = new TableColumn<>("Attendance");
        attCol.setPrefWidth(80);
        attCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.0f%%", Double.parseDouble(p.getValue().getOrDefault("attendancePercent", "0")))));
        
        // Risk column
        TableColumn<Map<String, String>, String> riskCol = new TableColumn<>("Dropout Risk");
        riskCol.setPrefWidth(100);
        riskCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            p.getValue().getOrDefault("dropoutRisk", "")));
        
        // Pass/Fail column
        TableColumn<Map<String, String>, String> passCol = new TableColumn<>("Status");
        passCol.setPrefWidth(70);
        passCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            p.getValue().getOrDefault("passOrFail", "")));
        
        // Placement column
        TableColumn<Map<String, String>, String> placementCol = new TableColumn<>("Placement");
        placementCol.setPrefWidth(80);
        placementCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.0f%%", Double.parseDouble(p.getValue().getOrDefault("placementProbability", "0")) * 100)));
        
        table.getColumns().addAll(nameCol, emailCol, gradeCol, marksCol, attCol, riskCol, passCol, placementCol);
    }
    
    public void loadHistoryData(List<?> records) {
        // Convert records to displayable format
        // Implementation depends on data structure
    }
    
    public void updateStats(Map<String, String> insights) {
        // Update stat cards with real data
    }
}
