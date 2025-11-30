# Eclipse Dataspace Components (EDC) - Minimal Proof of Concept

## 📚 What is Eclipse Dataspace Components?

**Eclipse Dataspace Components (EDC)** is an open-source framework for building **data space connectors**. These connectors enable secure, sovereign data sharing between organizations.

**This PoC simulates secure data-sharing between a financial data provider and an investment-research consumer using EDC.** It demonstrates how financial market data can be shared with policy controls for research purposes while preventing unauthorized redistribution.

### Key Concepts for Beginners

#### 🌐 Data Space
Think of a data space as a "virtual marketplace" where organizations can:
- Share data securely
- Maintain control over their data (data sovereignty)
- Follow common rules and standards
- Track how their data is used

#### 🔌 Connector
A connector is software that acts as a "smart gateway" for data sharing. Each organization runs their own connector to:
- Publish data they want to share (as "assets")
- Discover data from other organizations
- Negotiate contracts for data access
- Transfer data securely

#### 🧠 Control Plane vs 💪 Data Plane

**Control Plane** (The Brain):
- Manages metadata about available data
- Handles contract negotiations
- Enforces policies
- Coordinates data transfers

**Data Plane** (The Muscle):
- Performs actual data transfer
- Transforms data if needed
- Provides data streaming capabilities

#### 📦 Asset
An asset is anything you want to share:
- A database table
- A REST API
- A file or document
- Real-time data streams
- AI models

#### 📜 Policy
Rules that govern data usage:
- Who can access the data
- What they can do with it
- Time restrictions
- Geographic limitations
- Usage purposes (e.g., "research only")

#### 📝 Contract
A formal agreement between data provider and consumer that specifies:
- Which asset is being shared
- Which policies apply
- Duration of access
- Obligations of both parties

### How EDC Works (Simplified)

```
┌───────────────────────┐                                ┌───────────────────────┐
│    Data Provider      │                                │   Research Consumer   │
│      Connector        │                                │      Connector        │
│  (Market Data Feed)   │                                │   (Investment Firm)   │
└───────────┬───────────┘                                └───────────┬───────────┘
            │                                                        │
            │ 1. Publishes Asset + Policy                            │
            │    "Market Data Q1 2025 for research use"              │
            │                                                        │
            │ 2. ←───────────────────────────────────────────────────┤
            │    Consumer requests catalog                           │
            │    "What financial data do you have?"                  │
            │                                                        │
            ├───────────────────────────────────────────────────────►│
            │ 3. Returns catalog with available assets               │
            │                                                        │
            │ 4. ←───────────────────────────────────────────────────┤
            │    Consumer initiates contract negotiation             │
            │    "I want access to Market Data for research"         │
            │                                                        │
            ├───────────────────────────────────────────────────────►│
            │ 5. Verifies policy, creates contract                   │
            │    "Agreement created: Contract #123"                │
            │    "Purpose: portfolio-analytics"                      │
            │                                                        │
            │ 6. ←───────────────────────────────────────────────────┤
            │    Consumer requests data transfer                     │
            │    "Start transfer using Contract #123"              │
            │                                                        │
            ├───────────────────────────────────────────────────────►│
            │ 7. Market data flows through data plane                │
            │                                                        │
```

## 🏗️ Project Structure

```
edc-minimal-poc/
├── pom.xml                              # Maven configuration with EDC dependencies
├── dataspaceconnector-configuration.properties  # Single connector config
├── provider-connector.properties        # Provider connector config (ports 8xxx)
├── consumer-connector.properties        # Consumer connector config (ports 9xxx)
├── run-provider.ps1                     # Start Provider connector script
├── run-consumer.ps1                     # Start Consumer connector script
├── test-api.ps1                         # Quick API test script
├── test-catalog-query.ps1               # Two-connector catalog query test
├── README.md                            # This file
│
└── src/main/
    ├── java/com/example/edc/
    │   ├── MinimalEdcConnector.java           # Main entry point
    │   └── extension/
    │       └── SampleDataExtension.java       # Custom extension with sample data
    │
    └── resources/
        ├── logback.xml                        # Logging configuration
        └── META-INF/services/
            └── org.eclipse.edc.spi.system.ServiceExtension  # Extension registration
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher (EDC requires Java 17+)
- **Maven 3.8+**
- Internet connection (to download dependencies)

### Building the Project

```powershell
# Navigate to project directory
cd c:\_dev\workspace0\edc-minimal-poc

# Clean and build
mvn clean package

# This creates a "fat JAR" with all dependencies:
# target/edc-minimal-poc-1.0.0.jar
```

### Running the Connector

#### Option A: Single Connector Mode

```powershell
# Run the connector
java -jar target/edc-minimal-poc-1.0.0.jar
```

#### Option B: Two-Connector Mode (Provider + Consumer)

```powershell
# Terminal 1: Start Provider
.\run-provider.ps1

# Terminal 2: Start Consumer
.\run-consumer.ps1
```

The connector will start and display:
```
========================================
Starting Minimal EDC Connector PoC
========================================

What is happening now:
1. Loading EDC extensions (modular components)
2. Starting Control Plane (contract management)
3. Starting Data Plane (data transfer)
4. Starting Management API (REST interface)

========================================
EDC Connector Started Successfully!
========================================

Available endpoints:
- Management API: http://localhost:8181/api/management
- DSP Protocol: http://localhost:8282/api/dsp
- IDS Protocol: http://localhost:8383/api/v1/ids
```

### Port Reference

| Connector | Management API | DSP Protocol | Public API |
|-----------|----------------|--------------|------------|
| Single/Provider | 8181 | 8282 | 8080 |
| Consumer | 9181 | 9282 | 9080 |

## 🧪 Testing the Connector

### 1. List Assets

The `SampleDataExtension` automatically creates a sample "Market Data API" asset when the connector starts.

```powershell
# List all assets
curl http://localhost:8181/api/management/v3/assets
```

Expected response (JSON):
```json
[
  {
    "@id": "market-data-2025-q1",
    "@type": "Asset",
    "properties": {
      "name": "Market Data API",
      "description": "Real-time equity price feed for Q1 2025",
      "contentType": "application/json",
      "type": "API",
      "category": "financial-market",
      "assetClass": "equities",
      "region": "global"
    }
  }
]
```

### 2. Get Individual Resources

Get the pre-loaded sample resources:

```powershell
# Get the market data asset
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/market-data-2025-q1" -Method GET

# Get the financial research policy
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/policydefinitions/financial-research-policy" -Method GET

# Get the contract definition
Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/contractdefinitions/market-data-contract-def" -Method GET
```

### 3. Request Catalog (Two-Connector Mode)

In two-connector mode, the Consumer queries the Provider's catalog:

```powershell
# Run the catalog query test (requires both connectors running)
.\test-catalog-query.ps1
```

Or manually via API:

```powershell
# Consumer requests Provider's catalog
$body = @{
    "@context" = @{ "edc" = "https://w3id.org/edc/v0.0.1/ns/" }
    "@type" = "CatalogRequest"
    "counterPartyAddress" = "http://localhost:8282/api/dsp"
    "protocol" = "dataspace-protocol-http"
} | ConvertTo-Json -Depth 10

Invoke-RestMethod -Uri "http://localhost:9181/api/management/v3/catalog/request" `
    -Method POST -ContentType "application/json" -Body $body
```

### 4. Create Your Own Asset (Optional)

```powershell
# Create a new asset via API
curl -X POST http://localhost:8181/api/management/v3/assets `
  -H "Content-Type: application/json" `
  -d '{
    "@context": {
      "edc": "https://w3id.org/edc/v0.0.1/ns/"
    },
    "@id": "my-api-asset",
    "properties": {
      "name": "My Custom API",
      "contentType": "application/json"
    },
    "dataAddress": {
      "@type": "DataAddress",
      "type": "HttpData",
      "baseUrl": "https://api.example.com/data"
    }
  }'
```

## 📖 Understanding the Code

### MinimalEdcConnector.java

This is the **main entry point**. It:
1. Creates an EDC runtime
2. Boots all extensions
3. Starts all services (Control Plane, Data Plane, APIs)

**Key takeaway**: EDC uses a plugin architecture. The runtime discovers and loads all extensions automatically.

### SampleDataExtension.java

This is a **custom extension** that demonstrates how to:
1. Create assets programmatically
2. Define policies
3. Link them with contract definitions

**Key takeaway**: Extensions let you customize EDC behavior without modifying core code.

### config.properties

This file configures:
- Port numbers for different APIs
- Connector identity
- Storage backends (in-memory for this PoC)
- Transfer settings

Configuration files:
- `dataspaceconnector-configuration.properties` - Single connector mode
- `provider-connector.properties` - Provider config (ports 8xxx)
- `consumer-connector.properties` - Consumer config (ports 9xxx)

**Key takeaway**: EDC is highly configurable through properties files, environment variables, or system properties.

## 🎓 Learning Path

### For Complete Beginners:

1. **Start here**: Run the connector and explore the Management API
2. **Next**: Read `SampleDataExtension.java` to understand assets, policies, and contracts
3. **Then**: Try creating your own asset via the API
4. **Advanced**: Run two connectors and test catalog queries between them

### Key EDC Concepts to Master:

1. **Assets**: What data you're sharing
2. **Policies**: Rules for data usage
3. **Contract Definitions**: Templates linking assets and policies
4. **Contract Negotiations**: The process of agreeing on terms
5. **Transfer Processes**: Actually moving data

### Advanced Topics (Not in this PoC):

- Custom policy evaluators
- Database-backed persistence
- Authentication and authorization
- Custom data plane implementations
- Multi-party data sharing scenarios
- Integration with identity providers

## 🛠️ Extending This PoC

### Add a Database Backend

Replace in-memory stores with PostgreSQL:

```xml
<dependency>
    <groupId>org.eclipse.edc</groupId>
    <artifactId>sql-pool-apache-commons</artifactId>
    <version>${edc.version}</version>
</dependency>
```

### Add Authentication

Integrate OAuth2 or API keys:

```xml
<dependency>
    <groupId>org.eclipse.edc</groupId>
    <artifactId>auth-tokenbased</artifactId>
    <version>${edc.version}</version>
</dependency>
```

### Add Custom Policies

Implement `PolicyEngine` to enforce custom rules (e.g., rate limiting, geo-restrictions).

### Add More Data Sources

- S3 buckets
- Azure Blob Storage
- Kafka streams
- Database views

## 🐛 Troubleshooting

### "Port already in use"
Change ports in `config.properties`:
```properties
web.http.management.port=9181
web.http.protocol.port=9282
```

### "Java version error"
EDC requires Java 17+. Check your version:
```powershell
java -version
```

### "Dependencies not downloading"
Check your internet connection and Maven settings. Try:
```powershell
mvn clean install -U
```

## 📚 Additional Resources

- **Official EDC Documentation**: https://eclipse-edc.github.io/docs/
- **GitHub Repository**: https://github.com/eclipse-edc/Connector
- **Samples Repository**: https://github.com/eclipse-edc/Samples
- **Data Space Protocol**: https://docs.internationaldataspaces.org/

## 🤝 Contributing

This is a learning PoC. Feel free to:
- Add more sample extensions
- Improve documentation
- Create additional examples
- Fix bugs

## 📄 License

This project uses Eclipse Dataspace Components, which is licensed under Apache License 2.0.

## ❓ FAQ

**Q: What's the difference between EDC and other data sharing solutions?**
A: EDC focuses on **data sovereignty** - keeping control of your data even when sharing it. Unlike traditional APIs or file sharing, EDC enforces policies and tracks usage.

**Q: Do I need two connectors to test?**
A: Not initially. This PoC lets you explore one connector with `java -jar target/edc-minimal-poc-1.0.0.jar`. For full dataspace testing (catalog queries, negotiations), run two connectors:
```powershell
.\run-provider.ps1   # Terminal 1
.\run-consumer.ps1   # Terminal 2
.\test-catalog-query.ps1  # Terminal 3
```

**Q: Is this production-ready?**
A: No! This uses in-memory storage and simple policies. For production:
- Use persistent storage (PostgreSQL, etc.)
- Implement proper authentication
- Use HTTPS
- Add monitoring and logging
- Implement backup and disaster recovery

**Q: Can I use this with non-Java systems?**
A: Yes! EDC exposes REST APIs. Any system can interact with it over HTTP. The connector itself is Java, but your data sources and consumers can be anything.

**Q: How does EDC compare to API gateways?**
A: API gateways focus on routing and rate limiting. EDC adds contract negotiation, policy enforcement, and data sovereignty. Think of EDC as a "smart API gateway" for data spaces.

---

**Happy Learning! 🎉**

Start with running the connector, then explore the code, and finally try extending it with your own ideas!
