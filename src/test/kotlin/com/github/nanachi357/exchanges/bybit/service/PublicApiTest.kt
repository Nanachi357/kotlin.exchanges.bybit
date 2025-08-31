package com.github.nanachi357.exchanges.bybit.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PublicApiTest {
    
    @Test
    fun `test symbols endpoint`() {
        println("Testing symbols()...")
        val symbols = PublicApi.symbols()
        
        assertNotNull(symbols, "Symbols response should not be null")
        println("Symbols result: ${symbols?.list?.size ?: 0} symbols found")
        
        // Basic validation
        assertNotNull(symbols?.category, "Category should not be null")
        assertTrue(symbols?.list?.isNotEmpty() == true, "Symbols list should not be empty")
    }
    
    @Test
    fun `test klines endpoint`() {
        println("Testing klines(BTC)...")
        val klines = PublicApi.klines("BTC")
        
        assertNotNull(klines, "Klines response should not be null")
        println("Klines result: ${klines?.list?.size ?: 0} klines found")
        
        // Basic validation
        assertNotNull(klines?.symbol, "Symbol should not be null")
        assertNotNull(klines?.category, "Category should not be null")
    }
    
    @Test
    fun `test currencies endpoint`() {
        println("Testing currencies()...")
        val currencies = PublicApi.currencies()
        
        assertNotNull(currencies, "Currencies response should not be null")
        println("Currencies result: ${currencies?.rows?.size ?: 0} currencies found")
        
        // Basic validation
        assertNotNull(currencies?.rows, "Rows should not be null")
    }
    
    @Test
    fun `test fees endpoint`() {
        println("Testing fees(BTCUSDT)...")
        val fees = PublicApi.fees("BTCUSDT")
        
        assertNotNull(fees, "Fees response should not be null")
        println("Fees result: ${fees?.list?.size ?: 0} fees found")
        
        // Basic validation
        assertNotNull(fees?.list, "Fees list should not be null")
    }
    
    @Test
    fun `test all endpoints integration`() {
        println("Running integration test for all endpoints...")
        
        try {
            val symbols = PublicApi.symbols()
            val klines = PublicApi.klines("BTC")
            val currencies = PublicApi.currencies()
            val fees = PublicApi.fees("BTCUSDT")
            
            println("All endpoints responded successfully")
            println("Results summary:")
            println("  - Symbols: ${symbols?.list?.size ?: 0}")
            println("  - Klines: ${klines?.list?.size ?: 0}")
            println("  - Currencies: ${currencies?.rows?.size ?: 0}")
            println("  - Fees: ${fees?.list?.size ?: 0}")
            
        } catch (e: Exception) {
            fail("Integration test failed: ${e.message}")
        }
    }
}
