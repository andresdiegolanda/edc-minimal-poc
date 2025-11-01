# EDC Management API Testing Script
# This script tests the running EDC connector's Management API

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Testing EDC Connector APIs" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Wait a moment for the connector to fully initialize
Write-Host "Waiting 2 seconds for connector to fully initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 2

# Test 1: Get market data asset
Write-Host ""
Write-Host "1. Getting 'market-data-2025-q1'..." -ForegroundColor Green
try {
    $asset = Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets/market-data-2025-q1" -Method GET
    Write-Host "SUCCESS: Asset retrieved" -ForegroundColor Green
    Write-Host "  - ID: $($asset.'@id')" -ForegroundColor White
    Write-Host "  - Name: $($asset.properties.name)" -ForegroundColor Gray
    Write-Host "  - Content Type: $($asset.properties.contenttype)" -ForegroundColor Gray
    Write-Host "  - Description: $($asset.properties.description)" -ForegroundColor Gray
    Write-Host "  - Asset Class: $($asset.properties.assetClass)" -ForegroundColor Gray
} catch {
    Write-Host "FAILED: Could not retrieve asset" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
}

# Test 2: Get financial research policy
Write-Host ""
Write-Host "2. Getting 'financial-research-policy'..." -ForegroundColor Green
try {
    $policy = Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/policydefinitions/financial-research-policy" -Method GET
    Write-Host "SUCCESS: Policy retrieved" -ForegroundColor Green
    Write-Host "  - ID: $($policy.'@id')" -ForegroundColor White
    Write-Host "  - Type: $($policy.'@type')" -ForegroundColor Gray
} catch {
    Write-Host "FAILED: Could not retrieve policy" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
}

# Test 3: Get contract definition
Write-Host ""
Write-Host "3. Getting 'market-data-contract-def'..." -ForegroundColor Green
try {
    $contract = Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/contractdefinitions/market-data-contract-def" -Method GET
    Write-Host "SUCCESS: Contract definition retrieved" -ForegroundColor Green
    Write-Host "  - ID: $($contract.'@id')" -ForegroundColor White
    Write-Host "  - Access Policy: $($contract.accessPolicyId)" -ForegroundColor Gray
    Write-Host "  - Contract Policy: $($contract.contractPolicyId)" -ForegroundColor Gray
} catch {
    Write-Host "FAILED: Could not retrieve contract definition" -ForegroundColor Red
    Write-Host "Error: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Testing Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
