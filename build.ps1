# Student Prediction System - Windows Build Script (PowerShell)
param(
    [switch]$Clean = $false
)

$ErrorActionPreference = "Stop"
$scriptDir = Get-Location
$srcDir = "$scriptDir\src"
$libDir = "$scriptDir\lib"
$buildDir = "$scriptDir\build"
$jarFile = "$scriptDir\student-prediction.jar"

Write-Host "=============================================="
Write-Host " Student Prediction System - Building..."
Write-Host "=============================================="
Write-Host ""

# 1. Cleanup
if (Test-Path $buildDir) {
    Write-Host "[1/4] Cleaning up..."
    Remove-Item $buildDir -Recurse -Force
}
New-Item -ItemType Directory -Path $buildDir -Force | Out-Null

# 2. Build classpath
Write-Host "[2/4] Building classpath..."
$jars = @(Get-ChildItem -Path $libDir -Filter "*.jar" -File | ForEach-Object { $_.FullName })
$classpath = $jars -join ";"
Write-Host "       Found $($jars.Count) JARs"

# 3. Get all Java files
Write-Host "[3/4] Compiling..."
$javaFiles = @(Get-ChildItem -Path $srcDir -Recurse -Filter "*.java" -File | ForEach-Object { $_.FullName })

if ($javaFiles.Count -eq 0) {
    Write-Host "ERROR: No Java files found in $srcDir"
    exit 1
}

Write-Host "       Found $($javaFiles.Count) Java files"

# Compile
& javac --release 21 `
        -cp $classpath `
        -d $buildDir `
        $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Compilation failed!"
    exit 1
}

$classFiles = @(Get-ChildItem -Path $buildDir -Recurse -Filter "*.class" -File)
Write-Host "       Compiled: $($classFiles.Count) class files"

# 4. Bundle and JAR
Write-Host "[4/4] Creating fat JAR..."

# Extract all JARs
Push-Location $buildDir
foreach ($jar in $jars) {
    Write-Host "       Extracting: $(Split-Path -Leaf $jar)"
    & jar xf $jar 2>$null
}

# Remove module-info conflicts
Get-ChildItem -Path . -Recurse -Filter "module-info.class" -File | Remove-Item -Force

Pop-Location

# Create manifest
$manifest = @"
Main-Class: MainLauncher
Multi-Release: true
"@
$manifest | Out-File -Encoding ASCII "$env:TEMP\manifest.txt"

# Create JAR
& jar cfm $jarFile "$env:TEMP\manifest.txt" -C $buildDir "." 2>$null

Write-Host ""
Write-Host "=============================================="
Write-Host " BUILD SUCCESSFUL!"
Write-Host " Output: $jarFile"
Write-Host "=============================================="
Write-Host ""
Write-Host " How to run:"
Write-Host "   java -jar $jarFile"
Write-Host ""
