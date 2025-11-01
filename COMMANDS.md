# 🚀 EDC Quick Commands

## Build, Run, and Test Commands

### 1️⃣ Build the Application

```powershell
# Clean build with tests skipped (default)
mvn clean package

# Or with explicit skip
mvn clean package -DskipTests
```

**Expected Output:**
```
[INFO] Building jar: target/edc-minimal-poc-1.0.0.jar
[INFO] BUILD SUCCESS
```

---

### 2️⃣ Start the EDC Connector

```powershell
# Run the connector (Terminal 1)
java -jar target/edc-minimal-poc-1.0.0.jar
```

**Wait for this message:**
```
========================================
EDC Connector Started Successfully!
========================================

Available endpoints:
- Management API: http://localhost:8181/api/management
- DSP Protocol: http://localhost:8282/api/dsp
```

**Keep this terminal running!** The connector must stay active for testing.

---

### 3️⃣ Run the Tests

**Open a NEW PowerShell terminal (Terminal 2) and run:**

```powershell
# Run all integration tests
mvn test -DskipTests=false
```

**Expected Output:**
```
→ Running: 1. Connector Health Check - Is EDC running?
   ✓ Connector is responding!

→ Running: 2. GET Asset - Retrieve weather-api-asset
   ✓ Asset retrieved and validated successfully!

...

[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

### 4️⃣ Quick Manual Test (Alternative)

Instead of the full test suite, you can quickly verify with PowerShell:

```powershell
# Test the API manually (Terminal 2)
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/weather-api-asset" -Method GET
```

**Or use the test script:**
```powershell
.\test-api.ps1
```

---

## 📋 All-in-One Command Sequence

Copy and paste these commands in order:

```powershell
# Step 1: Build
mvn clean package

# Step 2: Start connector (keep this terminal open)
java -jar target/edc-minimal-poc-1.0.0.jar

# Step 3: In a NEW terminal, run tests
mvn test -DskipTests=false
```

---

## 🎯 Quick Reference

| Action | Command |
|--------|---------|
| **Build** | `mvn clean package` |
| **Run Connector** | `java -jar target/edc-minimal-poc-1.0.0.jar` |
| **Run All Tests** | `mvn test -DskipTests=false` |
| **Run Single Test** | `mvn test -DskipTests=false -Dtest=EdcManagementApiTest#testGetWeatherAsset` |
| **Quick Manual Test** | `.\test-api.ps1` |
| **Stop Connector** | `Ctrl+C` in the connector terminal |

---

## 🔄 Typical Workflow

### First Time Setup
```powershell
# 1. Build the project
mvn clean package

# 2. Start connector in Terminal 1
java -jar target/edc-minimal-poc-1.0.0.jar

# 3. Open Terminal 2 and run tests
mvn test -DskipTests=false
```

### After Code Changes
```powershell
# 1. Stop connector (Ctrl+C in Terminal 1)

# 2. Rebuild
mvn clean package

# 3. Restart connector (Terminal 1)
java -jar target/edc-minimal-poc-1.0.0.jar

# 4. Re-run tests (Terminal 2)
mvn test -DskipTests=false
```

---

## 🐛 Troubleshooting

### "Tests failed with Connection refused"
**Problem:** Connector is not running  
**Solution:** Start the connector in Terminal 1 first!

### "Port already in use"
**Problem:** Connector already running or port taken  
**Solution:** 
```powershell
# Stop existing connector with Ctrl+C
# Or find and kill the process
netstat -ano | findstr :8181
```

### "BUILD FAILURE: Tests run: 0"
**Problem:** Tests are being skipped  
**Solution:** Add `-DskipTests=false`:
```powershell
mvn test -DskipTests=false
```

---

## 💡 Pro Tips

**Run tests with detailed output:**
```powershell
mvn test -DskipTests=false -X
```

**Run only compilation (faster):**
```powershell
mvn clean compile
```

**Package without running Maven tests:**
```powershell
mvn clean package -DskipTests
```

**Run tests in IntelliJ/Eclipse:**
- Right-click `EdcManagementApiTest.java` → Run Tests

---

**Need more help?** See:
- [QUICKSTART.md](QUICKSTART.md) - Quick start guide
- [TESTING.md](TESTING.md) - Complete testing documentation
- [README.md](README.md) - Full project documentation
