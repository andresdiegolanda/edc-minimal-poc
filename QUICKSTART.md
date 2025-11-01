# 🚀 Quick Start Guide

## Step-by-Step: Your First 5 Minutes with EDC

### Step 1: Build the Project (1 minute)

```powershell
cd c:\_dev\workspace0\eclipseDataspaceComponents
mvn clean package
```

You'll see:
```
[INFO] Building jar: target/edc-minimal-poc-1.0.0.jar
[INFO] BUILD SUCCESS
```

### Step 2: Run the Connector (30 seconds)

```powershell
java -jar target/edc-minimal-poc-1.0.0.jar
```

Wait for:
```
========================================
EDC Connector Started Successfully!
========================================
```

### Step 3: Test It's Working (2 minutes)

Open a new PowerShell window and test the APIs:

```powershell
# Test 1: Get the sample weather asset
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/weather-api-asset" -Method GET

# Test 2: Get the sample policy
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/policydefinitions/allow-all-policy" -Method GET

# Test 3: Get the sample contract definition
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/contractdefinitions/weather-contract-def" -Method GET
```

**Expected Output for Test 1:**
```
@id         : weather-api-asset
@type       : Asset
properties  : @{name=Public Weather API; description=Provides current weather data...}
dataAddress : @{@type=DataAddress; type=HttpData}
```

✅ **Success!** Your EDC connector is running and responding to API calls!

### Step 4: Understand What You Built (1 minute)

You now have:
- ✅ A running EDC connector
- ✅ A sample "Weather API" asset
- ✅ An "allow-all" policy
- ✅ A contract definition linking them

### Step 5: Explore the Code (As long as you want!)

1. Open `src/main/java/com/example/edc/MinimalEdcConnector.java`
   - See how the connector starts

2. Open `src/main/java/com/example/edc/extension/SampleDataExtension.java`
   - See how assets, policies, and contracts are created

3. Open `config.properties`
   - See all configuration options

4. Read `README.md` for detailed explanations

---

## 🎯 What to Try Next

### Create Your Own Asset

```powershell
$body = @'
{
  "@context": {"edc": "https://w3id.org/edc/v0.0.1/ns/"},
  "@id": "my-first-asset",
  "properties": {
    "name": "My First Data Asset",
    "contenttype": "application/json",
    "description": "I created this!"
  },
  "dataAddress": {
    "@type": "DataAddress",
    "type": "HttpData",
    "baseUrl": "https://jsonplaceholder.typicode.com/posts"
  }
}
'@

Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets" -Method POST -ContentType "application/json" -Body $body
```

Then check it was created:
```powershell
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/my-first-asset" -Method GET
```

✅ **Success!** You've created your first custom asset!

### Modify the Sample Extension

1. Open `SampleDataExtension.java`
2. Change the asset name or description
3. Rebuild: `mvn clean package`
4. Restart the connector
5. Check your changes via the test script: `.\test-api.ps1`

### Run the Integration Tests

Want to see professional API testing in action?

```powershell
# Make sure connector is running first!
# Then in a new terminal:
mvn test -DskipTests=false
```

This runs a comprehensive test suite that:
- ✅ Verifies all endpoints are working
- ✅ Tests asset creation/retrieval
- ✅ Validates policies and contracts
- ✅ Demonstrates proper API usage

See [TESTING.md](TESTING.md) for complete testing guide.

---

## 🆘 Common Issues

**"curl: command not found"**
- On Windows PowerShell, use `Invoke-WebRequest` instead or install curl

**"Address already in use"**
- Stop the running connector (Ctrl+C)
- Or change ports in `config.properties`

**"Class not found"**
- Make sure you ran `mvn clean package` first

---

## 🎓 Next Learning Steps

1. ✅ You've completed: Running a basic connector
2. 📖 Next: Read the full [README.md](README.md) to understand concepts
3. 🔧 Then: Modify the sample extension
4. 🚀 Finally: Set up two connectors and test contract negotiation

**Congratulations! You're now an EDC beginner!** 🎉
