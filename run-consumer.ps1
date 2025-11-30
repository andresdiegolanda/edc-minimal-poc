# ============================================
# RUN CONSUMER CONNECTOR
# ============================================
# This script starts the EDC Consumer connector.
# The Consumer discovers and requests data from providers.
#
# Ports:
#   - Management API: http://localhost:9181/api/management
#   - DSP Protocol:   http://localhost:9282/api/dsp
#
# Usage:
#   .\run-consumer.ps1
#
# Prerequisites:
#   - Provider connector should be running first (.\run-provider.ps1)
#
# To stop: Press Ctrl+C

Write-Host "============================================" -ForegroundColor Cyan
Write-Host " EDC CONSUMER CONNECTOR" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Role: Data Consumer (discovers and requests data)" -ForegroundColor Yellow
Write-Host ""
Write-Host "Endpoints:" -ForegroundColor Green
Write-Host "  Management API: http://localhost:9181/api/management" -ForegroundColor White
Write-Host "  DSP Protocol:   http://localhost:9282/api/dsp" -ForegroundColor White
Write-Host ""
Write-Host "Provider Connection:" -ForegroundColor Green
Write-Host "  Provider DSP: http://localhost:8282/api/dsp" -ForegroundColor White
Write-Host ""
Write-Host "Test catalog query:" -ForegroundColor Green
Write-Host "  .\test-catalog-query.ps1" -ForegroundColor Gray
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

# Start the consumer connector
java "-Dedc.fs.config=consumer-connector.properties" -jar $jarPath
