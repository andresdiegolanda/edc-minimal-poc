# ============================================
# TEST CATALOG QUERY
# ============================================
# This script demonstrates the Consumer querying the Provider's catalog.
# This is the core dataspace interaction: discovering what data is available.
#
# Prerequisites:
#   1. Provider running: .\run-provider.ps1 (Terminal 1)
#   2. Consumer running: .\run-consumer.ps1 (Terminal 2)
#   3. Run this script in Terminal 3
#
# What happens:
#   Consumer (port 9181) --> DSP Protocol --> Provider (port 8282)
#   Consumer receives the Provider's catalog listing available assets

Write-Host "============================================" -ForegroundColor Cyan
Write-Host " CATALOG QUERY TEST" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# --------------------------------------------
# Step 1: Verify both connectors are running
# --------------------------------------------
Write-Host "Step 1: Checking connectors..." -ForegroundColor Yellow

# Check Provider
Write-Host "  Checking Provider (port 8181)..." -ForegroundColor Gray
try {
    $providerCheck = Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/market-data-2025-q1" -Method GET -TimeoutSec 5
    Write-Host "  [OK] Provider is running" -ForegroundColor Green
} catch {
    Write-Host "  [FAIL] Provider not responding. Start with: .\run-provider.ps1" -ForegroundColor Red
    exit 1
}

# Check Consumer
Write-Host "  Checking Consumer (port 9181)..." -ForegroundColor Gray
try {
    $consumerCheck = Invoke-RestMethod -Uri "http://localhost:9181/api/management/v3/assets/market-data-2025-q1" -Method GET -TimeoutSec 5
    Write-Host "  [OK] Consumer is running" -ForegroundColor Green
} catch {
    Write-Host "  [FAIL] Consumer not responding. Start with: .\run-consumer.ps1" -ForegroundColor Red
    exit 1
}

Write-Host ""

# --------------------------------------------
# Step 2: Query the Provider's Catalog
# --------------------------------------------
Write-Host "Step 2: Querying Provider's catalog via Consumer..." -ForegroundColor Yellow
Write-Host ""
Write-Host "  Consumer (9181) --> DSP --> Provider (8282)" -ForegroundColor Cyan
Write-Host ""

# Build the catalog request
# The Consumer asks the Provider: "What data do you have available?"
$catalogRequest = @{
    "@context" = @{
        "edc" = "https://w3id.org/edc/v0.0.1/ns/"
    }
    "@type" = "CatalogRequest"
    "counterPartyAddress" = "http://localhost:8282/api/dsp"
    "protocol" = "dataspace-protocol-http"
} | ConvertTo-Json -Depth 10

Write-Host "Request Body:" -ForegroundColor Gray
Write-Host $catalogRequest -ForegroundColor DarkGray
Write-Host ""

try {
    Write-Host "Sending catalog request..." -ForegroundColor Yellow
    
    $response = Invoke-RestMethod `
        -Uri "http://localhost:9181/api/management/v3/catalog/request" `
        -Method POST `
        -ContentType "application/json" `
        -Body $catalogRequest `
        -TimeoutSec 30
    
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Green
    Write-Host " CATALOG QUERY SUCCESSFUL!" -ForegroundColor Green
    Write-Host "============================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Provider's Catalog:" -ForegroundColor Yellow
    Write-Host ""
    
    # Pretty print the response
    $response | ConvertTo-Json -Depth 20
    
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host " WHAT THIS MEANS" -ForegroundColor Cyan
    Write-Host "============================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "The Consumer successfully discovered the Provider's offerings:" -ForegroundColor White
    Write-Host "  - Asset: market-data-2025-q1 (financial market data)" -ForegroundColor Gray
    Write-Host "  - Policy: financial-research-policy (access rules)" -ForegroundColor Gray
    Write-Host "  - Contract: Available for negotiation" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Next step: Contract negotiation (to be implemented)" -ForegroundColor Yellow
    Write-Host ""
    
} catch {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Red
    Write-Host " CATALOG QUERY FAILED" -ForegroundColor Red
    Write-Host "============================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Yellow
    Write-Host "  1. Ensure Provider is running on port 8282 (DSP)" -ForegroundColor Gray
    Write-Host "  2. Ensure Consumer is running on port 9181 (Management)" -ForegroundColor Gray
    Write-Host "  3. Check Provider logs for errors" -ForegroundColor Gray
    Write-Host "  4. Verify network connectivity between ports" -ForegroundColor Gray
    Write-Host ""
    
    # Show response body if available
    if ($_.Exception.Response) {
        try {
            $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
            $responseBody = $reader.ReadToEnd()
            Write-Host "Response Body:" -ForegroundColor Yellow
            Write-Host $responseBody -ForegroundColor Gray
        } catch {
            # Ignore errors reading response
        }
    }
    
    exit 1
}

Write-Host "============================================" -ForegroundColor Cyan
Write-Host " TEST COMPLETE" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
