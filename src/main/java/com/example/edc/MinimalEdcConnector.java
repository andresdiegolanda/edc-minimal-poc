package com.example.edc;

import org.eclipse.edc.boot.system.runtime.BaseRuntime;

/**
 * MINIMAL EDC CONNECTOR - MAIN ENTRY POINT
 * 
 * What is Eclipse Dataspace Components (EDC)?
 * ============================================ EDC is a framework for building
 * "data space connectors" - software components that enable organizations to
 * securely share data while maintaining sovereignty (control) over their data.
 * 
 * Key Concepts: ------------- 1. DATA SPACE: A virtual environment where
 * multiple organizations can share data following common rules and standards.
 * 
 * 2. CONNECTOR: A software component (like this one) that acts as a gateway for
 * data sharing. Each organization has their own connector.
 * 
 * 3. CONTROL PLANE: The "brain" of the connector. It handles: - Contract
 * negotiations (agreeing on terms of data sharing) - Policy enforcement
 * (ensuring data is used according to rules) - Metadata management (what data
 * is available)
 * 
 * 4. DATA PLANE: The "muscle" of the connector. It handles: - Actual data
 * transfer between systems - Data transformation if needed
 * 
 * 5. ASSETS: Data or services that can be shared (e.g., a database, API, file)
 * 
 * 6. POLICIES: Rules that govern how data can be used (e.g., "only for research
 * purposes")
 * 
 * 7. CONTRACT: An agreement between two parties on how data will be shared
 * 
 * How EDC Works (Simplified Flow): -------------------------------- 1. Provider
 * publishes data assets with policies 2. Consumer discovers available assets
 * through the provider's catalog 3. Consumer negotiates a contract for a
 * specific asset 4. Once contract is agreed, consumer can access the data 5.
 * Data is transferred through the data plane
 * 
 * This Minimal PoC: ---------------- This example creates a basic EDC connector
 * that: - Starts both Control Plane and Data Plane - Uses in-memory storage (no
 * database needed for demo) - Exposes a management API on port 8181 - Exposes
 * the DSP protocol endpoint on port 8282
 */
public class MinimalEdcConnector {

	public static void main(String[] args) {
		System.out.println("========================================");
		System.out.println("Starting Minimal EDC Connector PoC");
		System.out.println("========================================");
		System.out.println("");
		System.out.println("What is happening now:");
		System.out.println("1. Loading EDC extensions (modular components)");
		System.out.println("2. Starting Control Plane (contract management)");
		System.out.println("3. Starting Data Plane (data transfer)");
		System.out.println("4. Starting Management API (REST interface)");
		System.out.println("");

		// Create and boot the EDC runtime
		var runtime = new BaseRuntime();

		// Boot the runtime - this will:
		// 1. Load all EDC extensions from the classpath
		// 2. Initialize the dependency injection container
		// 3. Start all services
		runtime.boot(false);

		System.out.println("");
		System.out.println("========================================");
		System.out.println("EDC Connector Started Successfully!");
		System.out.println("========================================");
		System.out.println("");
		System.out.println("Base URLs (not directly accessible):");
		System.out.println("- Management API: http://localhost:8181/api/management/*");
		System.out.println("- DSP Protocol: http://localhost:8282/api/dsp/*");
		System.out.println("");
		System.out.println("Quick test - Get sample asset:");
		System.out.println("  Invoke-RestMethod -Uri 'http://localhost:8181/api/management/v3/assets/market-data-2025-q1'");
		System.out.println("");
		System.out.println("Or run full test suite: .\\test-api.ps1");
		System.out.println("");
	}
}
