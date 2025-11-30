package com.example.edc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EDC MANAGEMENT API TEST SUITE
 * ==============================
 * 
 * WHAT IS THIS CLASS?
 * -------------------
 * This is a comprehensive test suite for the Eclipse Dataspace Components (EDC) 
 * Management API. It tests all the core endpoints to ensure the connector is 
 * working correctly.
 * 
 * WHY TESTING IS IMPORTANT:
 * -------------------------
 * 1. VERIFICATION: Confirms that your EDC connector is running and accessible
 * 2. VALIDATION: Ensures all sample data (assets, policies, contracts) are loaded
 * 3. REGRESSION PREVENTION: Catches bugs when you make changes
 * 4. DOCUMENTATION: Shows how to interact with EDC APIs programmatically
 * 5. LEARNING TOOL: Demonstrates proper API usage patterns
 * 
 * WHAT YOU'LL LEARN:
 * ------------------
 * - How to make HTTP requests to EDC Management API
 * - How to parse JSON-LD responses (EDC uses JSON-LD format)
 * - How to verify asset, policy, and contract data
 * - How to create new assets via API
 * - How to handle EDC-specific JSON structures
 * 
 * PREREQUISITES TO RUN THESE TESTS:
 * ----------------------------------
 * 1. EDC connector MUST be running on localhost:8181
 * 2. Sample data must be loaded (via SampleDataExtension)
 * 3. JUnit 5 and Jackson libraries must be in classpath (already in pom.xml)
 * 
 * HOW TO RUN:
 * -----------
 * Method 1: Via Maven
 *   mvn test
 * 
 * Method 2: Via IDE
 *   Right-click this class → "Run Tests"
 * 
 * Method 3: Single test
 *   mvn test -Dtest=EdcManagementApiTest#testGetWeatherAsset
 * 
 * IMPORTANT NOTES:
 * ----------------
 * - These are INTEGRATION tests (require running connector)
 * - NOT unit tests (don't test in isolation)
 * - Tests run sequentially (@TestMethodOrder)
 * - Each test builds on previous tests' success
 * - If connector isn't running, all tests will fail
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("EDC Management API Integration Tests")
class EdcManagementApiIT {

    // =============================================================================
    // TEST CONFIGURATION
    // =============================================================================
    
    /**
     * BASE URL - Where is the EDC Management API running?
     * This is the root endpoint for all management operations.
     * Default: http://localhost:8181/api/management
     */
    private static final String BASE_URL = "http://localhost:8181/api/management";
    
    /**
     * HTTP CLIENT - Used to make REST API calls
     * Java 11+ provides a modern, built-in HTTP client that:
     * - Supports HTTP/2
     * - Handles JSON naturally
     * - Has timeout capabilities
     * - Is thread-safe and reusable
     */
    private static HttpClient httpClient;
    
    /**
     * JSON PARSER - Used to parse JSON responses
     * Jackson ObjectMapper converts JSON strings to Java objects and vice versa.
     * EDC uses JSON-LD format, which is JSON with additional context.
     */
    private static ObjectMapper objectMapper;

    // =============================================================================
    // TEST LIFECYCLE METHODS
    // =============================================================================
    
    /**
     * SETUP - Runs ONCE before ALL tests
     * 
     * @BeforeAll means this method runs before any test in this class.
     * It's the perfect place to:
     * - Initialize resources that all tests will share
     * - Set up connections
     * - Load test data
     * 
     * Why static? Because @BeforeAll methods must be static - they run
     * before any test instance is created.
     */
    @BeforeAll
    static void setup() {
        System.out.println("\n========================================");
        System.out.println("Starting EDC Management API Tests");
        System.out.println("========================================");
        
        // Initialize HTTP client with sensible timeouts
        // - connectTimeout: How long to wait for initial connection
        // - Other timeouts set per-request (see buildRequest method)
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        // Initialize JSON parser
        objectMapper = new ObjectMapper();
        
        System.out.println("✓ HTTP Client initialized");
        System.out.println("✓ JSON Parser initialized");
        System.out.println("→ Base URL: " + BASE_URL);
    }

    /**
     * TEARDOWN - Runs ONCE after ALL tests
     * 
     * @AfterAll means this method runs after all tests complete.
     * Use it for cleanup:
     * - Close connections
     * - Delete test data
     * - Release resources
     */
    @AfterAll
    static void teardown() {
        System.out.println("\n========================================");
        System.out.println("All Tests Completed");
        System.out.println("========================================\n");
    }

    /**
     * BEFORE EACH TEST - Runs before EVERY test method
     * 
     * @BeforeEach runs before each individual test.
     * Useful for:
     * - Printing test names
     * - Resetting state
     * - Setting up test-specific data
     */
    @BeforeEach
    void beforeEachTest(TestInfo testInfo) {
        System.out.println("\n→ Running: " + testInfo.getDisplayName());
    }

    // =============================================================================
    // TEST 1: CONNECTOR HEALTH CHECK
    // =============================================================================
    
    /**
     * TEST: Is the EDC connector accessible?
     * 
     * WHAT THIS TESTS:
     * - Can we reach the connector at all?
     * - Is the Management API endpoint responding?
     * - Is the connector healthy and ready?
     * 
     * WHY IT'S FIRST:
     * If this fails, all other tests will fail. This is a "smoke test" - 
     * it checks if the system is even running before testing specific features.
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP 404 is NORMAL (no endpoint at root /api/management)
     * - HTTP 200 from a specific endpoint means connector is alive
     * - Connection errors mean connector isn't running
     * 
     * @Order(1) ensures this test runs first
     */
    @Test
    @Order(1)
    @DisplayName("1. Connector Health Check - Is EDC running?")
    void testConnectorHealth() throws Exception {
        System.out.println("   Testing connector availability...");
        
        // Try to reach ANY endpoint to verify connector is running
        // We use the assets endpoint because we know it exists
        HttpRequest request = buildGetRequest("/v3/assets/market-data-2025-q1");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // We just want to verify we get SOME response (not a connection error)
        // Any HTTP status code (200, 404, 400, etc.) means connector is alive
        // Connection refused would throw an exception
        System.out.println("   Response Status: " + response.statusCode());
        
        // If we get here without exception, connector is reachable
        assertTrue(response.statusCode() > 0, "Connector should be reachable");
        System.out.println("   ✓ Connector is responding!");
    }

    // =============================================================================
    // TEST 2: GET SAMPLE ASSET
    // =============================================================================
    
    /**
     * TEST: Can we retrieve the sample "market-data-2025-q1" asset?
     * 
     * WHAT THIS TESTS:
     * - Asset retrieval via GET request
     * - JSON-LD response parsing
     * - Sample data was loaded correctly
     * - Asset properties are correct
     * 
     * EDC API PATTERN:
     * GET /v3/assets/{asset-id}
     * - Returns a single asset by ID
     * - Response is JSON-LD format
     * - Includes @id, @type, properties, dataAddress
     * 
     * JSON-LD EXPLAINED:
     * EDC uses JSON-LD (JSON for Linked Data):
     * - @id: Unique identifier
     * - @type: Resource type (Asset, Policy, etc.)
     * - @context: Defines vocabulary (namespace mappings)
     * - Regular properties: name, description, etc.
     */
    @Test
    @Order(2)
    @DisplayName("2. GET Asset - Retrieve market-data-2025-q1")
    void testGetMarketDataAsset() throws Exception {
        System.out.println("   Retrieving market-data-2025-q1...");
        
        // Build GET request to specific asset endpoint
        HttpRequest request = buildGetRequest("/v3/assets/market-data-2025-q1");
        
        // Send request and get response as String
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Log the response for debugging
        System.out.println("   Status: " + response.statusCode());
        System.out.println("   Response Body: " + response.body());
        
        // ASSERTION 1: Check HTTP status code
        // 200 OK means success
        assertEquals(200, response.statusCode(), "Should get HTTP 200 OK");
        
        // Parse JSON response into a tree structure we can navigate
        JsonNode json = objectMapper.readTree(response.body());
        
        // ASSERTION 2: Check asset ID
        // JSON path: json["@id"]
        assertEquals("market-data-2025-q1", json.get("@id").asText(), 
                "Asset ID should match");
        
        // ASSERTION 3: Check asset type
        // JSON path: json["@type"]
        assertEquals("Asset", json.get("@type").asText(), 
                "Should be of type Asset");
        
        // ASSERTION 4: Check asset name in properties
        // JSON path: json["properties"]["name"]
        String assetName = json.get("properties").get("name").asText();
        assertNotNull(assetName, "Asset should have a name");
        System.out.println("   Asset Name: " + assetName);
        
        // ASSERTION 5: Verify properties exist
        assertTrue(json.has("properties"), "Asset should have properties");
        assertTrue(json.has("dataAddress"), "Asset should have dataAddress");
        
        System.out.println("   ✓ Asset retrieved and validated successfully!");
    }

    // =============================================================================
    // TEST 3: GET SAMPLE POLICY
    // =============================================================================
    
    /**
     * TEST: Can we retrieve the "financial-research-policy"?
     * 
     * WHAT THIS TESTS:
     * - Policy retrieval via GET request
     * - Policy definition structure
     * - Policy rules and permissions
     * 
     * POLICY CONCEPTS:
     * Policies in EDC define rules for data access and usage:
     * - Permissions: What actions are allowed (use, transfer, etc.)
     * - Prohibitions: What actions are forbidden
     * - Obligations: What must be done (logging, payment, etc.)
     * 
     * The "financial-research-policy" represents typical financial data constraints:
     * - Usage purpose: Research and portfolio analytics
     * - Prohibition: No redistribution
     * - Obligation: Delete after 12 months
     * 
     * EDC API PATTERN:
     * GET /v3/policydefinitions/{policy-id}
     */
    @Test
    @Order(3)
    @DisplayName("3. GET Policy - Retrieve financial-research-policy")
    void testGetFinancialResearchPolicy() throws Exception {
        System.out.println("   Retrieving financial-research-policy...");
        
        HttpRequest request = buildGetRequest("/v3/policydefinitions/financial-research-policy");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   Status: " + response.statusCode());
        System.out.println("   Response Body: " + response.body());
        
        // Verify successful response
        assertEquals(200, response.statusCode(), "Should get HTTP 200 OK");
        
        // Parse and validate JSON structure
        JsonNode json = objectMapper.readTree(response.body());
        
        // Check policy ID
        assertEquals("financial-research-policy", json.get("@id").asText(), 
                "Policy ID should match");
        
        // Check it's a PolicyDefinition
        String type = json.get("@type").asText();
        assertTrue(type.contains("PolicyDefinition"), 
                "Should be a PolicyDefinition");
        
        // Verify policy has rules/permissions
        assertTrue(json.has("policy"), "Policy should have policy rules");
        
        System.out.println("   ✓ Policy retrieved and validated successfully!");
    }

    // =============================================================================
    // TEST 4: GET SAMPLE CONTRACT DEFINITION
    // =============================================================================
    
    /**
     * TEST: Can we retrieve the "market-data-contract-def"?
     * 
     * WHAT THIS TESTS:
     * - Contract definition retrieval
     * - Link between assets and policies
     * - Contract offer structure
     * 
     * CONTRACT DEFINITION EXPLAINED:
     * A Contract Definition links:
     * - ASSETS: What data is being offered
     * - ACCESS POLICY: Who can see/request the data
     * - CONTRACT POLICY: Rules for using the data
     * 
     * Think of it as a "catalog entry" that says:
     * "I'm offering [this asset] under [these terms]"
     * 
     * When a consumer browses your catalog, they see Contract Definitions.
     * Each one represents an offer they can negotiate.
     * 
     * EDC API PATTERN:
     * GET /v3/contractdefinitions/{contract-id}
     */
    @Test
    @Order(4)
    @DisplayName("4. GET Contract Definition - Retrieve market-data-contract-def")
    void testGetContractDefinition() throws Exception {
        System.out.println("   Retrieving market-data-contract-def...");
        
        HttpRequest request = buildGetRequest("/v3/contractdefinitions/market-data-contract-def");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   Status: " + response.statusCode());
        System.out.println("   Response Body: " + response.body());
        
        assertEquals(200, response.statusCode(), "Should get HTTP 200 OK");
        
        JsonNode json = objectMapper.readTree(response.body());
        
        // Verify contract ID
        assertEquals("market-data-contract-def", json.get("@id").asText(), 
                "Contract definition ID should match");
        
        // IMPORTANT: Check policy links
        // Contract definition must reference policies
        assertTrue(json.has("accessPolicyId"), 
                "Contract definition must have access policy");
        assertTrue(json.has("contractPolicyId"), 
                "Contract definition must have contract policy");
        
        // Verify it links to our financial-research-policy
        String accessPolicy = json.get("accessPolicyId").asText();
        String contractPolicy = json.get("contractPolicyId").asText();
        
        System.out.println("   Access Policy: " + accessPolicy);
        System.out.println("   Contract Policy: " + contractPolicy);
        
        assertEquals("financial-research-policy", accessPolicy, 
                "Should link to financial-research-policy for access");
        assertEquals("financial-research-policy", contractPolicy, 
                "Should link to financial-research-policy for contract");
        
        System.out.println("   ✓ Contract definition retrieved and validated!");
    }

    // =============================================================================
    // TEST 5: CREATE A NEW ASSET
    // =============================================================================
    
    /**
     * TEST: Can we create a new asset via POST?
     * 
     * WHAT THIS TESTS:
     * - Asset creation via POST request
     * - JSON-LD request body formatting
     * - Write operations on Management API
     * - Verification of created resource
     * 
     * WHY THIS IS IMPORTANT:
     * Previous tests only READ data. This test WRITES data.
     * It demonstrates how to:
     * - Programmatically register new assets
     * - Use proper JSON-LD format for EDC
     * - Handle POST requests with body
     * 
     * ASSET STRUCTURE:
     * An asset requires:
     * 1. @context: JSON-LD namespace definitions
     * 2. @id: Unique identifier for the asset
     * 3. properties: Metadata (name, type, description, etc.)
     * 4. dataAddress: Where/how to access the actual data
     * 
     * DATA ADDRESS EXPLAINED:
     * The dataAddress tells EDC where the data lives:
     * - type: "HttpData" for HTTP APIs
     * - baseUrl: The actual endpoint
     * - Other types: S3, AzureBlob, Database, etc.
     * 
     * EDC API PATTERN:
     * POST /v3/assets
     * Body: JSON-LD asset definition
     * Returns: Created asset (or error)
     */
    @Test
    @Order(5)
    @DisplayName("5. POST Asset - Create a new test asset")
    void testCreateAsset() throws Exception {
        System.out.println("   Creating new test asset...");
        
        // Use timestamp to ensure unique asset ID for each test run
        String assetId = "test-asset-" + System.currentTimeMillis();
        
        // Define the new asset in JSON-LD format
        // This is what we'll send in the POST body
        String newAssetJson = String.format("""
        {
          "@context": {
            "edc": "https://w3id.org/edc/v0.0.1/ns/"
          },
          "@id": "%s",
          "properties": {
            "name": "Test Asset Created by JUnit",
            "description": "This asset was created programmatically via the Management API",
            "contenttype": "application/json",
            "version": "1.0.0"
          },
          "dataAddress": {
            "@type": "DataAddress",
            "type": "HttpData",
            "baseUrl": "https://jsonplaceholder.typicode.com/posts"
          }
        }
        """, assetId);
        
        System.out.println("   Request Body:");
        System.out.println(newAssetJson);
        
        // Build POST request with JSON body
        HttpRequest request = buildPostRequest("/v3/assets", newAssetJson);
        
        // Send request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   Status: " + response.statusCode());
        System.out.println("   Response Body: " + response.body());
        
        // EDC typically returns 200 or 201 for successful creation
        assertTrue(response.statusCode() >= 200 && response.statusCode() < 300,
                "Should get successful response (2xx)");
        
        // Now verify the asset was created by retrieving it
        System.out.println("   Verifying asset was created...");
        
        HttpRequest getRequest = buildGetRequest("/v3/assets/" + assetId);
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        
        assertEquals(200, getResponse.statusCode(), 
                "Should be able to retrieve the newly created asset");
        
        // Parse and validate the retrieved asset
        JsonNode json = objectMapper.readTree(getResponse.body());
        
        assertEquals(assetId, json.get("@id").asText(),
                "Retrieved asset should have correct ID");
        
        String name = json.get("properties").get("name").asText();
        assertEquals("Test Asset Created by JUnit", name,
                "Asset name should match what we created");
        
        System.out.println("   ✓ Asset created and verified successfully!");
        System.out.println("   ✓ Asset ID: test-asset-12345");
        System.out.println("   ✓ Asset Name: " + name);
    }

    // =============================================================================
    // TEST 6: HANDLE NON-EXISTENT RESOURCE
    // =============================================================================
    
    /**
     * TEST: What happens when we request a non-existent asset?
     * 
     * WHAT THIS TESTS:
     * - Error handling
     * - HTTP 404 responses
     * - Graceful failure behavior
     * 
     * WHY TEST NEGATIVE CASES:
     * Real applications must handle errors gracefully.
     * This test ensures:
     * - EDC returns proper 404 for missing resources
     * - Your code can detect and handle missing assets
     * - The connector doesn't crash on bad requests
     * 
     * EXPECTED BEHAVIOR:
     * - HTTP 404 Not Found
     * - Error message in response body
     * - Connection remains stable
     */
    @Test
    @Order(6)
    @DisplayName("6. GET Non-existent Asset - Should return 404")
    void testGetNonExistentAsset() throws Exception {
        System.out.println("   Requesting non-existent asset...");
        
        // Try to get an asset that definitely doesn't exist
        HttpRequest request = buildGetRequest("/v3/assets/this-asset-does-not-exist");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("   Status: " + response.statusCode());
        System.out.println("   Response Body: " + response.body());
        
        // Should get 404 Not Found
        assertEquals(404, response.statusCode(), 
                "Non-existent asset should return HTTP 404");
        
        System.out.println("   ✓ Correctly returned 404 for non-existent asset");
    }

    // =============================================================================
    // HELPER METHODS
    // =============================================================================
    
    /**
     * HELPER: Build a GET request
     * 
     * WHY USE A HELPER METHOD:
     * - Reduces code duplication
     * - Centralizes request configuration
     * - Makes tests more readable
     * - Easy to modify headers/timeouts in one place
     * 
     * @param path The API path (e.g., "/v3/assets/my-asset")
     * @return Configured HttpRequest ready to send
     */
    private HttpRequest buildGetRequest(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Accept", "application/json")  // We want JSON response
                .timeout(Duration.ofSeconds(10))        // Don't wait forever
                .GET()                                   // HTTP GET method
                .build();
    }
    
    /**
     * HELPER: Build a POST request with JSON body
     * 
     * POST requests are used to CREATE new resources.
     * The body contains the data for the new resource in JSON format.
     * 
     * @param path The API path (e.g., "/v3/assets")
     * @param jsonBody The JSON content to send
     * @return Configured HttpRequest ready to send
     */
    private HttpRequest buildPostRequest(String path, String jsonBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/json")  // Body is JSON
                .header("Accept", "application/json")        // We want JSON response
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))  // Send JSON body
                .build();
    }
}
