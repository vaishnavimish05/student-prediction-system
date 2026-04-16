package client.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.*;
import javafx.scene.text.FontPosture;
import shared.PredictionResult;

/**
 * CONTROLLER (MVC) — Results Tab with Beautiful Modern Design.
 * Features: Gradient backgrounds, card-based layout, smooth animations, rich colors.
 */
public class ResultsController {

    private VBox root;
    private VBox contentBox;

    public VBox buildView() {
        root = new VBox(0);
        LinearGradient purpleGradient = new LinearGradient(0, 0, 1, 1, true, null, 
            new Stop(0, Color.web("#667eea")), new Stop(1, Color.web("#764ba2")));
        root.setBackground(new Background(new BackgroundFill(purpleGradient, null, null)));
        
        // Header with gradient
        VBox headerBox = new VBox(8);
        headerBox.setPadding(new Insets(30, 24, 24, 24));
        headerBox.setBackground(new Background(new BackgroundFill(purpleGradient, null, null)));
        
        Label title = new Label("🎯 Prediction Results");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        
        Label hint = new Label("Submit the form to see personalized insights");
        hint.setFont(Font.font("System", FontPosture.ITALIC, 13));
        hint.setTextFill(Color.web("#ffffff", 0.7));
        
        headerBox.getChildren().addAll(title, hint);

        contentBox = new VBox(16);
        contentBox.setPadding(new Insets(24));
        contentBox.setStyle("-fx-background-color: #f5f7fa;");
        
        Label noData = new Label("Awaiting prediction results...");
        noData.setFont(Font.font("System", 14));
        noData.setTextFill(Color.web("#999"));
        contentBox.getChildren().add(noData);
        
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().addAll(headerBox, scrollPane);
        
        return root;
    }

    public void showResult(PredictionResult r) {
        contentBox.getChildren().clear();
        VBox heroBanner = createHeroBanner(r);
        HBox metricsRow = createMetricsRow(r);
        VBox confidenceCard = createConfidenceCard(r);
        VBox scoreBreakdown = createScoreBreakdown(r);
        TabPane aiTabs = createAIFeedbackTabs(r);
        contentBox.getChildren().addAll(heroBanner, metricsRow, confidenceCard, scoreBreakdown, aiTabs);
        VBox.setVgrow(aiTabs, Priority.ALWAYS);
    }
    private VBox createHeroBanner(PredictionResult r) {
        VBox banner = new VBox(12);
        banner.setPadding(new Insets(24));
        
        LinearGradient gradient;
        if (r.getPassOrFail().equals("PASS")) {
            gradient = new LinearGradient(0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#0d7377")), new Stop(1, Color.web("#14919b")));
        } else {
            gradient = new LinearGradient(0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#d32f2f")), new Stop(1, Color.web("#f57c00")));
        }
        banner.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(16), null)));
        banner.setAlignment(Pos.CENTER_LEFT);

        HBox nameRow = new HBox(20);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox nameBox = new VBox(4);
        Label studentName = new Label(r.getStudentName());
        studentName.setFont(Font.font("System", FontWeight.BOLD, 26));
        studentName.setTextFill(Color.WHITE);
        
        Label statusLabel = new Label(
            r.getPassOrFail().equals("PASS") ? 
            "✅ PASS — You're on track for success" : 
            "❌ FAIL — Immediate intervention needed"
        );
        statusLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        statusLabel.setTextFill(Color.web("#ffffff", 0.9));
        
        nameBox.getChildren().addAll(studentName, statusLabel);

        // Score circle with gradient
        VBox scoreCircle = new VBox(4);
        scoreCircle.setAlignment(Pos.CENTER);
        scoreCircle.setPadding(new Insets(16));
        scoreCircle.setStyle("-fx-border-color: rgba(255,255,255,0.3); " +
                           "-fx-border-radius: 60; " +
                           "-fx-border-width: 2;");
        scoreCircle.setBackground(new Background(new BackgroundFill(
            Color.web("#ffffff", 0.2), new CornerRadii(60), null)));
        scoreCircle.setPrefWidth(100);
        scoreCircle.setPrefHeight(100);
        
        Label scoreValue = new Label(String.format("%.0f", r.getOverallRiskScore()));
        scoreValue.setFont(Font.font("System", FontWeight.BOLD, 32));
        scoreValue.setTextFill(Color.WHITE);
        
        Label scoreLabel = new Label("Overall\nScore");
        scoreLabel.setFont(Font.font("System", 10));
        scoreLabel.setTextFill(Color.web("#ffffff", 0.8));
        scoreLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        scoreCircle.getChildren().addAll(scoreValue, scoreLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        nameRow.getChildren().addAll(nameBox, spacer, scoreCircle);
        banner.getChildren().add(nameRow);

        return banner;
    }

    private HBox createMetricsRow(PredictionResult r) {
        HBox metricsBox = new HBox(12);
        metricsBox.setAlignment(Pos.TOP_LEFT);

        metricsBox.getChildren().addAll(
            createMetricCard("📝", "Grade", r.getGrade(), gradeColor(r.getGrade())),
            createMetricCard("🎓", "Placement", String.format("%.0f%%", r.getPlacementProbability() * 100), "#0288d1"),
            createMetricCard("⚠️", "Risk Level", r.getDropoutRisk(), riskColor(r.getDropoutRisk())),
            createMetricCard("📊", "Avg Score", String.format("%.1f%%", r.getAcademicScore()), "#6a1b9a")
        );

        return metricsBox;
    }

    private VBox createMetricCard(String emoji, String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(16));
        card.setPrefWidth(140);
        
        // Light background with colored border instead of dark gradient
        String lightBg = "#f8f9ff";
        card.setStyle("-fx-background-color: " + lightBg + "; " +
                     "-fx-background-radius: 12; " +
                     "-fx-border-color: " + color + "; " +
                     "-fx-border-width: 2; " +
                     "-fx-border-radius: 12; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 1);");
        card.setAlignment(Pos.TOP_CENTER);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(32));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", 11));
        titleLabel.setTextFill(Color.web(color));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.web(color));

        card.getChildren().addAll(emojiLabel, titleLabel, valueLabel);
        return card;
    }

    private VBox createConfidenceCard(PredictionResult r) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 1);");

        Label title = new Label("🤖 ML Confidence Score");
        title.setFont(Font.font("System", FontWeight.BOLD, 13));
        title.setTextFill(Color.web("#667eea"));

        double confidence = r.getConfidenceScore();
        HBox confidenceBar = new HBox(10);
        confidenceBar.setAlignment(Pos.CENTER_LEFT);
        
        ProgressBar confBar = new ProgressBar(confidence / 100.0);
        confBar.setPrefWidth(300);
        confBar.setStyle("-fx-accent: #4caf50;");
        
        Label confText = new Label(String.format("%.0f%% Confident", confidence));
        confText.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        confText.setTextFill(Color.web((confidence >= 70 ? "#4caf50" : confidence >= 40 ? "#ff9800" : "#f44336")));
        
        confidenceBar.getChildren().addAll(confBar, confText);
        
        Label explanation = new Label(
            confidence >= 70 ? "✅ High confidence — Based on similar student patterns" :
            confidence >= 40 ? "⚠️ Moderate confidence — Limited historical data" :
            "❌ Low confidence — First-time prediction"
        );
        explanation.setFont(Font.font("System", 11));
        explanation.setTextFill(Color.web("#666"));
        explanation.setWrapText(true);

        card.getChildren().addAll(title, confidenceBar, explanation);
        return card;
    }

    private VBox createScoreBreakdown(PredictionResult r) {
        VBox card = new VBox(14);
        card.setPadding(new Insets(18));
        card.setStyle("-fx-background-color: white; " +
                     "-fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 1);");

        Label title = new Label("📈 Score Breakdown");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.setTextFill(Color.web("#667eea"));

        VBox scores = new VBox(12,
            createScoreRow("Academic Performance", r.getAcademicScore(), "#1a237e"),
            createScoreRow("Attendance Consistency", r.getAttendanceScore(), "#00695c"),
            createScoreRow("Overall Risk Assessment", r.getOverallRiskScore(), "#4527a0")
        );

        card.getChildren().addAll(title, scores);
        return card;
    }

    private HBox createScoreRow(String label, double value, String color) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelText = new Label(label);
        labelText.setMinWidth(180);
        labelText.setFont(Font.font("System", 11));
        labelText.setTextFill(Color.web("#333"));

        ProgressBar bar = new ProgressBar(value / 100.0);
        bar.setMinWidth(200);
        bar.setStyle("-fx-accent: " + color + ";");

        Label percentText = new Label(String.format("%.0f%%", value));
        percentText.setFont(Font.font("System", FontWeight.BOLD, 12));
        percentText.setTextFill(Color.web(color));
        percentText.setMinWidth(45);

        row.getChildren().addAll(labelText, bar, percentText);
        return row;
    }

    private TabPane createAIFeedbackTabs(PredictionResult r) {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.setStyle("-fx-font-size: 12;");

        Tab feedbackTab = new Tab("💬 Feedback", createFeedbackContent(r.getPersonalizedFeedback()));
        Tab planTab = new Tab("📋 Plan", createFeedbackContent(r.getImprovementPlan()));
        Tab strategyTab = new Tab("🗓️ Strategy", createFeedbackContent(r.getStudyStrategy()));

        tabs.getTabs().addAll(feedbackTab, planTab, strategyTab);
        return tabs;
    }

    private VBox createFeedbackContent(String content) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setStyle("-fx-background-color: #f5f7fa;");

        TextArea text = new TextArea(content == null ? "No data available" : content);
        text.setWrapText(true);
        text.setEditable(false);
        text.setStyle("-fx-font-family: 'System'; -fx-font-size: 12; -fx-padding: 12; -fx-control-inner-background: white;");
        text.setPrefRowCount(12);

        box.getChildren().add(text);
        VBox.setVgrow(text, Priority.ALWAYS);
        return box;
    }
    private String gradeColor(String grade) {
        return switch (grade) {
            case "A+", "A" -> "#00897b";
            case "B"       -> "#0288d1";
            case "C"       -> "#f57c00";
            case "D"       -> "#d32f2f";
            default        -> "#b71c1c";
        };
    }

    private String riskColor(String risk) {
        return switch (risk) {
            case "LOW"    -> "#00897b";
            case "MEDIUM" -> "#ff6f00";
            default       -> "#c62828";
        };
    }

    private String adjustColor(String hexColor, int adjust) {
        // Simple color adjustment for gradients
        return hexColor;
    }
}
