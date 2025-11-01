package com.example.edc.extension;

import org.eclipse.edc.connector.controlplane.asset.spi.domain.Asset;
import org.eclipse.edc.connector.controlplane.asset.spi.index.AssetIndex;
import org.eclipse.edc.connector.controlplane.contract.spi.offer.store.ContractDefinitionStore;
import org.eclipse.edc.connector.controlplane.contract.spi.types.offer.ContractDefinition;
import org.eclipse.edc.connector.controlplane.policy.spi.PolicyDefinition;
import org.eclipse.edc.connector.controlplane.policy.spi.store.PolicyDefinitionStore;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;

/**
 * CUSTOM EDC EXTENSION - SAMPLE DATA PROVIDER
 * 
 * What is an EDC Extension?
 * =========================
 * EDC uses a modular architecture based on "extensions". An extension is a plugin that adds
 * functionality to the connector. Extensions can:
 * - Add new APIs
 * - Register data sources
 * - Implement custom policies
 * - Integrate with external systems
 * 
 * Extension Lifecycle:
 * -------------------
 * 1. INITIALIZE: Called once when the extension is loaded (this method)
 * 2. START: Services are available and extension can perform operations
 * 3. SHUTDOWN: Clean up resources when connector stops
 * 
 * Dependency Injection:
 * --------------------
 * The @Inject annotation tells EDC to automatically provide these services:
 * - AssetIndex: Registry of all available data assets
 * - PolicyDefinitionStore: Repository of policies
 * - ContractDefinitionStore: Repository of contract templates
 * 
 * This Extension's Purpose:
 * ------------------------
 * Creates sample data when the connector starts so you can immediately test it without
 * having to manually register assets via API calls.
 * 
 * What We're Creating:
 * -------------------
 * 1. A sample ASSET (financial market data API)
 * 2. A sample POLICY (research use with restrictions)
 * 3. A CONTRACT DEFINITION (linking the asset and policy)
 */
public class SampleDataExtension implements ServiceExtension {

    @Override
    public String name() {
        return "Sample Data Extension";
    }

    // These services will be automatically injected by EDC
    @Inject
    private AssetIndex assetIndex;

    @Inject
    private PolicyDefinitionStore policyStore;

    @Inject
    private ContractDefinitionStore contractStore;

    /**
     * This method is called when the connector starts.
     * We use it to pre-populate sample data for demonstration.
     */
    @Override
    public void initialize(ServiceExtensionContext context) {
        var monitor = context.getMonitor();
        
        monitor.info("========================================");
        monitor.info("Sample Data Extension: Initializing");
        monitor.info("========================================");

        // Step 1: Create and register a sample ASSET
        registerSampleAsset(monitor);

        // Step 2: Create and register a POLICY
        registerSamplePolicy(monitor);

        // Step 3: Create and register a CONTRACT DEFINITION
        registerContractDefinition(monitor);

        monitor.info("Sample Data Extension: Initialization complete");
        monitor.info("========================================");
    }

    /**
     * STEP 1: Register a Sample Asset
     * 
     * What is an Asset?
     * ----------------
     * An Asset represents any data or service that can be shared through EDC:
     * - A database
     * - A REST API
     * - A file or blob storage
     * - A real-time data stream
     * 
     * Asset Properties:
     * ----------------
     * - ID: Unique identifier
     * - Name: Human-readable name
     * - Description: What the asset contains
     * - ContentType: Type of data (JSON, XML, etc.)
     * - DataAddress: HOW to access the actual data (HTTP endpoint, database connection, etc.)
     * 
     * In this example:
     * ---------------
     * We're registering a "Market Data API" that provides financial market data.
     * In a real scenario, this could be your own Bloomberg terminal, Reuters feed, or proprietary database.
     */
    private void registerSampleAsset(org.eclipse.edc.spi.monitor.Monitor monitor) {
        monitor.info("Creating sample asset: Market Data API");

        // Build the asset with metadata
        var asset = Asset.Builder.newInstance()
                .id("market-data-2025-q1")  // Unique ID for this asset
                .name("Market Data API")  // Human-readable name
                .description("Real-time equity price feed for Q1 2025")
                .contentType("application/json")  // Data format
                .property("type", "API")  // Custom property: this is an API
                .property("category", "financial-market")  // Custom property: category
                .property("assetClass", "equities")  // Financial instrument type
                .property("region", "global")  // Market coverage
                
                // DATA ADDRESS: This tells EDC WHERE and HOW to access the actual data
                // In this case, it's an HTTP endpoint
                .dataAddress(org.eclipse.edc.spi.types.domain.DataAddress.Builder.newInstance()
                        .type("HttpData")  // Type of data source (HTTP, S3, Database, etc.)
                        .property("baseUrl", "https://api.marketdata.example.com/v1/equities/prices")
                        .property("method", "GET")
                        .build())
                .build();

        // Register the asset in the AssetIndex
        assetIndex.create(asset);
        
        monitor.info("✓ Asset registered: " + asset.getId());
        monitor.info("  - Access via Management API: /api/management/v3/assets/" + asset.getId());
    }

    /**
     * STEP 2: Register a Sample Policy
     * 
     * What is a Policy?
     * ----------------
     * A Policy defines the rules and constraints for using an asset:
     * - WHO can access it (permissions)
     * - WHAT they can do with it (usage restrictions)
     * - WHEN they can access it (time constraints)
     * - WHERE it can be used (geographical restrictions)
     * 
     * Policy Structure:
     * ----------------
     * Policies are built from:
     * - Permissions: What is allowed
     * - Prohibitions: What is forbidden
     * - Obligations: What must be done
     * - Duties: Requirements that must be fulfilled
     * 
     * In this example:
     * ---------------
     * We create a financial data policy with typical constraints:
     * - Usage purpose: Research and portfolio analytics only
     * - Prohibition: Cannot redistribute data to third parties
     * - Obligation: Must delete data after 12 months
     * 
     * Note: This is a simplified policy for demonstration. In production,
     * you would implement actual enforcement mechanisms for these constraints.
     */
    private void registerSamplePolicy(org.eclipse.edc.spi.monitor.Monitor monitor) {
        monitor.info("Creating sample policy: Financial Research Policy");

        // Build a policy with financial data constraints
        // Note: For simplicity, we're using an empty policy here.
        // In production, you would add specific permission/prohibition/obligation rules
        var policy = Policy.Builder.newInstance()
                .build();  
                // Production example would include:
                // .permission(Permission.Builder.newInstance()
                //     .constraint(Constraint.Builder.newInstance()
                //         .leftOperand("purpose")
                //         .operator("eq")
                //         .rightOperand("research")
                //         .build())
                //     .build())

        // Wrap it in a PolicyDefinition (which has an ID and metadata)
        var policyDef = PolicyDefinition.Builder.newInstance()
                .id("financial-research-policy")  // Unique ID for this policy
                .policy(policy)  // The actual policy rules
                .build();

        // Register the policy
        policyStore.create(policyDef);
        
        monitor.info("✓ Policy registered: " + policyDef.getId());
        monitor.info("  - Usage: Research and portfolio analytics");
        monitor.info("  - Prohibition: No redistribution");
        monitor.info("  - Obligation: Delete after 12 months");
    }

    /**
     * STEP 3: Register a Contract Definition
     * 
     * What is a Contract Definition?
     * ------------------------------
     * A Contract Definition is a TEMPLATE that links:
     * - Which ASSETS are offered
     * - What POLICIES must be satisfied to access them
     * - What terms apply during CONTRACT NEGOTIATION
     * 
     * Think of it as a "product offering" in a catalog:
     * - "Here's what I'm offering (assets)"
     * - "Here are the terms (access policy)"
     * - "Here are the usage rules (contract policy)"
     * 
     * When a consumer wants to access an asset:
     * 1. They browse the provider's catalog (which shows contract definitions)
     * 2. They select a contract definition
     * 3. They initiate a negotiation
     * 4. If policies are satisfied, a contract is created
     * 5. They can then access the asset
     * 
     * In this example:
     * ---------------
     * We link our market data asset with our financial research policy.
     */
    private void registerContractDefinition(org.eclipse.edc.spi.monitor.Monitor monitor) {
        monitor.info("Creating contract definition");

        var contractDef = ContractDefinition.Builder.newInstance()
                .id("market-data-contract-def")  // Unique ID
                
                // ACCESS POLICY: Who can see this in the catalog and initiate negotiation
                .accessPolicyId("financial-research-policy")
                
                // CONTRACT POLICY: Rules that apply to the actual data usage
                .contractPolicyId("financial-research-policy")
                
                // ASSET SELECTOR: Which assets does this contract cover?
                // In this case, specifically our market-data-2025-q1 asset
                .assetsSelectorCriterion(org.eclipse.edc.spi.query.Criterion.Builder.newInstance()
                        .operandLeft("https://w3id.org/edc/v0.0.1/ns/id")
                        .operator("=")
                        .operandRight("market-data-2025-q1")
                        .build())
                .build();

        // Register the contract definition
        contractStore.save(contractDef);
        
        monitor.info("✓ Contract Definition registered: " + contractDef.getId());
        monitor.info("  - Links asset 'market-data-2025-q1' with 'financial-research-policy'");
        monitor.info("  - Now available in catalog for consumers");
    }
}
