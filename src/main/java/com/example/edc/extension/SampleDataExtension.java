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
 * 1. A sample ASSET (a weather API)
 * 2. A sample POLICY (allowing any use)
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
     * We're registering a "Weather API" that points to a public weather service.
     * In a real scenario, this could be your own internal API or database.
     */
    private void registerSampleAsset(org.eclipse.edc.spi.monitor.Monitor monitor) {
        monitor.info("Creating sample asset: Weather API");

        // Build the asset with metadata
        var asset = Asset.Builder.newInstance()
                .id("weather-api-asset")  // Unique ID for this asset
                .name("Public Weather API")  // Human-readable name
                .description("Provides current weather data for cities worldwide")
                .contentType("application/json")  // Data format
                .property("type", "API")  // Custom property: this is an API
                .property("category", "weather")  // Custom property: category
                
                // DATA ADDRESS: This tells EDC WHERE and HOW to access the actual data
                // In this case, it's an HTTP endpoint
                .dataAddress(org.eclipse.edc.spi.types.domain.DataAddress.Builder.newInstance()
                        .type("HttpData")  // Type of data source (HTTP, S3, Database, etc.)
                        .property("baseUrl", "https://api.weatherapi.com/v1/current.json")
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
     * We create a simple "allow-all" policy for demonstration purposes.
     * In production, you'd have more restrictive policies like:
     * - "Only for research purposes"
     * - "Maximum 1000 requests per day"
     * - "Only accessible from EU region"
     */
    private void registerSamplePolicy(org.eclipse.edc.spi.monitor.Monitor monitor) {
        monitor.info("Creating sample policy: Allow-All Policy");

        // Build a simple policy that allows everything
        var policy = Policy.Builder.newInstance()
                .build();  // Empty policy = no restrictions

        // Wrap it in a PolicyDefinition (which has an ID and metadata)
        var policyDef = PolicyDefinition.Builder.newInstance()
                .id("allow-all-policy")  // Unique ID for this policy
                .policy(policy)  // The actual policy rules
                .build();

        // Register the policy
        policyStore.create(policyDef);
        
        monitor.info("✓ Policy registered: " + policyDef.getId());
        monitor.info("  - Type: Allow-All (no restrictions)");
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
     * We link our weather API asset with our allow-all policy.
     */
    private void registerContractDefinition(org.eclipse.edc.spi.monitor.Monitor monitor) {
        monitor.info("Creating contract definition");

        var contractDef = ContractDefinition.Builder.newInstance()
                .id("weather-contract-def")  // Unique ID
                
                // ACCESS POLICY: Who can see this in the catalog and initiate negotiation
                .accessPolicyId("allow-all-policy")
                
                // CONTRACT POLICY: Rules that apply to the actual data usage
                .contractPolicyId("allow-all-policy")
                
                // ASSET SELECTOR: Which assets does this contract cover?
                // In this case, specifically our weather-api-asset
                .assetsSelectorCriterion(org.eclipse.edc.spi.query.Criterion.Builder.newInstance()
                        .operandLeft("https://w3id.org/edc/v0.0.1/ns/id")
                        .operator("=")
                        .operandRight("weather-api-asset")
                        .build())
                .build();

        // Register the contract definition
        contractStore.save(contractDef);
        
        monitor.info("✓ Contract Definition registered: " + contractDef.getId());
        monitor.info("  - Links asset 'weather-api-asset' with 'allow-all-policy'");
        monitor.info("  - Now available in catalog for consumers");
    }
}
