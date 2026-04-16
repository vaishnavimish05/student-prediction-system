@echo off
:: =============================================================
::  Student Prediction System - Windows Run Script
::  Double-click this file or run from command prompt
:: =============================================================

set JAR_DIR=%~dp0
set JAR=%JAR_DIR%student-prediction.jar

if not exist "%JAR%" (
    echo ERROR: student-prediction.jar not found!
    echo Please ensure student-prediction.jar is in the same folder.
    pause
    exit /b 1
)

echo ==============================================
echo  Student Prediction System
echo  Server: http://localhost:8080
echo ==============================================
echo.
echo Starting... (a window will open shortly)
echo.

java -jar "%JAR%"
 
if %ERRORLEVEL% neq 0 (
    echo.
    echo Application exited with error. Check that Java 17+ is installed.
    pause
)
