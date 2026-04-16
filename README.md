# Student Prediction System
**JavaFX + Servlet (Tomcat Embedded) + Rule-Based AI + MySQL/JDBC — MVC Architecture**

---

## How to Run

### Quick Start (no database)
The app works without MySQL — predictions still work, but data won't be saved.

**Linux/Mac:**
```bash
chmod +x run.sh
./run.sh
```

**Windows:**
Double-click `run.bat`

**Or directly:**
```bash
java --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
     -jar student-prediction.jar
```

---

### With MySQL Database

1. Create the database:
```sql
CREATE DATABASE student_prediction_db;
```

2. Run with DB credentials:
```bash
# Linux/Mac
./run.sh --db-user=root --db-password=yourpassword

# Windows (edit run.bat and set -Ddb.password=yourpassword)

# Direct
java --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
     -Ddb.url=jdbc:mariadb://localhost:3306/student_prediction_db?useSSL=false \
     -Ddb.user=root \
     -Ddb.password=yourpassword \
     -jar student-prediction.jar
```

The schema (students table) is created automatically on first run.

---

## Requirements
- **Java 17+** (with JavaFX support, or OpenJFX installed)
- **MySQL or MariaDB** (optional — for persistent storage)

Check Java version: `java -version`

---

## Architecture (MVC)

```
┌─────────────────────────────────────────────────────────────┐
│                    JavaFX CLIENT (View + Controller)         │
│  InputFormController → StudentModel → ApiService (HTTP)     │
│  ResultsController   ← PredictionResult ←                   │
│  HistoryController   ← Student List    ←                    │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP JSON (port 8080)
┌──────────────────────────▼──────────────────────────────────┐
│              SERVLET BACKEND (Embedded Tomcat 10)           │
│  PredictionServlet → PredictionService → StudentAIEngine    │
│                                        → StudentDAO (JDBC)  │
└─────────────────────────────────────────────────────────────┘
                           │
                    MySQL/MariaDB
```

**The "AI" is rule-based** — no external API, no internet needed:
- Classifies students into 6 profile types (High Achiever, Consistent, Struggling, At-Risk, etc.)
- Generates personalised feedback, improvement plans, and study strategies
- All based on weighted scoring and pattern matching — runs offline

---

## Source Code Structure
```
src/
├── MainLauncher.java              ← Entry point (boots Tomcat + JavaFX)
├── shared/
│   ├── StudentData.java           ← Input DTO
│   └── PredictionResult.java      ← Output DTO
├── server/
│   ├── model/Student.java         ← DB entity
│   ├── dao/DatabaseManager.java   ← JDBC connection
│   ├── dao/StudentDAO.java        ← JDBC CRUD
│   ├── ai/StudentAIEngine.java    ← Rule-based AI
│   ├── service/PredictionService.java ← Business logic
│   └── servlet/
│       ├── PredictionServlet.java ← Jakarta Servlet (Controller)
│       └── ServerMain.java        ← Embedded Tomcat boot
└── client/
    ├── model/StudentModel.java    ← Form state (MVC Model)
    ├── service/ApiService.java    ← HTTP client
    ├── controller/
    │   ├── InputFormController.java
    │   ├── ResultsController.java
    │   └── HistoryController.java
    └── view/MainView.java         ← JavaFX tab layout (MVC View)
```
