# 🧪 EDC Management API Integration Tests

## Overview

This test suite (`EdcManagementApiIT.java`) provides comprehensive integration testing for the Eclipse Dataspace Components (EDC) Management API. It's designed as both a testing tool AND an educational resource for learning how to interact with EDC programmatically.

---

## 📚 What You'll Learn

### Testing Concepts
- ✅ **Integration Testing**: Testing a running system (vs unit tests that test isolated code)
- ✅ **HTTP API Testing**: Making REST calls and validating responses
- ✅ **JSON-LD Parsing**: Working with EDC's JSON-LD format
- ✅ **Test Ordering**: Running tests in sequence with dependencies
- ✅ **Assertions**: Validating expected behavior

### EDC Concepts
- ✅ **Asset Management**: Creating and retrieving data assets
- ✅ **Policy Definitions**: Working with access policies
- ✅ **Contract Definitions**: Understanding catalog offers
- ✅ **Management API**: Using EDC's REST interface
- ✅ **JSON-LD Format**: EDC's data representation

---

## 🎯 Test Coverage

### Test 1: Connector Health Check
**Purpose**: Verify the EDC connector is running and accessible  
**What it tests**:
- Network connectivity to localhost:8181
- Management API responsiveness
- Basic system health

**Why it's first**: If this fails, all other tests will fail. It's a "smoke test."

---

### Test 2: GET Market Data Asset
**Purpose**: Retrieve the sample `market-data-2025-q1`  
**What it tests**:
- Asset retrieval via GET request
- JSON-LD response parsing
- Asset structure validation
- Property verification (name, description, type)

**API Used**: `GET /v3/assets/market-data-2025-q1`

**Expected Response**:
```json
{
  "@id": "market-data-2025-q1",
  "@type": "Asset",
  "properties": {
    "name": "Market Data API",
    "description": "Real-time equity price feed for Q1 2025",
    "contenttype": "application/json"
  },
  "dataAddress": {
    "@type": "DataAddress",
    "type": "HttpData"
  }
}
```

---

### Test 3: GET Financial Research Policy
**Purpose**: Retrieve the sample `financial-research-policy`  
**What it tests**:
- Policy retrieval via GET request
- Policy structure validation
- Policy type verification

**API Used**: `GET /v3/policydefinitions/financial-research-policy`

**Key Learning**: Policies define rules for data access. The "financial-research-policy" restricts usage to research purposes.

---

### Test 4: GET Contract Definition
**Purpose**: Retrieve the `market-data-contract-def`  
**What it tests**:
- Contract definition retrieval
- Policy linkage verification
- Asset-to-policy binding

**API Used**: `GET /v3/contractdefinitions/market-data-contract-def`

**Key Learning**: Contract definitions link assets with policies. They represent "offers" in the data space catalog.

---

### Test 5: POST New Asset (CREATE)
**Purpose**: Create a new asset programmatically  
**What it tests**:
- Asset creation via POST
- JSON-LD request body formatting
- Write operations on Management API
- Verification of created resource

**API Used**: `POST /v3/assets`

**Request Body Example**:
```json
{
  "@context": {
    "edc": "https://w3id.org/edc/v0.0.1/ns/"
  },
  "@id": "test-asset-12345",
  "properties": {
    "name": "Test Asset Created by JUnit",
    "description": "Created programmatically",
    "contenttype": "application/json"
  },
  "dataAddress": {
    "@type": "DataAddress",
    "type": "HttpData",
    "baseUrl": "https://jsonplaceholder.typicode.com/posts"
  }
}
```

**Why it's important**: Shows how to register new data assets in EDC from your applications.

---

### Test 6: GET Non-Existent Asset (Error Handling)
**Purpose**: Verify proper error handling  
**What it tests**:
- HTTP 404 response for missing resources
- Graceful error handling
- API stability under invalid requests

**API Used**: `GET /v3/assets/this-asset-does-not-exist`

**Expected**: HTTP 404 Not Found

**Why it's important**: Real applications must handle errors gracefully.

---

## 🚀 How to Run the Tests

### Prerequisites

**CRITICAL**: The EDC connector MUST be running before you run tests!

```powershell
# Terminal 1: Start the connector
java -jar target/edc-minimal-poc-1.0.0.jar
```

Wait for:
```
========================================
EDC Connector Started Successfully!
========================================
```

### Method 1: Run All Tests via Maven

```powershell
# Terminal 2: Run integration tests
mvn verify
```

**Note**: Integration tests run during the `verify` phase and require a running connector.

### Method 2: Run All Tests via IDE

1. Open `EdcManagementApiIT.java` in your IDE
2. Right-click on the class name
3. Select "Run Tests" or "Run 'EdcManagementApiIT'"

### Method 3: Run Single Test

```powershell
# Run just one specific test
mvn verify -Dit.test=EdcManagementApiIT#testGetMarketDataAsset
```

Or in your IDE:
1. Click the green arrow next to the test method
2. Select "Run"

### Method 4: Run with Detailed Output

```powershell
mvn verify -X
```

The `-X` flag shows DEBUG output, including full HTTP requests/responses.

---

## 📊 Expected Output

### Successful Test Run

```
========================================
Starting EDC Management API Tests
========================================
✓ HTTP Client initialized
✓ JSON Parser initialized
→ Base URL: http://localhost:8181/api/management

→ Running: 1. Connector Health Check - Is EDC running?
   Testing connector availability...
   Response Status: 200
   ✓ Connector is responding!

→ Running: 2. GET Asset - Retrieve market-data-2025-q1
   Retrieving market-data-2025-q1...
   Status: 200
   Response Body: {...}
   Asset Name: Market Data API
   ✓ Asset retrieved and validated successfully!

→ Running: 3. GET Policy - Retrieve financial-research-policy
   Retrieving financial-research-policy...
   Status: 200
   ✓ Policy retrieved and validated successfully!

→ Running: 4. GET Contract Definition - Retrieve market-data-contract-def
   Retrieving market-data-contract-def...
   Status: 200
   Access Policy: financial-research-policy
   Contract Policy: financial-research-policy
   ✓ Contract definition retrieved and validated!

→ Running: 5. POST Asset - Create a new test asset
   Creating new test asset...
   Request Body: {...}
   Status: 200
   Verifying asset was created...
   ✓ Asset created and verified successfully!
   ✓ Asset ID: test-asset-12345
   ✓ Asset Name: Test Asset Created by JUnit

→ Running: 6. GET Non-existent Asset - Should return 404
   Requesting non-existent asset...
   Status: 404
   ✓ Correctly returned 404 for non-existent asset

========================================
All Tests Completed
========================================

[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

### Failed Test (Connector Not Running)

```
java.net.ConnectException: Connection refused: connect
	at EdcManagementApiIT.testConnectorHealth(EdcManagementApiIT.java:195)

[ERROR] Tests run: 6, Failures: 0, Errors: 1, Skipped: 5
```

**Solution**: Start the EDC connector first!

---

## 🔍 Understanding the Code

### Test Structure

```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // Run tests in order
class EdcManagementApiIT {
    
    @BeforeAll  // Runs once before all tests
    static void setup() {
        // Initialize HTTP client, JSON parser
    }
    
    @BeforeEach  // Runs before each test
    void beforeEachTest(TestInfo testInfo) {
        // Print test name
    }
    
    @Test  // Marks a test method
    @Order(1)  // Test execution order
    @DisplayName("Test description")  // Human-readable name
    void testSomething() throws Exception {
        // 1. Arrange: Set up test data
        // 2. Act: Perform action
        // 3. Assert: Verify results
    }
    
    @AfterAll  // Runs once after all tests
    static void teardown() {
        // Cleanup
    }
}
```

### HTTP Request Pattern

```java
// GET request
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(BASE_URL + "/v3/assets/my-asset"))
    .header("Accept", "application/json")
    .timeout(Duration.ofSeconds(10))
    .GET()
    .build();

HttpResponse<String> response = httpClient.send(
    request, 
    HttpResponse.BodyHandlers.ofString()
);

// POST request with JSON body
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create(BASE_URL + "/v3/assets"))
    .header("Content-Type", "application/json")
    .header("Accept", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
    .build();
```

### JSON Parsing Pattern

```java
// Parse response
JsonNode json = objectMapper.readTree(response.body());

// Access fields
String id = json.get("@id").asText();
String name = json.get("properties").get("name").asText();

// Check if field exists
if (json.has("dataAddress")) {
    // Process dataAddress
}
```

### Assertion Patterns

```java
// Equality assertion
assertEquals(expected, actual, "Error message");
assertEquals(200, response.statusCode(), "Should be HTTP 200");

// Null checks
assertNotNull(value, "Should not be null");

// Boolean conditions
assertTrue(condition, "Should be true");
assertFalse(condition, "Should be false");
```

---

## 🛠️ Customizing the Tests

### Change Base URL

If your connector runs on a different port:

```java
private static final String BASE_URL = "http://localhost:9999/api/management";
```

### Add Your Own Test

```java
@Test
@Order(7)
@DisplayName("7. My Custom Test")
void testMyFeature() throws Exception {
    System.out.println("   Testing my feature...");
    
    // Your test code here
    HttpRequest request = buildGetRequest("/v3/assets/my-asset");
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
    assertEquals(200, response.statusCode(), "Should succeed");
    
    System.out.println("   ✓ Test passed!");
}
```

### Test Different Data

Modify the POST asset test to create different assets:

```java
String newAssetJson = """
{
  "@context": {"edc": "https://w3id.org/edc/v0.0.1/ns/"},
  "@id": "my-custom-asset",
  "properties": {
    "name": "My Custom Asset",
    "contenttype": "application/xml"
  },
  "dataAddress": {
    "@type": "DataAddress",
    "type": "HttpData",
    "baseUrl": "https://my-api.example.com/data"
  }
}
""";
```

---

## 🐛 Troubleshooting

### "Connection refused"
**Problem**: EDC connector is not running  
**Solution**: Start the connector in a separate terminal:
```powershell
java -jar target/edc-minimal-poc-1.0.0.jar
```

### "Class not found: org.junit.jupiter"
**Problem**: JUnit dependencies not loaded  
**Solution**: Rebuild the project:
```powershell
mvn clean install
```

### "HTTP 404 Not Found" for valid endpoints
**Problem**: Using wrong API version or path  
**Solution**: Check the Management API documentation:
- Assets: `/v3/assets`
- Policies: `/v2/policydefinitions`
- Contracts: `/v2/contractdefinitions`

### "Tests are skipped"
**Problem**: Running `mvn test` instead of `mvn verify`  
**Solution**: Integration tests run during the `verify` phase:
```powershell
mvn verify
```

### "JSON parsing error"
**Problem**: Response format doesn't match expected structure  
**Solution**: Print the response body for debugging:
```java
System.out.println("Response: " + response.body());
```

---

## 📖 Learning Path

### Beginner Level
1. ✅ Run all tests successfully
2. ✅ Read each test method and understand what it does
3. ✅ Modify test names and descriptions
4. ✅ Change timeout values and observe behavior

### Intermediate Level
5. ✅ Add a new test for creating a policy
6. ✅ Test contract negotiation (requires two connectors)
7. ✅ Add negative tests (invalid JSON, wrong types)
8. ✅ Test pagination for large datasets

### Advanced Level
9. ✅ Add performance tests (measure response times)
10. ✅ Add concurrent test execution
11. ✅ Test catalog federation (multiple connectors)
12. ✅ Implement full contract negotiation flow

---

## 🔗 Related Resources

### EDC Documentation
- [EDC GitHub](https://github.com/eclipse-edc/Connector)
- [Management API Spec](https://eclipse-edc.github.io/docs)
- [JSON-LD Overview](https://json-ld.org/)

### Testing Resources
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [HTTP Client Tutorial](https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html)
- [Jackson Documentation](https://github.com/FasterXML/jackson-docs)

### Project Files
- `EdcManagementApiIT.java` - The integration test class
- `SampleDataExtension.java` - Creates test data
- `MinimalEdcConnector.java` - Main connector class
- `README.md` - Comprehensive project documentation

---

## 💡 Best Practices

### Test Design
✅ **One assertion per concept**: Each test should verify one thing  
✅ **Descriptive names**: Use `@DisplayName` for clarity  
✅ **Ordered execution**: Use `@Order` when tests have dependencies  
✅ **Proper cleanup**: Use `@AfterEach` or `@AfterAll` for cleanup  
✅ **Meaningful messages**: Include descriptive assertion messages

### EDC API Usage
✅ **Check HTTP status first**: Always verify the response code  
✅ **Parse JSON safely**: Check if fields exist before accessing  
✅ **Use proper timeouts**: Don't wait forever for responses  
✅ **Handle errors gracefully**: Test negative cases too  
✅ **Follow JSON-LD format**: Include @context for EDC compatibility

### Code Organization
✅ **Helper methods**: Reduce duplication with buildGetRequest(), etc.  
✅ **Constants**: Use static final for URLs, timeouts  
✅ **Comments**: Explain WHY, not just WHAT  
✅ **Logging**: Use System.out for test progress visibility

---

## 🎓 What's Next?

After mastering these tests:

1. **Extend Test Coverage**
   - Add tests for UPDATE operations (PUT)
   - Add tests for DELETE operations
   - Test error conditions exhaustively

2. **Test Real Scenarios**
   - Two-connector contract negotiation
   - Data transfer workflows
   - Policy evaluation

3. **Automate Testing**
   - Add to CI/CD pipeline
   - Run tests on every commit
   - Generate test reports

4. **Performance Testing**
   - Measure API response times
   - Test with large datasets
   - Load testing with multiple clients

---

## ✨ Summary

This test suite is your gateway to understanding EDC programmatically:

- **Educational**: Every method is extensively documented
- **Practical**: Real API calls you can run and modify
- **Comprehensive**: Covers all basic Management API operations
- **Production-ready**: Use as foundation for real test suites

**Master these tests, and you'll master EDC's Management API!** 🚀

---

*Last Updated: November 1, 2025*  
*EDC Version: 0.8.1*  
*JUnit Version: 5.10.0*
