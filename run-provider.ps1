# ============================================
# RUN PROVIDER CONNECTOR
# ============================================
# This script starts the EDC Provider connector.
# The Provider owns data and shares it with consumers.
#
# Ports:
#   - Management API: http://localhost:8181/api/management
#   - DSP Protocol:   http://localhost:8282/api/dsp
#
# Usage:
#   .\run-provider.ps1
#
# To stop: Press Ctrl+C

Write-Host "============================================" -ForegroundColor Cyan
Write-Host " EDC PROVIDER CONNECTOR" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Role: Data Provider (owns and shares data)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Endpoints:" -ForegroundColor Green
Write-Host "  Management API: http://localhost:8181/api/management" -ForegroundColor White
Write-Host "  DSP Protocol:   http://localhost:8282/api/dsp" -ForegroundColor White
Write-Host ""
Write-Host "Sample Data:" -ForegroundColor Green
Write-Host "  Asset:    market-data-2025-q1" -ForegroundColor White
Write-Host "  Policy:   financial-research-policy" -ForegroundColor White
Write-Host "  Contract: market-data-contract-def" -ForegroundColor White
Write-Host ""
Write-Host "Test commands:" -ForegroundColor Green
Write-Host '  Invoke-RestMethod -Uri "http://localhost:8181/api/management/v3/assets" -Method GET' -ForegroundColor Gray
Write-Host ""
Write-Host "Starting..." -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Check if JAR exists
$jarPath = "target\edc-minimal-poc-1.0.0.jar"
if (-not (Test-Path $jarPath)) {
    Write-Host "ERROR: JAR file not found at $jarPath" -ForegroundColor Red
    Write-Host "Run 'mvn clean package' first." -ForegroundColor Yellow
    exit 1
}

# Start the provider connector
java "-Dedc.fs.config=provider-connector.properties" -jar $jarPath
