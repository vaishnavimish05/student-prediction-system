#!/usr/bin/env bash
# =============================================================
#  Student Prediction System — Run Script
#  Usage: ./run.sh
#         ./run.sh --db-password=yourpassword
# =============================================================

JAR_DIR="$(cd "$(dirname "$0")" && pwd)"
JAR="$JAR_DIR/student-prediction.jar"

if [ ! -f "$JAR" ]; then
    echo "ERROR: student-prediction.jar not found in $JAR_DIR"
    echo "Please run build.sh first."
    exit 1
fi

# Parse optional args
DB_URL="jdbc:mariadb://localhost:3306/student_prediction_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
DB_USER="root"
DB_PASS=""

for arg in "$@"; do
    case $arg in
        --db-url=*)      DB_URL="${arg#*=}" ;;
        --db-user=*)     DB_USER="${arg#*=}" ;;
        --db-password=*) DB_PASS="${arg#*=}" ;;
    esac
done

echo "=============================================="
echo " Student Prediction System"
echo " Server: http://localhost:8080"
echo " DB:     $DB_URL"
echo "=============================================="

java \
  --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics \
  --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED \
  --add-opens javafx.graphics/com.sun.javafx.application=ALL-UNNAMED \
  -Ddb.url="$DB_URL" \
  -Ddb.user="$DB_USER" \
  -Ddb.password="$DB_PASS" \
  -jar "$JAR"
