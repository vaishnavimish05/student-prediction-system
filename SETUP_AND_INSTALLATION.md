# Student Prediction System - Setup & Installation Guide

## 🖥️ How to Run on Different Systems

This guide explains how to run the Student Prediction System on Windows, Linux, and macOS.

---

## ✅ System Requirements

### Minimum Requirements (All Systems)
- **Java Runtime:** JDK 21 or higher
- **RAM:** 2 GB minimum (4 GB recommended)
- **Disk Space:** 500 MB
- **GUI Support:** Display server (X11 on Linux, native on Windows/Mac)

### Recommended Setup
- **OS:** Windows 10+, Ubuntu 20.04+, macOS 12+
- **Java:** JDK 21.0.2 LTS
- **RAM:** 4-8 GB
- **Internet:** Optional (for initial setup only)

---

## 🪟 Windows Setup

### Step 1: Install Java 21

**Option A: Using Chocolatey (Recommended)**
```powershell
# Install Chocolatey first if needed
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install Java 21
choco install openjdk21 -y
```

**Option B: Manual Download**
1. Go to https://www.oracle.com/java/technologies/downloads/#java21
2. Download "Windows x64 Installer"
3. Run installer and follow prompts
4. Accept default installation path (usually `C:\Program Files\Java\jdk-21`)

**Option C: Using Windows Package Manager**
```powershell
winget install Oracle.JDK.21
```

### Step 2: Verify Java Installation

```powershell
# Open PowerShell and check Java version
java -version
```

Expected output:
```
openjdk 21.0.2 2024-01-16
OpenJDK Runtime Environment (build 21.0.2+13-LTS)
OpenJDK 64-Bit Server VM (build 21.0.2+13-LTS, mixed mode)
```

### Step 3: Download Application

```powershell
# Option A: Clone from repository (if available)
git clone <repository-url>
cd student-prediction-system

# Option B: Extract from ZIP
# 1. Download student-prediction-system.zip
# 2. Extract to C:\Users\YourUsername\Documents\student-prediction-system
# 3. Open PowerShell in that directory
```

### Step 4: Build Application (First Time Only)

```powershell
# Navigate to project directory
cd "C:\path\to\student-prediction-system\dist"

# Run build script
powershell -ExecutionPolicy Bypass -File build.ps1
```

**Expected Output:**
```
==============================================
 Student Prediction System - Building...
==============================================

[1/4] Checking Java version...
[2/4] Building classpath...
       Found 13 JARs
[3/4] Compiling...
       Found 20 Java files
[4/4] Packaging...

==============================================
 BUILD SUCCESSFUL!
 Output: C:\path\student-prediction.jar
==============================================
```

### Step 5: Run Application

```powershell
# From the dist directory
java -jar student-prediction.jar
```

**Expected Console Output:**
```
[SERVER] Starting Student Prediction Server on port 8080
[DB] App will run without DB (in-memory mode).
[HISTORY] Loaded 6 historical records
Apr 06, 2026 12:21:45 AM org.apache.catalina.core.StandardService startInternal
INFO: Starting service [Tomcat]
[SERVER] Server started ✓ http://localhost:8080/api/health
```

### Step 6: Access Application

Open your browser and go to:
```
http://localhost:8080/
```

---

## 🐧 Linux Setup (Ubuntu/Debian)

### Step 1: Install Java 21

```bash
# Update package manager
sudo apt update
sudo apt upgrade -y

# Install OpenJDK 21
sudo apt install openjdk-21-jdk -y

# Verify installation
java -version
```

### Step 2: Download Application

```bash
# Option A: Using Git
git clone <repository-url>
cd student-prediction-system/dist

# Option B: Using wget
wget https://github.com/your-repo/releases/download/v1.0/student-prediction.jar
```

### Step 3: Set Permissions

```bash
# Make build script executable
chmod +x build.sh

# If using PowerShell script, convert to Bash first
# Or manually compile using javac
```

### Step 4: Compile (if needed)

```bash
# Option A: If you have a Bash build script
./build.sh

# Option B: Manual compilation
javac -d build/classes src/**/*.java
jar cvfe student-prediction.jar MainApp build/classes
```

### Step 5: Run Application

```bash
# Simple run
java -jar student-prediction.jar

# Or with more memory allocation
java -Xmx2g -jar student-prediction.jar

# Running in background with logging
java -jar student-prediction.jar > app.log 2>&1 &
```

### Step 6: Access Application

```bash
# Open browser (if GUI available)
xdg-open http://localhost:8080

# Or from another machine
# http://<your-linux-ip>:8080
```

### Step 7: Optional - Run as Service

Create systemd service file:
```bash
sudo nano /etc/systemd/system/student-prediction.service
```

Add content:
```ini
[Unit]
Description=Student Prediction System
After=network.target

[Service]
Type=simple
User=youruser
WorkingDirectory=/home/youruser/student-prediction-system/dist
ExecStart=/usr/bin/java -jar student-prediction.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Then:
```bash
sudo systemctl daemon-reload
sudo systemctl enable student-prediction
sudo systemctl start student-prediction
sudo systemctl status student-prediction
```

---

## 🍎 macOS Setup

### Step 1: Install Java 21

**Option A: Using Homebrew (Recommended)**
```bash
# Install Homebrew if needed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install OpenJDK 21
brew install openjdk@21

# Add to PATH
sudo ln -sfn /usr/local/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
```

**Option B: Direct Download**
1. Visit https://www.oracle.com/java/technologies/downloads/#java21
2. Download "macOS Installer (ARM64)" or "macOS Installer (x64)"
3. Run installer

### Step 2: Verify Installation

```bash
java -version
```

### Step 3: Download Application

```bash
# Using Git
git clone <repository-url>
cd student-prediction-system/dist

# Or download ZIP and extract
# cd ~/Downloads/student-prediction-system/dist
```

### Step 4: Build Application

```bash
# Make build script executable
chmod +x build.sh

# Run build (if using Bash script)
./build.sh

# Or for PowerShell script, use native compilation
javac -d build/classes src/**/*.java
jar cvfe student-prediction.jar MainApp build/classes
```

### Step 5: Run Application

```bash
# Start application
java -jar student-prediction.jar

# With more memory
java -Xmx2g -jar student-prediction.jar
```

### Step 6: Access Application

```bash
# Open browser
open http://localhost:8080

# Or manually: Safari → Address Bar → http://localhost:8080
```

### Step 7: Optional - Create Alias

```bash
# Add to ~/.zshrc or ~/.bash_profile
alias start-prediction="cd ~/student-prediction-system/dist && java -jar student-prediction.jar"

# Then restart terminal and use:
start-prediction
```

---

## 🔌 Network Access

### Local Machine Only
```
# Default - only accessible from same machine
http://localhost:8080
```

### From Other Machines on Network

**On Server/Application Machine:**
```powershell
# Windows - Find your IP
ipconfig

# Linux/Mac - Find your IP
ifconfig
# or
hostname -I
```

**On Client Machine:**
```
http://<server-ip>:8080

# Example: http://192.168.1.100:8080
```

### Production Deployment (Port 80)

```bash
# Linux - Run with sudo to use port 80
sudo java -jar student-prediction.jar --port=80

# Or use a reverse proxy (Nginx/Apache)
```

---

## 📦 Pre-built JAR Distribution

If you have pre-built `student-prediction.jar`:

### Windows
```powershell
# Just run the JAR directly
java -jar student-prediction.jar
```

### Linux/Mac
```bash
# Make sure Java 21 is installed
java --version

# Run the JAR
java -jar student-prediction.jar
```

---

## 🔧 Configuration Options

### Command-Line Arguments

```bash
# Custom port
java -jar student-prediction.jar --port=9000

# Custom database
java -jar student-prediction.jar --db=mysql://localhost:3306/prediction_db

# Debug mode
java -jar student-prediction.jar --debug

# Disable history loading
java -jar student-prediction.jar --no-history
```

### JVM Options

```bash
# Allocate 4GB RAM
java -Xmx4g -jar student-prediction.jar

# Verbose garbage collection
java -Xmx2g -verbose:gc -jar student-prediction.jar

# Remote debugging
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar student-prediction.jar
```

### Environment Variables

```bash
# Windows PowerShell
$env:JAVA_OPTS = "-Xmx2g"
java -jar student-prediction.jar

# Linux/Mac Bash
export JAVA_OPTS="-Xmx2g"
java -jar student-prediction.jar
```

---

## 🐛 Troubleshooting

### Problem: "Java not found"

**Solution:**
```bash
# Windows
$env:PATH += ";C:\Program Files\Java\jdk-21\bin"
java -version

# Linux/Mac
export PATH="/usr/local/opt/openjdk@21/bin:$PATH"
java -version
```

### Problem: "Port 8080 already in use"

**Solution Option 1: Kill existing process**
```powershell
# Windows
Get-Process -Name java | Stop-Process -Force

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

**Solution Option 2: Use different port**
```bash
java -jar student-prediction.jar --port=9000
# Then access: http://localhost:9000
```

### Problem: "Out of Memory"

**Solution:**
```bash
# Increase JVM heap size
java -Xmx4g -jar student-prediction.jar
```

### Problem: "Cannot create temporary directory"

**Solution:**
```powershell
# Windows
$env:TEMP = "C:\Temp"
mkdir $env:TEMP -Force
java -jar student-prediction.jar

# Linux/Mac
export TMPDIR=/tmp
java -jar student-prediction.jar
```

### Problem: "History file not found"

**Solution:**
```bash
# Ensure you're running from the dist directory
cd /path/to/student-prediction-system/dist
java -jar student-prediction.jar

# Or copy history file to working directory
cp student_predictions_history.csv ./
```

### Problem: Application starts but UI doesn't open

**Solution:**
```bash
# This is normal in headless environments
# Access via browser instead: http://localhost:8080

# For headless Linux servers, ensure X11 forwarding
ssh -X user@remote-server
java -jar student-prediction.jar
```

---

## 📊 Verification Checklist

After starting the application, verify:

- [ ] Console shows "[SERVER] Server started ✓"
- [ ] Port 8080 is listening (check with netstat)
- [ ] Browser can access http://localhost:8080
- [ ] Form loads without errors
- [ ] "Predict" button works
- [ ] Results display correctly
- [ ] History tab loads data
- [ ] Quit button closes application cleanly

### Verify Using Commands

```bash
# Check if port is listening
# Windows
netstat -ano | findstr :8080

# Linux
lsof -i :8080
netstat -tulpn | grep :8080

# Mac
lsof -i :8080
```

### Test API Endpoints

```bash
# Health check
curl http://localhost:8080/api/health

# Get students
curl http://localhost:8080/api/students

# Get history
curl http://localhost:8080/api/history

# Get insights
curl http://localhost:8080/api/insights
```

---

## 🌐 Docker Deployment (Advanced)

If you want to containerize the application:

### Dockerfile

```dockerfile
FROM openjdk:21-slim

WORKDIR /app

# Copy application
COPY student-prediction.jar .

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "student-prediction.jar"]
```

### Build Docker Image

```bash
docker build -t student-prediction:latest .
```

### Run Docker Container

```bash
# Simple run
docker run -p 8080:8080 student-prediction:latest

# With volume for history persistence
docker run -p 8080:8080 -v /data:/app/data student-prediction:latest

# In background
docker run -d -p 8080:8080 --name prediction student-prediction:latest
```

### Docker Compose

```yaml
version: '3.8'

services:
  prediction-app:
    image: student-prediction:latest
    ports:
      - "8080:8080"
    volumes:
      - ./data:/app/data
    environment:
      - JAVA_OPTS=-Xmx2g
    restart: unless-stopped
```

Run with:
```bash
docker-compose up -d
```

---

## 🔐 Security Considerations

### Running in Production

1. **Use HTTPS/SSL**
   - Use reverse proxy (Nginx/Apache)
   - Install SSL certificate
   - Forward HTTP traffic to HTTPS

2. **Authentication**
   - Add login system to InputFormController
   - Validate user roles before predictions
   - Implement JWT tokens

3. **Database Security**
   - Use environment variables for credentials
   - Don't hardcode connection strings
   - Use connection pooling

4. **Network Security**
   - Run behind firewall
   - Whitelist IP addresses
   - Use VPN for remote access

5. **Data Protection**
   - Encrypt student data at rest
   - Use TLS for data in transit
   - Implement audit logging

### Example: HTTPS with Nginx Reverse Proxy

```nginx
server {
    listen 443 ssl;
    server_name prediction.example.com;

    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 🚀 Performance Optimization

### For High Load

```bash
# Allocate more memory
java -Xmx8g -Xms4g student-prediction.jar

# Enable garbage collection optimization
java -XX:+UseG1GC -Xmx8g student-prediction.jar

# Enable aggressive optimization
java -XX:+AggressiveOpts -Xmx8g student-prediction.jar
```

### Monitor Performance

```bash
# Monitor heap usage
jps -l                    # List running Java processes
jstat -gc <pid> 1000      # Garbage collection stats every 1 second
jmap -heap <pid>          # Heap memory dump
```

---

## 📝 Logging & Debugging

### Enable Debug Logging

```bash
# Run with debug output
java -Ddebug=true -jar student-prediction.jar

# Log to file
java -jar student-prediction.jar > app.log 2>&1
tail -f app.log

# Windows PowerShell
java -jar student-prediction.jar | Tee-Object -FilePath app.log
```

### View Application Logs

```bash
# Linux/Mac - Follow log in real-time
tail -f app.log

# Windows PowerShell
Get-Content app.log -Wait -Tail 20

# Grep errors (Linux/Mac)
grep "ERROR" app.log
```

---

## 📋 Quick Start Summary

### Windows (PowerShell)
```powershell
# 1. Install Java
choco install openjdk21

# 2. Navigate to project
cd "C:\path\to\student-prediction-system\dist"

# 3. Build (first time)
powershell -ExecutionPolicy Bypass -File build.ps1

# 4. Run
java -jar student-prediction.jar

# 5. Open browser
Start-Process "http://localhost:8080"
```

### Linux (Bash)
```bash
# 1. Install Java
sudo apt install openjdk-21-jdk

# 2. Navigate to project
cd ~/student-prediction-system/dist

# 3. Build (if needed)
javac -d classes src/**/*.java
jar cvfe app.jar MainApp classes

# 4. Run
java -jar student-prediction.jar

# 5. Open browser
xdg-open http://localhost:8080 &
```

### macOS (Bash)
```bash
# 1. Install Java
brew install openjdk@21

# 2. Navigate to project
cd ~/student-prediction-system/dist

# 3. Build
javac -d classes src/**/*.java

# 4. Run
java -jar student-prediction.jar

# 5. Open browser
open http://localhost:8080
```

---

## ✨ Conclusion

The Student Prediction System is **fully portable** and can run on:
- ✅ Windows 10+ (PowerShell)
- ✅ Linux (Ubuntu, Debian, CentOS)
- ✅ macOS (Intel & Apple Silicon)
- ✅ Docker containers
- ✅ Cloud platforms (AWS, Azure, GCP)
- ✅ Raspberry Pi (with Java 21)

All you need is **Java 21** installed!

For questions or issues, check the Troubleshooting section or consult the main PROJECT_EXPLANATION.md.
