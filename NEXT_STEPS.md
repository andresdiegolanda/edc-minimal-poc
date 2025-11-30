# Next Steps: EDC Minimal PoC Development Roadmap

## Current State (v1.1.0)

You have a **two-connector setup** that demonstrates:
- ✅ EDC connector startup and configuration
- ✅ Asset registration (market-data-2025-q1)
- ✅ Policy definition (financial-research-policy)
- ✅ Contract definition (market-data-contract-def)
- ✅ Management API interaction
- ✅ Integration testing
- ✅ **NEW: Provider and Consumer connectors**
- ✅ **NEW: Catalog query via DSP protocol**

---

## What's Been Implemented

### Phase 1: Dual Connector Configuration ✅ COMPLETE

```
┌─────────────────────────────────────────────────────────────────────────┐
│                     CURRENT STATE (v1.1.0)                              │
│                                                                         │
│  ┌──────────────────────┐          ┌──────────────────────┐             │
│  │  PROVIDER CONNECTOR  │          │  CONSUMER CONNECTOR  │             │
│  │                      │   DSP    │                      │             │
│  │  • Owns the data     │◄────────►│  • Requests data     │             │
│  │  • Defines policies  │ Protocol │  • Queries catalog   │             │
│  │  • Approves contracts│          │  • Receives data     │             │
│  │                      │          │                      │             │
│  │  Port: 8181/8282     │          │  Port: 9181/9282     │             │
│  └──────────────────────┘          └──────────────────────┘             │
│                                                                         │
│              ✅ Catalog Query Working!                                 │
└─────────────────────────────────────────────────────────────────────────┘
```

### Files Created

| File                            | Purpose                              |
|---------------------------------|--------------------------------------|
| `provider-connector.properties` | Provider config (ports 8xxx)         |
| `consumer-connector.properties` | Consumer config (ports 9xxx)         |
| `run-provider.ps1`              | Start Provider connector             |
| `run-consumer.ps1`              | Start Consumer connector             |
| `test-catalog-query.ps1`        | Test catalog discovery               |

### How to Test

```powershell
# Terminal 1: Start Provider
.\run-provider.ps1

# Terminal 2: Start Consumer
.\run-consumer.ps1

# Terminal 3: Test catalog query
.\test-catalog-query.ps1
```

---

## Recommended Next Step: Contract Negotiation
edc.participant.id=urn:connector:consumer

# Consumer ports (different from provider)
web.http.management.port=9181
web.http.management.path=/api/management
web.http.protocol.port=9282
web.http.protocol.path=/api/dsp
```

#### Step 1.3: Project Structure
```
edc-minimal-poc/
├── provider-connector.properties    # NEW
├── consumer-connector.properties    # NEW
├── src/
---

## Recommended Next Step: Contract Negotiation

### Phase 3: Contract Negotiation (To Be Implemented)

**Objective**: Consumer requests access, Provider approves

```
┌──────────────────┐                    ┌──────────────────┐
│     CONSUMER     │                    │     PROVIDER     │
│                  │                    │                  │
│  1. "I want      │  ──── DSP ────►    │                  │
│      access"     │                    │                  │
│                  │                    │  2. Checks       │
│                  │                    │     policy       │
│                  │                    │                  │
│                  │  ◄───────────────  │  3. "Access      │
│  4. Contract     │                    │      granted"    │
│     confirmed!   │                    │                  │
└──────────────────┘                    └──────────────────┘
```

**Key Learning**: This is where EDC enforces data sovereignty - the Provider's policies determine who can access what data.

---

### Phase 4: Data Transfer (Future)

**Objective**: Actual data flows from Provider to Consumer

```
┌──────────────────┐                    ┌──────────────────┐
│     CONSUMER     │                    │     PROVIDER     │
│                  │                    │                  │
│  1. "Start       │  ──── DSP ────►    │                  │
│      transfer"   │                    │                  │
│                  │                    │  2. Prepares     │
│                  │                    │     data         │
│                  │                    │                  │
│  3. Receives     │  ◄── DATA ───────  │  4. Sends        │
│     data!        │    (HTTP/S3/etc)   │     data         │
└──────────────────┘                    └──────────────────┘
```

---

## Minimal Implementation Checklist

### Files to Create

| File | Purpose | Complexity |
|------|---------|------------|
| `provider-connector.properties` | Provider config | Simple copy + rename |
| `consumer-connector.properties` | Consumer config (different ports) | Simple copy + modify ports |
| `ConsumerExtension.java` | Consumer startup logic | Optional (can be empty) |
| `run-provider.ps1` | Start provider connector | 2 lines |
| `run-consumer.ps1` | Start consumer connector | 2 lines |
| `test-catalog-query.ps1` | Test catalog discovery | ~10 lines |

### Commands to Add

```powershell
# Terminal 1: Start Provider
java -Dedc.fs.config=provider-connector.properties -jar target/edc-minimal-poc-1.0.0.jar

# Terminal 2: Start Consumer  
java -Dedc.fs.config=consumer-connector.properties -jar target/edc-minimal-poc-1.0.0.jar

# Terminal 3: Test catalog query
.\test-catalog-query.ps1
```

---

## Learning Objectives for v2.0.0

After implementing this, learners will understand:

| Concept | What They'll Learn |
---

## Learning Objectives Achieved (v1.1.0)

| Concept               | Status | What You Learned                                |
|-----------------------|--------|-------------------------------------------------|
| **DSP Protocol**      | ✅     | How connectors communicate securely             |
| **Catalog Federation**| ✅     | How data discovery works across organizations   |
| **Port Configuration**| ✅     | How to run multiple connectors locally          |
| **Contract Negotiation** | 🔜  | How access is requested and granted (next step) |
| **Data Transfer**     | 🔜     | How data flows between connectors (future)      |

---

## Estimated Effort for Next Phase

| Task                              | Time Estimate |
|-----------------------------------|---------------|
| Implement contract negotiation    | 1-2 hours     |
| Create negotiation test script    | 30 minutes    |
| Update documentation              | 30 minutes    |
| **Total for Phase 3**             | **~2-3 hours**|

---

## Alternative Next Steps (Lower Priority)

If contract negotiation is too complex for your current needs, here are simpler alternatives:

### Option A: Add More Assets
- Add 2-3 more sample assets with different properties
- Demonstrates asset variety without new infrastructure

### Option B: Implement Policy Variations
- Create multiple policies (time-based, role-based)
- Shows policy flexibility without connector changes

### Option C: Add Health Monitoring
- Implement a health check endpoint
- Add logging enhancements
- Simpler but less educational about EDC's core purpose

---

## Quick Reference

### Start Two Connectors
```powershell
# Terminal 1
.\run-provider.ps1

# Terminal 2
.\run-consumer.ps1

# Terminal 3
.\test-catalog-query.ps1
```

### Port Reference

| Connector | Management | DSP      | Public |
|-----------|------------|----------|--------|
| Provider  | 8181       | 8282     | 8080   |
| Consumer  | 9181       | 9282     | 9080   |

Good luck with the next phase! 🚀
