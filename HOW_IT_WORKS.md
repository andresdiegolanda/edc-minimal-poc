# 🔍 How It All Works Together

This document explains the complete flow of the minimal EDC PoC, from startup to data sharing.

## 🚀 Startup Sequence

```
1. User runs: java -jar target/edc-minimal-poc-1.0.0.jar
                    ↓
2. MinimalEdcConnector.main() starts
                    ↓
3. BaseRuntime.boot(false) is called
                    ↓
4. EDC discovers all ServiceExtensions via Java SPI
   (looks in META-INF/services/org.eclipse.edc.spi.system.ServiceExtension)
                    ↓
5. Dependency Injection container is initialized
                    ↓
6. All extensions initialize in order:
   - Core extensions (from dependencies)
   - SampleDataExtension (our custom extension)
                    ↓
7. SampleDataExtension.initialize() executes:
   → Creates "Weather API" asset
   → Creates "allow-all" policy
   → Creates contract definition
   → Stores everything in memory
                    ↓
8. HTTP servers start:
   - Management API (port 8181)
   - DSP Protocol (port 8282)
   - Data Plane (port 8080)
                    ↓
9. Connector is ready! ✅
```

## 📊 Component Architecture

```
┌───────────────────────────────────────────────────────────────────┐
│                      EDC CONNECTOR RUNTIME                        │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │               SERVICE EXTENSION FRAMEWORK                   │  │
│  │          (Discovers and initializes all extensions)         │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                               │                                   │
│                ┌──────────────┴──────────────┐                    │
│                │                             │                    │
│  ┌─────────────▼─────────────┐  ┌────────────▼────────────────┐  │
│  │     CORE EXTENSIONS       │  │     CUSTOM EXTENSIONS       │  │
│  │                           │  │                             │  │
│  │  • Control Plane          │  │  • SampleDataExtension      │  │
│  │  • Data Plane             │  │    (Creates sample data)    │  │
│  │  • Management API         │  │                             │  │
│  │  • DSP Protocol           │  └─────────────────────────────┘  │
│  │  • JSON-LD                │                                   │
│  │  • HTTP Core              │                                   │
│  └───────────────────────────┘                                   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                   IN-MEMORY DATA STORES                     │  │
│  │                                                             │  │
│  │  • AssetIndex     (Stores: market-data-2025-q1 asset)       │  │
│  │  • PolicyStore    (Stores: financial-research-policy)       │  │
│  │  • ContractStore  (Stores: market-data-contract-def)        │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────────┐  │
│  │                HTTP ENDPOINTS (Jetty Server)                │  │
│  │                                                             │  │
│  │  • :8181/api/management  → Management API                   │  │
│  │  • :8282/api/dsp         → DSP Protocol                     │  │
│  │  • :8080/api/public      → Data Plane                       │  │
│  └─────────────────────────────────────────────────────────────┘  │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

## 🔄 Extension Loading Process

The `SampleDataExtension` is automatically discovered and loaded:

```
Step 1: JVM scans classpath
          ↓
Step 2: Finds: src/main/resources/META-INF/services/
              org.eclipse.edc.spi.system.ServiceExtension
          ↓
Step 3: Reads content:
        "com.example.edc.extension.SampleDataExtension"
          ↓
Step 4: Loads the class
          ↓
Step 5: Dependency Injection injects:
        @Inject AssetIndex assetIndex
        @Inject PolicyDefinitionStore policyStore
        @Inject ContractDefinitionStore contractStore
          ↓
Step 6: Calls initialize(ServiceExtensionContext context)
          ↓
Step 7: Extension creates and registers sample data
```

## 📦 What the SampleDataExtension Creates

### Asset: "market-data-2025-q1"
```json
{
  "id": "market-data-2025-q1",
  "name": "Market Data API",
  "description": "Real-time equity price feed for Q1 2025",
  "contentType": "application/json",
  "properties": {
    "type": "API",
    "category": "financial-market",
    "assetClass": "equities",
    "region": "global"
  },
  "dataAddress": {
    "type": "HttpData",
    "baseUrl": "https://api.marketdata.example.com/v1/equities/prices",
    "method": "GET"
  }
}
```

### Policy: "financial-research-policy"
```json
{
  "id": "financial-research-policy",
  "policy": {
    "permissions": [],    // Simplified for PoC
    "prohibitions": [],   // Would include: no redistribution
    "obligations": []     // Would include: delete after 12 months
  }
}
```

### Contract Definition: "market-data-contract-def"
```json
{
  "id": "market-data-contract-def",
  "accessPolicyId": "financial-research-policy",
  "contractPolicyId": "financial-research-policy",
  "assetsSelector": {
    "operandLeft": "id",
    "operator": "=",
    "operandRight": "market-data-2025-q1"
  }
}
```

## 🌐 API Request Flow

### Example: Get All Assets

```
1. User/Client sends:
   GET http://localhost:8181/api/management/v3/assets
        ↓
2. Jetty receives request on port 8181
        ↓
3. Jersey (JAX-RS) routes to Asset API endpoint
        ↓
4. API endpoint calls AssetIndex.queryAssets()
        ↓
5. AssetIndex retrieves from in-memory store:
   - market-data-2025-q1
        ↓
6. API transforms to JSON-LD format
        ↓
7. Response returned to client:
   [
     {
       "@id": "market-data-2025-q1",
       "properties": {...}
     }
   ]
```

## 🤝 Contract Negotiation Flow (Between Two Connectors)

This is what happens when a Consumer wants data from a Provider:

```
PROVIDER CONNECTOR                          CONSUMER CONNECTOR
(This PoC)                                  (Another instance)
       │                                          │
       │  1. Catalog Request                      │
       │◄─────────────────────────────────────────│
       │     GET /api/dsp/catalog                 │
       │                                          │
       │  2. Catalog Response                     │
       │─────────────────────────────────────────►│
       │     Returns: market-data-contract-def    │
       │                                          │
       │  3. Contract Negotiation Request         │
       │◄─────────────────────────────────────────│
       │     POST /api/dsp/negotiations           │
       │     Body: {                              │
       │       offerId: "market-data-contract..", │
       │       assetId: "market-data-2025-q1"     │
       │     }                                    │
       │                                          │
       │  4. Provider validates policy            │
       │     - Checks financial-research-policy   │
       │     - Validates constraints ✅            │
       │                                          │
       │  5. Contract Agreement                   │
       │─────────────────────────────────────────►│
       │     Response: {                          │
       │       agreementId: "contract-123"        │
       │       status: "FINALIZED"                │
       │     }                                    │
       │                                          │
       │  6. Transfer Request                     │
       │◄─────────────────────────────────────────│
       │     POST /api/dsp/transfers              │
       │     Body: {                              │
       │       agreementId: "contract-123"        │
       │     }                                    │
       │                                          │
       │  7. Data Plane provisions transfer       │
       │     - Creates data flow                  │
       │     - Returns endpoint                   │
       │                                          │
       │  8. Transfer Initiated                   │
       │─────────────────────────────────────────►│
       │     Response: {                          │
       │       transferId: "transfer-456"         │
       │       endpoint: "http://..."             │
       │     }                                    │
       │                                          │
       │  9. Data Transfer                        │
       │═════════════════════════════════════════►│
       │     Actual data flows through            │
     │     data plane on port 8080          │
     │                                      │
```

## 🔐 Policy Evaluation

Even though our policy has minimal constraints, here's how evaluation works:

```
1. Contract negotiation arrives
        ↓
2. Extract policy ID from contract definition
        ↓
3. Load policy from PolicyDefinitionStore
        ↓
4. Policy Engine evaluates:
   ┌───────────────────────────────────────┐
   │ FOR EACH permission:                  │
   │   - Check constraints                 │
   │   - If any fail → DENY                │
   │                                       │
   │ FOR EACH prohibition:                 │
   │   - Check if applies                  │
   │   - If any apply → DENY               │
   │                                       │
   │ FOR EACH obligation:                  │
   │   - Check if fulfilled                │
   │   - If any not fulfilled → DENY       │
   │                                       │
   │ If all pass → ALLOW                   │
   └───────────────────────────────────────┘
        ↓
5. In our case:
   - No constraints → ✅ PASS
   - No prohibitions → ✅ PASS
   - No obligations → ✅ PASS
        ↓
6. Contract is APPROVED
```

## 🗄️ Data Storage (In-Memory)

```
┌────────────────────────────────────────────┐
│          AssetIndex (HashMap)              │
├────────────────────────────────────────────┤
│ Key: "market-data-2025-q1"                 │
│ Value: Asset {                             │
│   id: "market-data-2025-q1"                │
│   properties: {...}                        │
│   dataAddress: {...}                       │
│ }                                          │
└────────────────────────────────────────────┘

┌────────────────────────────────────────────┐
│      PolicyDefinitionStore (HashMap)       │
├────────────────────────────────────────────┤
│ Key: "financial-research-policy"           │
│ Value: PolicyDefinition {                  │
│   id: "financial-research-policy"          │
│   policy: Policy { ... }                   │
│ }                                          │
└────────────────────────────────────────────┘

┌────────────────────────────────────────────┐
│      ContractDefinitionStore (HashMap)     │
├────────────────────────────────────────────┤
│ Key: "market-data-contract-def"            │
│ Value: ContractDefinition {                │
│   id: "market-data-contract-def"           │
│   accessPolicyId: "financial-research..."  │
│   contractPolicyId: "..."                  │
│   assetsSelector: {...}                    │
│ }                                          │
└────────────────────────────────────────────┘

⚠️ All data is LOST when connector stops!
   (This is intentional for the PoC)
```

## 🔍 Configuration Loading

```
1. Connector starts
        ↓
2. ConfigurationFsExtension loads config.properties
        ↓
3. Properties are read:
   - edc.participant.id = urn:connector:minimal-poc
   - web.http.management.port = 8181
   - web.http.protocol.port = 8282
   - etc.
        ↓
4. Configuration is stored in ServiceExtensionContext
        ↓
5. All extensions can access config via:
   context.getSetting("key", "default")
        ↓
6. HTTP servers bind to configured ports
```

## 📝 Logging Flow

```
1. Code calls:
   monitor.info("Message")
        ↓
2. Monitor delegates to SLF4J
        ↓
3. SLF4J routes to Logback
        ↓
4. Logback checks logback.xml:
   - Pattern: %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
   - Output: Console (STDOUT)
        ↓
5. Formatted message appears in console:
   23:45:12.345 [main] INFO  c.e.e.SampleDataExtension - Creating sample asset
```

## 🎯 Complete Lifecycle

```
┌───────────────────────────────────────────────────────────────┐
│ 1. INITIALIZATION PHASE                                       │
│    - Load extensions                                          │
│    - Inject dependencies                                      │
│    - Call initialize() on each extension                      │
│    - SampleDataExtension creates data                         │
└───────────────────────────────────────────────────────────────┘
                            ↓
┌───────────────────────────────────────────────────────────────┐
│ 2. STARTUP PHASE                                              │
│    - Start HTTP servers                                       │
│    - Bind to ports (8181, 8282, 8080)                         │
│    - Start background threads (state machines)                │
└───────────────────────────────────────────────────────────────┘
                            ↓
┌───────────────────────────────────────────────────────────────┐
│ 3. RUNNING PHASE                                              │
│    - Accept API requests                                      │
│    - Process contract negotiations                            │
│    - Transfer data                                            │
│    - Log activities                                           │
└───────────────────────────────────────────────────────────────┘
                            ↓
┌───────────────────────────────────────────────────────────────┐
│ 4. SHUTDOWN PHASE (Ctrl+C)                                    │
│    - Stop accepting new requests                              │
│    - Complete in-flight operations                            │
│    - Close HTTP servers                                       │
│    - Call shutdown() on extensions                            │
│    - Clear in-memory data (lost forever)                      │
└───────────────────────────────────────────────────────────────┘
```

## 🧩 How Everything Fits Together

```
USER ACTION                    COMPONENT                    RESULT
─────────────────────────────────────────────────────────────────
Run JAR                  →  Main class                 →  Starts runtime
                         →  Extension loading          →  Discovers extensions
                         →  Dependency injection       →  Wires services
                         →  SampleDataExtension       →  Creates sample data
                         →  HTTP servers start        →  APIs available

GET /api/management...   →  Management API            →  Returns assets
                         →  AssetIndex                →  Queries in-memory
                         →  JSON-LD transformer       →  Formats response

Contract negotiation     →  DSP API                   →  Receives request
                         →  Policy Engine             →  Evaluates policy
                         →  Contract store            →  Stores agreement
                         →  Response                  →  Returns contract ID

Data transfer            →  Transfer request          →  Validates contract
                         →  Data Plane                →  Provisions transfer
                         →  HTTP client               →  Fetches from backend
                         →  Stream                    →  Sends to consumer
```

## 🎓 Key Takeaways

1. **EDC is modular**: Everything is an extension
2. **Dependency injection**: Services are automatically wired
3. **In-memory by default**: Simple but not persistent
4. **Multiple protocols**: DSP, Management API, Data Plane
5. **Policy-driven**: Every access goes through policy evaluation
6. **Catalog-based discovery**: Assets are published in catalogs
7. **Contract-first**: No access without a valid contract

## 🔗 Where to Go From Here

Now that you understand how it works:

1. **Modify SampleDataExtension** to create different assets
2. **Implement a custom policy** with real constraints
3. **Set up two connectors** and test negotiation
4. **Add database persistence** instead of in-memory
5. **Explore the DSP protocol** with Postman/curl

---

**You now understand the complete flow!** 🎉

Every piece serves a purpose, and they all work together to enable secure, sovereign data sharing.
