# 📚 Eclipse Dataspace Components - Minimal PoC Documentation Index

Welcome to the complete documentation for the EDC Minimal Proof of Concept!

## 🎯 Start Here

**Choose your path based on your goal:**

### 🚀 I want to run it NOW (5 minutes)
👉 **[QUICKSTART.md](QUICKSTART.md)**
- Build in 1 minute
- Run in 30 seconds
- Test in 2 minutes
- No prior knowledge needed

### 📖 I want to understand the concepts
👉 **[README.md](README.md)**
- What is EDC?
- Core concepts explained
- Visual diagrams
- Real-world analogies
- Complete beginner's guide

### 🔍 I want to see how it works internally
👉 **[HOW_IT_WORKS.md](HOW_IT_WORKS.md)**
- Complete flow diagrams
- Startup sequence
- API request flow
- Contract negotiation process
- Data storage architecture

### 🎓 I want advanced examples
👉 **[ADVANCED.md](ADVANCED.md)**
- Real-world use cases
- Complex policy examples
- Deployment patterns
- Security best practices
- Production considerations

### 📊 I want the project overview
👉 **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)**
- What we built
- Project structure
- Learning path
- Technology stack
- Success criteria

## 📁 Documentation Files

### Core Documentation

| File | Purpose | Audience | Time to Read |
|------|---------|----------|--------------|
| **QUICKSTART.md** | Get started fast | Everyone | 5 min |
| **README.md** | Comprehensive guide | Beginners | 30 min |
| **HOW_IT_WORKS.md** | Internal mechanics | Intermediate | 20 min |
| **ADVANCED.md** | Deep dive topics | Advanced | 45 min |
| **PROJECT_SUMMARY.md** | Overview & metrics | All | 10 min |
| **THIS FILE** | Navigation guide | All | 2 min |

### Code Documentation

| File | What It Does | Documentation Level |
|------|--------------|---------------------|
| **MinimalEdcConnector.java** | Main entry point | 🟢🟢🟢🟢🟢 Extensive |
| **SampleDataExtension.java** | Creates sample data | 🟢🟢🟢🟢🟢 Extensive |
| **pom.xml** | Build configuration | 🟢🟢🟢🟢⚪ Well-commented |
| **provider-connector.properties** | Provider config | 🟢🟢🟢🟢🟢 Extensive |
| **consumer-connector.properties** | Consumer config | 🟢🟢🟢🟢🟢 Extensive |
| **logback.xml** | Logging setup | 🟢🟢🟢⚪⚪ Basic |

### Scripts

| File | What It Does |
|------|--------------|
| **run-provider.ps1** | Start Provider connector |
| **run-consumer.ps1** | Start Consumer connector |
| **test-api.ps1** | Quick API tests |
| **test-catalog-query.ps1** | Two-connector catalog query test |

## 🎓 Learning Paths

### Path 1: Complete Beginner (Total: ~2 hours)

```
1. QUICKSTART.md (5 min)
   └─ Get it running
        ↓
2. README.md - Core Concepts section (15 min)
   └─ Understand what EDC is
        ↓
3. MinimalEdcConnector.java (10 min)
   └─ See how it starts
        ↓
4. SampleDataExtension.java (20 min)
   └─ Learn about assets, policies, contracts
        ↓
5. README.md - Testing section (15 min)
   └─ Test the APIs
        ↓
6. HOW_IT_WORKS.md - Complete Lifecycle (15 min)
   └─ See the big picture
        ↓
7. Modify the sample asset (30 min)
   └─ Hands-on learning
        ↓
ACHIEVEMENT UNLOCKED: EDC Beginner! 🎉
```

### Path 2: Experienced Developer (Total: ~1 hour)

```
1. PROJECT_SUMMARY.md (5 min)
   └─ Understand project structure
        ↓
2. README.md - Architecture (10 min)
   └─ Grasp the design
        ↓
3. Code review: Both Java files (15 min)
   └─ Understand implementation
        ↓
4. HOW_IT_WORKS.md - API flows (10 min)
   └─ See request handling
        ↓
5. ADVANCED.md - Deployment patterns (15 min)
   └─ Production considerations
        ↓
6. Build and test (5 min)
   └─ Validate understanding
        ↓
ACHIEVEMENT UNLOCKED: EDC Practitioner! 🚀
```

### Path 3: Architect / Designer (Total: ~45 minutes)

```
1. README.md - What is EDC? (10 min)
   └─ Business context
        ↓
2. ADVANCED.md - Use cases (15 min)
   └─ Real-world scenarios
        ↓
3. ADVANCED.md - Deployment patterns (10 min)
   └─ Architecture options
        ↓
4. HOW_IT_WORKS.md - Complete flow (10 min)
   └─ Technical details
        ↓
ACHIEVEMENT UNLOCKED: EDC Architect! 🏛️
```

## 🗺️ Topic Map

### If You Want to Learn About...

**Data Sovereignty**
- README.md → "What is Eclipse Dataspace Components?"
- ADVANCED.md → "Real-World Use Cases"

**Assets**
- README.md → "Asset" section
- SampleDataExtension.java → `registerSampleAsset()` method
- ADVANCED.md → "Custom Asset Types"

**Policies**
- README.md → "Policy" section
- SampleDataExtension.java → `registerSamplePolicy()` method
- ADVANCED.md → "Policy Examples"

**Contracts**
- README.md → "Contract" section
- SampleDataExtension.java → `registerContractDefinition()` method
- HOW_IT_WORKS.md → "Contract Negotiation Flow"

**Extensions**
- README.md → "What is an EDC Extension?"
- SampleDataExtension.java → Complete file
- HOW_IT_WORKS.md → "Extension Loading Process"

**Configuration**
- provider-connector.properties → Provider config
- consumer-connector.properties → Consumer config
- HOW_IT_WORKS.md → "Configuration Loading"

**Two-Connector Setup**
- COMMANDS.md → "Two-Connector Mode" section
- NEXT_STEPS.md → Implementation status
- test-catalog-query.ps1 → Catalog query test

**Architecture**
- README.md → "How EDC Works"
- HOW_IT_WORKS.md → "Component Architecture"
- PROJECT_SUMMARY.md → "Technology Stack"

**APIs**
- README.md → "Testing the Connector"
- HOW_IT_WORKS.md → "API Request Flow"
- ADVANCED.md → "API examples"

**Security**
- ADVANCED.md → "Security Best Practices"
- config.properties → Security-related settings

**Production Deployment**
- ADVANCED.md → "Deployment Patterns"
- ADVANCED.md → "Security Best Practices"
- PROJECT_SUMMARY.md → "Known Limitations"

## 🎯 Quick Reference

### Common Questions & Where to Find Answers

**Q: How do I run the connector?**  
A: [QUICKSTART.md](QUICKSTART.md) → "Step 2: Run the Connector" or [COMMANDS.md](COMMANDS.md)

**Q: How do I run two connectors?**  
A: [COMMANDS.md](COMMANDS.md) → "Two-Connector Mode"

**Q: What is a data space?**  
A: [README.md](README.md) → "Key Concepts → Data Space"

**Q: How do I create an asset?**  
A: [SampleDataExtension.java](src/main/java/com/example/edc/extension/SampleDataExtension.java) → `registerSampleAsset()`

**Q: How do I test the APIs?**  
A: [README.md](README.md) → "Testing the Connector" or run `.\test-api.ps1`

**Q: How do I test catalog query between connectors?**  
A: Run `.\test-catalog-query.ps1` (requires both connectors running)

**Q: What happens during startup?**  
A: [HOW_IT_WORKS.md](HOW_IT_WORKS.md) → "Startup Sequence"

**Q: How do contracts work?**  
A: [HOW_IT_WORKS.md](HOW_IT_WORKS.md) → "Contract Negotiation Flow"

**Q: What are the limitations?**  
A: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) → "Known Limitations"

**Q: How do I deploy to production?**  
A: [ADVANCED.md](ADVANCED.md) → "Deployment Patterns"

**Q: Where do I go after this PoC?**  
A: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) → "Next Steps for Learners"

**Q: What if I get stuck?**  
A: [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) → "Troubleshooting"

## 🔧 Practical Tasks

### Beginner Tasks

1. **Run the connector**
   - Guide: QUICKSTART.md
   - Time: 5 minutes

2. **View the sample asset**
   - Guide: QUICKSTART.md → "Test 2"
   - Command: `Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets" -Method GET`

3. **Test catalog query (two connectors)**
   - Guide: COMMANDS.md → "Two-Connector Mode"
   - Scripts: `.\run-provider.ps1`, `.\run-consumer.ps1`, `.\test-catalog-query.ps1`

4. **Change the asset name**
   - File: SampleDataExtension.java
   - Method: `registerSampleAsset()`
   - Rebuild and test

### Intermediate Tasks

4. **Add a second asset**
   - Template: SampleDataExtension.java
   - Create new asset with different properties

5. **Create a time-based policy**
   - Guide: ADVANCED.md → "Policy Examples → Time-Limited Access"
   - Implement in SampleDataExtension

6. **Change connector ports**
   - File: config.properties
   - Change `web.http.management.port`

### Advanced Tasks

7. **Set up two connectors**
   - Guide: ADVANCED.md → "Deployment Pattern 1"
   - Test contract negotiation

8. **Add database persistence**
   - Guide: ADVANCED.md → "Extending This PoC → Add a Database Backend"
   - Replace in-memory stores

9. **Implement OAuth2**
   - Guide: ADVANCED.md → "Security Best Practices → Implement Authentication"

## 📈 Progress Tracking

Track your learning journey:

- [ ] Read QUICKSTART.md
- [ ] Run the connector successfully
- [ ] Test all API endpoints
- [ ] Read and understand README.md core concepts
- [ ] Review MinimalEdcConnector.java code
- [ ] Review SampleDataExtension.java code
- [ ] Read HOW_IT_WORKS.md
- [ ] Modify the sample asset
- [ ] Add a second asset
- [ ] Read ADVANCED.md
- [ ] Create a custom policy
- [ ] Set up two connectors
- [ ] Perform contract negotiation

## 🎓 Certification

After completing this PoC, you should be able to:

✅ Explain what EDC is and why it exists  
✅ Describe the difference between control plane and data plane  
✅ Define: Asset, Policy, Contract, Extension  
✅ Run an EDC connector  
✅ Test APIs with curl/Postman  
✅ Create and register assets programmatically  
✅ Write simple policies  
✅ Understand contract negotiation flow  
✅ Explain data sovereignty  
✅ Modify and extend the connector  

## 📞 Need Help?

### Stuck? Check These:

1. **Build errors**: See PROJECT_SUMMARY.md → "Troubleshooting"
2. **Concept confusion**: Re-read README.md relevant section
3. **API errors**: Check HOW_IT_WORKS.md → "API Request Flow"
4. **Configuration issues**: Review config.properties comments

### Still Stuck?

- GitHub Issues: https://github.com/eclipse-edc/Connector/issues
- EDC Discussions: https://github.com/eclipse-edc/Connector/discussions
- Official Docs: https://eclipse-edc.github.io/docs/

## 🎉 Congratulations!

You have access to a complete, well-documented EDC PoC.  
Everything you need to learn is here.  
Start with QUICKSTART.md and enjoy the journey! 🚀

---

**Built with ❤️ for learners**

*"Documentation is not just about what the code does, but why it exists and how to use it."*
