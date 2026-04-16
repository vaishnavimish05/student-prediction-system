#!/usr/bin/env bash
# =============================================================
#  Student Prediction System — Build Script
#  Usage:  chmod +x build.sh && ./build.sh
#  Output: dist/student-prediction.jar
#          Run:  java -jar dist/student-prediction.jar
# =============================================================
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$SCRIPT_DIR/src"
LIB="$SCRIPT_DIR/lib"
BUILD="$SCRIPT_DIR/build"
DIST="$SCRIPT_DIR/dist"
JAR="$DIST/student-prediction.jar"

echo "=============================================="
echo " Student Prediction System — Building..."
echo "=============================================="

# ── 1. Cleanup ─────────────────────────────────────────────────
rm -rf "$BUILD" && mkdir -p "$BUILD" "$DIST"

# ── 2. Build classpath from lib/ ───────────────────────────────
CP=""
for f in "$LIB"/*.jar; do
    CP="${CP}:${f}"
done
CP="${CP:1}"  # remove leading colon
echo "[1/4] Classpath: $(echo "$CP" | tr ':' '\n' | wc -l) JARs"

# ── 3. Compile ─────────────────────────────────────────────────
echo "[2/4] Compiling Java sources..."
find "$SRC" -name "*.java" > /tmp/sps_sources.txt
javac --release 17 \
      --add-modules javafx.controls,javafx.fxml \
      -cp "$CP" \
      -d "$BUILD" \
      @/tmp/sps_sources.txt

echo "      Compiled: $(find "$BUILD" -name "*.class" | wc -l) class files"

# ── 4. Extract all dependency JARs into build/ ─────────────────
echo "[3/4] Bundling dependencies..."
cd "$BUILD"
for f in "$LIB"/*.jar; do
    jar xf "$f" 2>/dev/null || true
done
# Remove module-info files that conflict
find . -name "module-info.class" -delete
cd "$SCRIPT_DIR"

# ── 5. Create fat JAR with manifest ────────────────────────────
echo "[4/4] Creating fat JAR..."
cat > /tmp/sps_manifest.txt <<EOF
Main-Class: MainLauncher
Multi-Release: true
EOF

jar cfm "$JAR" /tmp/sps_manifest.txt -C "$BUILD" .

SIZE=$(du -sh "$JAR" | cut -f1)
echo ""
echo "=============================================="
echo " BUILD SUCCESSFUL!"
echo " Output : $JAR ($SIZE)"
echo "=============================================="
echo ""
echo " How to run:"
echo "   java --add-modules javafx.controls,javafx.fxml \\"
echo "        -jar $JAR"
echo ""
echo " With MySQL:"
echo "   java --add-modules javafx.controls,javafx.fxml \\"
echo "        -Ddb.url=jdbc:mariadb://localhost:3306/student_prediction_db \\"
echo "        -Ddb.user=root \\"
echo "        -Ddb.password=yourpassword \\"
echo "        -jar $JAR"
echo ""
