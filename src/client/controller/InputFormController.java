package client.controller;

import client.model.StudentModel;
import client.service.ApiService;
import client.view.MainView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import shared.PredictionResult;


public class InputFormController {

    private final StudentModel model;
    private final ApiService   api;
    private final MainView     mainView;

    // UI controls
    private TextField   tfName, tfEmail, tfMath, tfScience, tfEnglish;
    private Slider      slAttendance, slStudyHours;
    private Spinner<Integer> spAssignments, spFailures;
    private CheckBox    cbExtracurricular;
    private Label       lblAttVal, lblStudyVal, lblStatus;
    private Button      btnPredict;
    private VBox        root;

    public InputFormController(StudentModel model, ApiService api, MainView mainView) {
        this.model    = model;
        this.api      = api;
        this.mainView = mainView;
    }

    public ScrollPane buildView() {
        root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: #f8f9fa;");

        // Title
        Label title = new Label("📊 Student Performance Predictor");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1a237e"));

        Label subtitle = new Label("Enter student details to get AI-powered predictions");
        subtitle.setFont(Font.font("System", 13));
        subtitle.setTextFill(Color.web("#666"));

        // Form card
        VBox card = buildFormCard();

        // Status label
        lblStatus = new Label();
        lblStatus.setFont(Font.font("System", 13));
        lblStatus.setWrapText(true);

        // Predict button
        btnPredict = new Button("🔮  Predict Performance");
        btnPredict.setStyle("""
            -fx-background-color: #1a237e;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-padding: 12 30;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            """);
        btnPredict.setMaxWidth(Double.MAX_VALUE);
        btnPredict.setOnAction(e -> handlePredict());

        root.getChildren().addAll(title, subtitle, card, lblStatus, btnPredict);
        
        // Wrap in ScrollPane
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        return scrollPane;
    }

    private VBox buildFormCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setStyle("""
            -fx-background-color: #ffffff;
            -fx-background-radius: 16;
            -fx-effect: dropshadow(gaussian, rgba(26,35,126,0.15), 12, 0, 0, 4);
            -fx-border-color: #e8eaf6;
            -fx-border-radius: 16;
            -fx-border-width: 1;
            """);

        // Row 1: Name, Email
        tfName  = field("Full Name *");
        tfEmail = field("Email (optional)");
        HBox row1 = hrow(labeled("Student Name", tfName), labeled("Email", tfEmail));

        // Row 2: Subject marks
        tfMath    = field("0 – 100");
        tfScience = field("0 – 100");
        tfEnglish = field("0 – 100");
        HBox row2 = hrow(labeled("Maths Marks", tfMath),
                         labeled("Science Marks", tfScience),
                         labeled("English Marks", tfEnglish));

        // Attendance slider
        slAttendance = new Slider(0, 100, 75);
        slAttendance.setShowTickMarks(true);
        slAttendance.setMajorTickUnit(25);
        lblAttVal = new Label("75%");
        lblAttVal.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblAttVal.setTextFill(Color.web("#1a237e"));
        slAttendance.valueProperty().addListener((o, ov, nv) -> {
            lblAttVal.setText(String.format("%.0f%%", nv.doubleValue()));
        });
        HBox attRow = new HBox(10, slAttendance, lblAttVal);
        attRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(slAttendance, Priority.ALWAYS);

        // Study hours slider
        slStudyHours = new Slider(0, 10, 3);
        slStudyHours.setShowTickMarks(true);
        slStudyHours.setMajorTickUnit(2);
        lblStudyVal = new Label("3.0 hrs");
        lblStudyVal.setFont(Font.font("System", FontWeight.BOLD, 13));
        lblStudyVal.setTextFill(Color.web("#1a237e"));
        slStudyHours.valueProperty().addListener((o, ov, nv) -> {
            lblStudyVal.setText(String.format("%.1f hrs", nv.doubleValue()));
        });
        HBox studyRow = new HBox(10, slStudyHours, lblStudyVal);
        studyRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(slStudyHours, Priority.ALWAYS);

        // Assignments spinner
        spAssignments = new Spinner<>(0, 10, 8);
        spAssignments.setEditable(true);
        spAssignments.setPrefWidth(90);

        // Previous failures spinner
        spFailures = new Spinner<>(0, 5, 0);
        spFailures.setEditable(true);
        spFailures.setPrefWidth(90);

        // Extracurricular checkbox
        cbExtracurricular = new CheckBox("Has Extracurricular Activities");
        cbExtracurricular.setFont(Font.font("System", 13));

        HBox row3 = hrow(
            labeled("Assignments Submitted (out of 10)", spAssignments),
            labeled("Previous Year Failures", spFailures)
        );

        card.getChildren().addAll(
            row1,
            new Separator(),
            sectionLabel("📚 Academic Marks"),
            row2,
            new Separator(),
            sectionLabel("📅 Attendance & Study"),
            labeled("Attendance Percentage", attRow),
            labeled("Study Hours Per Day", studyRow),
            new Separator(),
            sectionLabel("📋 Other Details"),
            row3,
            cbExtracurricular
        );
        return card;
    }

    private void handlePredict() {
        // Always ensure button is enabled at start
        btnPredict.setDisable(false);
        
        // Validate
        String name = tfName.getText().trim();
        if (name.isEmpty()) {
            setStatus("⚠ Please enter the student's name.", "#e53935");
            return;
        }
        
        // Validate email if provided
        String email = tfEmail.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            setStatus("⚠ Please enter a valid email address (e.g., student@example.com).", "#e53935");
            return;
        }
        
        double math, science, english;
        try {
            math    = parseMarks(tfMath.getText(),    "Maths");
            science = parseMarks(tfScience.getText(), "Science");
            english = parseMarks(tfEnglish.getText(), "English");
        } catch (IllegalArgumentException e) {
            setStatus("⚠ " + e.getMessage(), "#e53935");
            return;
        }

        // Populate model
        model.setName(name);
        model.setEmail(tfEmail.getText().trim());
        model.setMath(math);
        model.setScience(science);
        model.setEnglish(english);
        model.setAttendance(slAttendance.getValue());
        model.setStudyHours(slStudyHours.getValue());
        model.setAssignments(spAssignments.getValue());
        model.setPreviousFailures(spFailures.getValue());
        model.setExtracurricular(cbExtracurricular.isSelected() ? 1 : 0);

        // Disable UI during request
        btnPredict.setDisable(true);
        setStatus("⏳ Analysing student data...", "#1565c0");

        // Run in background thread
        Thread.ofVirtual().start(() -> {
            try {
                PredictionResult result = api.predict(model.toStudentData());
                model.setLastResult(result);
                Platform.runLater(() -> {
                    btnPredict.setDisable(false);
                    setStatus("✅ Prediction complete for " + name, "#2e7d32");
                    mainView.showResults(result);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    btnPredict.setDisable(false);
                    setStatus("❌ Error: " + ex.getMessage(), "#c62828");
                });
            }
        });
    }

    private double parseMarks(String text, String subject) {
        try {
            double val = Double.parseDouble(text.trim());
            if (val < 0 || val > 100)
                throw new IllegalArgumentException(subject + " marks must be 0–100.");
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(subject + " marks must be a number.");
        }
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void setStatus(String msg, String color) {
        lblStatus.setText(msg);
        lblStatus.setStyle("-fx-text-fill: " + color + ";");
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-font-size: 13px;");
        return tf;
    }

    private Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, 13));
        l.setTextFill(Color.web("#37474f"));
        return l;
    }

    private VBox labeled(String label, javafx.scene.Node control) {
        Label l = new Label(label);
        l.setFont(Font.font("System", 12));
        l.setTextFill(Color.web("#555"));
        VBox box = new VBox(4, l, control);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private HBox hrow(javafx.scene.Node... nodes) {
        HBox row = new HBox(12, nodes);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}
