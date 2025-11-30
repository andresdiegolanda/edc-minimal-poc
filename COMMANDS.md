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

## 🔵 Option A: Single Connector Mode

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
mvn verify
```

**Expected Output:**
```
→ Running: 1. Connector Health Check - Is EDC running?
   ✓ Connector is responding!

→ Running: 2. GET Asset - Retrieve market-data-2025-q1
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
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/market-data-2025-q1" -Method GET
```

**Or use the test script:**
```powershell
.\test-api.ps1
```

---

## 🟢 Option B: Two-Connector Mode (Provider + Consumer)

This demonstrates real dataspace behavior: one connector providing data, another consuming it.

### 2️⃣ Start Provider Connector (Terminal 1)

```powershell
.\run-provider.ps1
```

**Or manually:**
```powershell
java "-Dedc.fs.config=provider-connector.properties" -jar target/edc-minimal-poc-1.0.0.jar
```

**Provider Endpoints:**
| Endpoint       | URL                                      |
|----------------|------------------------------------------|
| Management API | http://localhost:8181/api/management     |
| DSP Protocol   | http://localhost:8282/api/dsp            |

---

### 3️⃣ Start Consumer Connector (Terminal 2)

```powershell
.\run-consumer.ps1
```

**Or manually:**
```powershell
java "-Dedc.fs.config=consumer-connector.properties" -jar target/edc-minimal-poc-1.0.0.jar
```

**Consumer Endpoints:**
| Endpoint       | URL                                      |
|----------------|------------------------------------------|
| Management API | http://localhost:9181/api/management     |
| DSP Protocol   | http://localhost:9282/api/dsp            |

---

### 4️⃣ Test Catalog Query (Terminal 3)

```powershell
.\test-catalog-query.ps1
```

This script:
1. ✅ Verifies both connectors are running
2. ✅ Consumer queries Provider's catalog via DSP
3. ✅ Displays available assets, policies, and contracts

**Expected Output:**
```
Step 1: Checking connectors...
  [OK] Provider is running
  [OK] Consumer is running

Step 2: Querying Provider's catalog via Consumer...
  Consumer (9181) --> DSP --> Provider (8282)

============================================
 CATALOG QUERY SUCCESSFUL!
============================================
```

---

## 📋 All-in-One Command Sequences

### Single Connector Mode
```powershell
# Step 1: Build
mvn clean package

# Step 2: Start connector (keep this terminal open)
java -jar target/edc-minimal-poc-1.0.0.jar

# Step 3: In a NEW terminal, run integration tests
mvn verify
```

### Two-Connector Mode
```powershell
# Step 1: Build
mvn clean package

# Step 2: Terminal 1 - Start Provider
.\run-provider.ps1

# Step 3: Terminal 2 - Start Consumer
.\run-consumer.ps1

# Step 4: Terminal 3 - Test catalog query
.\test-catalog-query.ps1
```

---

## 🎯 Quick Reference

| Action | Command |
|--------|---------|
| **Build** | `mvn clean package` |
| **Run Single Connector** | `java -jar target/edc-minimal-poc-1.0.0.jar` |
| **Run Provider** | `.\run-provider.ps1` |
| **Run Consumer** | `.\run-consumer.ps1` |
| **Test Catalog Query** | `.\test-catalog-query.ps1` |
| **Run Integration Tests** | `mvn verify` |
| **Run Single Test** | `mvn verify -Dit.test=EdcManagementApiIT#testGetMarketDataAsset` |
| **Quick Manual Test** | `.\test-api.ps1` |
| **Stop Connector** | `Ctrl+C` in the connector terminal |

---

## 🔌 Port Reference

| Connector | Management API | DSP Protocol | Public API |
|-----------|----------------|--------------|------------|
| Single/Provider | 8181 | 8282 | 8080 |
| Consumer | 9181 | 9282 | 9080 |

---

## 🔄 Typical Workflow

### First Time Setup (Single Connector)
```powershell
# 1. Build the project
mvn clean package

# 2. Start connector in Terminal 1
java -jar target/edc-minimal-poc-1.0.0.jar

# 3. Open Terminal 2 and run integration tests
mvn verify
```

### First Time Setup (Two Connectors)
```powershell
# 1. Build the project
mvn clean package

# 2. Start Provider in Terminal 1
.\run-provider.ps1

# 3. Start Consumer in Terminal 2
.\run-consumer.ps1

# 4. Run catalog query in Terminal 3
.\test-catalog-query.ps1
```

### After Code Changes
```powershell
# 1. Stop connector(s) (Ctrl+C)

# 2. Rebuild
mvn clean package

# 3. Restart connector(s)
.\run-provider.ps1    # Terminal 1
.\run-consumer.ps1    # Terminal 2 (if using two connectors)

# 4. Re-run tests
.\test-catalog-query.ps1  # or: mvn verify
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
taskkill /PID <pid> /F
```

### "Consumer can't reach Provider"
**Problem:** Provider not running or wrong port  
**Solution:** Ensure Provider is running on port 8282 (DSP):
```powershell
# Check Provider is up
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets" -Method GET
```

### "BUILD FAILURE: No tests executed"
**Problem:** Running `mvn test` instead of `mvn verify`  
**Solution:** Integration tests run with verify phase:
```powershell
mvn verify
```

---

## 💡 Pro Tips

**Run tests with detailed output:**
```powershell
mvn verify -X
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
- Right-click `EdcManagementApiIT.java` → Run Tests

**Quick check both connectors are up:**
```powershell
# Provider
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets" -Method GET

# Consumer
Invoke-WebRequest -Uri "http://localhost:9181/api/management/v3/assets" -Method GET
```

---

## 📁 Configuration Files

| File | Purpose |
|------|---------|
| `dataspaceconnector-configuration.properties` | Original single-connector config |
| `provider-connector.properties` | Provider connector config (ports 8xxx) |
| `consumer-connector.properties` | Consumer connector config (ports 9xxx) |

---

**Need more help?** See:
- [QUICKSTART.md](QUICKSTART.md) - Quick start guide
- [TESTING.md](TESTING.md) - Complete testing documentation
- [README.md](README.md) - Full project documentation
