@echo off
REM =============================================================
REM  Student Prediction System — Windows Build Script
REM =============================================================

setlocal enabledelayedexpansion

echo.
echo ==============================================
echo  Student Prediction System - Building...
echo ==============================================
echo.

REM ── 1. Cleanup ───────────────────────────────────────────────
echo [1/4] Cleaning up old build...
if exist build (
    powershell -Command "Remove-Item -Recurse -Force -Path build -ErrorAction SilentlyContinue"
)
mkdir build

REM ── 2. Build classpath ───────────────────────────────────────
echo [2/4] Building classpath...
REM Build classpath with all JARs from lib folder
set "CP=lib\jackson-annotations-2.15.2.jar;lib\jackson-core-2.15.2.jar;lib\jackson-databind-2.15.2.jar;lib\jakarta.annotation-api-2.1.1.jar;lib\jakarta.servlet-api-6.0.0.jar;lib\javafx-base-21.jar;lib\javafx-controls-21-win.jar;lib\javafx-fxml-21.jar;lib\javafx-graphics-21-win.jar;lib\javafx-swing-21.jar;lib\javafx-web-21.jar;lib\tomcat-embed-core-10.1.16.jar;lib\tomcat-embed-jasper-10.1.16.jar"
echo       Classpath ready with dependencies

REM ── 3. Compile Java sources ───────────────────────────────────
echo [3/4] Compiling Java sources...

REM Use PowerShell to compile Java files
powershell -NoProfile -Command "$ClassPath = '%CP%'; $OutDir = 'build'; $Files = @(Get-ChildItem -Recurse -Path 'src' -Filter '*.java' -File | ForEach-Object {$_.FullName}); if ($Files.Count -gt 0) { Write-Host 'Compiling' $Files.Count 'Java files...'; javac -source 21 --enable-preview -cp $ClassPath -d $OutDir $Files; exit $LASTEXITCODE; } else { Write-Error 'No Java files found'; exit 1; }"

if !ERRORLEVEL! neq 0 (
    echo ERROR: Compilation failed!
    echo Check if sources exist in src folder
    exit /b 1
)

set CLASS_COUNT=0
for /r build %%f in (*.class) do set /A CLASS_COUNT+=1
echo       Compiled: !CLASS_COUNT! class files

REM ── 4. Bundle dependencies and create JAR ───────────────────
echo [4/4] Creating fat JAR...

REM Delete old JAR if it exists
if exist student-prediction.jar del student-prediction.jar

REM Extract all dependency JARs
cd build
for %%f in (..\lib\*.jar) do (
    jar xf "%%f" 2>nul
)
REM Remove conflicting module-info
for /r . %%f in (module-info.class) do del "%%f" 2>nul
cd ..

REM Create JAR with manifest
(
    echo Main-Class: MainLauncher
    echo Multi-Release: true
) > %TEMP%\manifest.txt

jar cfm student-prediction.jar %TEMP%\manifest.txt -C build .

if !ERRORLEVEL! neq 0 (
    echo ERROR: JAR creation failed!
    exit /b 1
)

echo.
echo ==============================================
echo  BUILD SUCCESSFUL!
echo  Output: student-prediction.jar
echo ==============================================
echo.
