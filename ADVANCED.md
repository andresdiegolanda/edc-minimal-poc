# 🎯 EDC Advanced Examples & Concepts

## Understanding the EDC Architecture

### The Big Picture

```
┌─────────────────────────────────────────────────────────┐
│                    EDC CONNECTOR                         │
│                                                          │
│  ┌────────────────────┐      ┌────────────────────┐   │
│  │   CONTROL PLANE    │      │    DATA PLANE      │   │
│  │                    │      │                    │   │
│  │  • Asset Index     │      │  • Data Transfer   │   │
│  │  • Policy Store    │◄────►│  • Transformation  │   │
│  │  • Contract Store  │      │  • Streaming       │   │
│  │  • Negotiation     │      │                    │   │
│  └────────────────────┘      └────────────────────┘   │
│           ▲                           ▲                │
│           │                           │                │
│  ┌────────┴──────────┐       ┌────────┴──────────┐   │
│  │  Management API   │       │   Public API      │   │
│  │  (Port 8181)      │       │   (Port 8080)     │   │
│  └───────────────────┘       └───────────────────┘   │
│           ▲                                            │
│           │                                            │
└───────────┼────────────────────────────────────────────┘
            │
            │ HTTP/REST
            │
     ┌──────┴───────┐
     │   Admin/     │
     │   Internal   │
     │   Systems    │
     └──────────────┘
```

## 🔄 Complete Data Sharing Flow

### Scenario: Company A shares sales data with Company B

```
Step 1: PROVIDER SETUP (Company A)
====================================
Provider creates:
1. Asset: "Q3 Sales Report"
2. Policy: "Only for business intelligence purposes"
3. Contract Definition: Links asset + policy

Step 2: DISCOVERY (Company B)
====================================
Consumer:
1. Connects to Provider's DSP endpoint
2. Requests catalog
3. Sees "Q3 Sales Report" with policy

Step 3: NEGOTIATION (Automated)
====================================
1. Consumer initiates contract negotiation
2. Provider validates Consumer meets policy requirements
3. Both parties cryptographically sign the agreement
4. Contract is created with unique ID

Step 4: DATA TRANSFER (Automated)
====================================
1. Consumer initiates transfer using contract ID
2. Provider's Control Plane authorizes the request
3. Provider's Data Plane streams the data
4. Consumer's Data Plane receives and stores data

Step 5: AUDITING
====================================
Both parties maintain records:
- Who accessed what data
- When it was accessed
- Under which contract terms
- For compliance and governance
```

## 📊 Real-World Use Cases

### Use Case 1: Healthcare Data Exchange

```
Scenario: Hospital shares patient data with research institute

Asset:
  - Type: SQL Database View
  - Content: Anonymized patient records
  - Format: CSV

Policy:
  - Purpose: Medical research only
  - Retention: Delete after 2 years
  - Geography: EU only
  - Prohibition: No commercial use

Contract:
  - Duration: 12 months
  - Usage limit: 10,000 records max
  - Reporting: Monthly usage reports required
```

### Use Case 2: Supply Chain Transparency

```
Scenario: Manufacturer shares component quality data with OEM

Asset:
  - Type: REST API
  - Content: Real-time quality metrics
  - Update frequency: Every 5 minutes

Policy:
  - Who: Authorized OEM partners only
  - What: Read-only access
  - When: Business hours only (9-5 UTC)
  - Audit: All accesses logged

Contract:
  - Payment: Per-request pricing
  - SLA: 99.9% uptime guarantee
  - Support: 24/7 technical support included
```

### Use Case 3: Smart City Data Sharing

```
Scenario: City shares traffic data with transportation app

Asset:
  - Type: Kafka Stream
  - Content: Real-time traffic sensor data
  - Volume: 1 million events/hour

Policy:
  - Purpose: Public transportation optimization
  - Rate limit: 1000 requests/minute
  - Attribution: Must credit data source
  - Privacy: No personal identification

Contract:
  - Cost: Free for non-commercial use
  - Commercial: License fee applies
  - Updates: Receive schema change notifications
```

## 🔐 Policy Examples

### Example 1: Time-Limited Access

```java
// Policy: Access only during Q4 2025
var policy = Policy.Builder.newInstance()
    .permission(Permission.Builder.newInstance()
        .action(Action.Builder.newInstance()
            .type("USE")
            .build())
        .constraint(Constraint.Builder.newInstance()
            .leftExpression(new LiteralExpression("DATETIME"))
            .operator(Operator.GEQ)
            .rightExpression(new LiteralExpression("2025-10-01T00:00:00Z"))
            .build())
        .constraint(Constraint.Builder.newInstance()
            .leftExpression(new LiteralExpression("DATETIME"))
            .operator(Operator.LEQ)
            .rightExpression(new LiteralExpression("2025-12-31T23:59:59Z"))
            .build())
        .build())
    .build();
```

### Example 2: Usage Purpose Restriction

```java
// Policy: Only for research purposes
var policy = Policy.Builder.newInstance()
    .permission(Permission.Builder.newInstance()
        .action(Action.Builder.newInstance()
            .type("USE")
            .build())
        .constraint(Constraint.Builder.newInstance()
            .leftExpression(new LiteralExpression("PURPOSE"))
            .operator(Operator.EQ)
            .rightExpression(new LiteralExpression("RESEARCH"))
            .build())
        .build())
    .build();
```

### Example 3: Geographic Restriction

```java
// Policy: Only accessible from EU
var policy = Policy.Builder.newInstance()
    .permission(Permission.Builder.newInstance()
        .action(Action.Builder.newInstance()
            .type("USE")
            .build())
        .constraint(Constraint.Builder.newInstance()
            .leftExpression(new LiteralExpression("REGION"))
            .operator(Operator.EQ)
            .rightExpression(new LiteralExpression("EU"))
            .build())
        .build())
    .prohibition(Prohibition.Builder.newInstance()
        .action(Action.Builder.newInstance()
            .type("DISTRIBUTE")
            .build())
        .build())
    .build();
```

## 🔧 Custom Asset Types

### HTTP API Asset

```java
var apiAsset = Asset.Builder.newInstance()
    .id("customer-api")
    .name("Customer Management API")
    .contentType("application/json")
    .dataAddress(DataAddress.Builder.newInstance()
        .type("HttpData")
        .property("baseUrl", "https://api.mycompany.com/customers")
        .property("method", "GET")
        .property("authType", "bearer")
        .property("secretName", "api-token-secret")
        .build())
    .build();
```

### Database Asset

```java
var dbAsset = Asset.Builder.newInstance()
    .id("sales-database")
    .name("Sales Data Warehouse")
    .contentType("application/sql")
    .dataAddress(DataAddress.Builder.newInstance()
        .type("Sql")
        .property("jdbcUrl", "jdbc:postgresql://localhost:5432/sales")
        .property("table", "quarterly_sales")
        .property("username", "readonly_user")
        .property("password", "vault://db-password")
        .build())
    .build();
```

### S3 Bucket Asset

```java
var s3Asset = Asset.Builder.newInstance()
    .id("product-images")
    .name("Product Image Library")
    .contentType("image/*")
    .dataAddress(DataAddress.Builder.newInstance()
        .type("AmazonS3")
        .property("region", "us-east-1")
        .property("bucketName", "product-images-bucket")
        .property("keyPrefix", "public/")
        .property("accessKeyId", "vault://aws-access-key")
        .property("secretAccessKey", "vault://aws-secret-key")
        .build())
    .build();
```

## 🚀 Deployment Patterns

### Pattern 1: Single Organization, Multiple Connectors

```
Organization XYZ
├── Connector A (Sales Department)
│   └── Shares: CRM data, sales reports
├── Connector B (Engineering)
│   └── Shares: CAD files, specifications
└── Connector C (Finance)
    └── Shares: Budget data, forecasts

Each department maintains sovereignty over their data
```

### Pattern 2: Multi-Tenant Connector

```
Shared EDC Connector
├── Tenant: Company A
│   └── Isolated: Assets, policies, contracts
├── Tenant: Company B
│   └── Isolated: Assets, policies, contracts
└── Tenant: Company C
    └── Isolated: Assets, policies, contracts

Single infrastructure, logical separation
```

### Pattern 3: Federation

```
Industry Data Space (e.g., Automotive)
├── OEM Connectors (BMW, Mercedes, VW)
├── Supplier Connectors (Bosch, Continental)
└── Service Provider Connectors (Logistics)

All follow common standards and policies
```

## 📈 Monitoring & Operations

### Key Metrics to Track

```
Control Plane Metrics:
- Contract negotiations initiated
- Contract negotiations succeeded/failed
- Active contracts
- Policy evaluations per second
- Catalog requests

Data Plane Metrics:
- Active data transfers
- Data volume transferred (GB)
- Transfer success/failure rate
- Average transfer duration
- Concurrent transfers

System Metrics:
- CPU/Memory usage
- API response times
- Database connection pool status
- Extension load status
```

### Health Checks

```powershell
# Basic health
curl http://localhost:8181/api/check/health

# Startup probe (Kubernetes)
curl http://localhost:8181/api/check/startup

# Liveness probe
curl http://localhost:8181/api/check/liveness

# Readiness probe
curl http://localhost:8181/api/check/readiness
```

## 🔒 Security Best Practices

### 1. Never Use In-Memory Storage in Production

```properties
# ❌ BAD (Development only)
edc.asset.store=in-memory

# ✅ GOOD (Production)
edc.datasource.asset.url=jdbc:postgresql://localhost:5432/edc
edc.datasource.asset.user=edc_user
edc.datasource.asset.password=vault://db-password
```

### 2. Always Use HTTPS

```properties
# ✅ GOOD
web.http.management.port=8181
web.http.management.ssl.enabled=true
web.http.management.ssl.keystore.path=/path/to/keystore.jks
web.http.management.ssl.keystore.password=vault://keystore-password
```

### 3. Implement Authentication

```java
// Example: OAuth2 token validation
@Override
public void initialize(ServiceExtensionContext context) {
    var authService = context.getService(AuthenticationService.class);
    
    authService.register(new OAuth2TokenValidator(
        "https://auth.mycompany.com",
        "expected-audience"
    ));
}
```

### 4. Use a Proper Vault

```properties
# ✅ GOOD: HashiCorp Vault
edc.vault.hashicorp.url=https://vault.mycompany.com
edc.vault.hashicorp.token=vault://vault-token
edc.vault.hashicorp.timeout.seconds=30

# ✅ GOOD: Azure Key Vault
edc.vault.azure.keyVault.name=my-keyvault
edc.vault.azure.client.id=xxx
edc.vault.azure.tenant.id=yyy
```

## 🧪 Testing Strategies

### Unit Testing Extensions

```java
@Test
void testAssetCreation() {
    var context = mock(ServiceExtensionContext.class);
    var assetIndex = new InMemoryAssetIndex();
    
    var extension = new SampleDataExtension();
    // Inject mocks
    extension.initialize(context);
    
    var assets = assetIndex.queryAssets(QuerySpec.none());
    assertThat(assets).hasSize(1);
    assertThat(assets.get(0).getId()).isEqualTo("weather-api-asset");
}
```

### Integration Testing

```java
@Test
void testContractNegotiation() {
    // Start two connectors
    var provider = startConnector(providerConfig);
    var consumer = startConnector(consumerConfig);
    
    // Consumer requests catalog
    var catalog = consumer.getCatalog(provider.getDspEndpoint());
    
    // Initiate negotiation
    var negotiationId = consumer.initiateNegotiation(
        catalog.getContractOffers().get(0)
    );
    
    // Wait for agreement
    await().atMost(30, SECONDS).until(() -> 
        consumer.getContractNegotiation(negotiationId)
            .getState() == FINALIZED
    );
}
```

## 📚 Additional Learning Resources

### Books & Papers
- "Data Spaces: Design, Deployment and Future Directions" (IDS)
- "Gaia-X: A Federated Data Infrastructure for Europe"
- IDSA Reference Architecture Model

### Standards
- IDS-G (International Data Spaces)
- Gaia-X Architecture
- DSSC (Data Space Support Center)

### Communities
- Eclipse Dataspace Working Group
- International Data Spaces Association
- Gaia-X Federation Services

### Sample Projects
- MVD (Minimum Viable Dataspace)
- Eclipse Tractus-X
- Catena-X Data Space

## 🎯 Certification Path

```
1. Beginner (You are here!)
   └─ Run basic connector
   └─ Understand core concepts

2. Intermediate
   └─ Set up two connectors
   └─ Perform contract negotiation
   └─ Transfer data between connectors

3. Advanced
   └─ Implement custom policies
   └─ Create custom data plane
   └─ Deploy to production

4. Expert
   └─ Design data space architecture
   └─ Implement governance framework
   └─ Multi-party data sharing
```

---

**Remember**: EDC is not just technology—it's a framework for building trust in data sharing!
