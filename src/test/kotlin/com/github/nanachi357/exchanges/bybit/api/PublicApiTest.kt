package com.github.nanachi357.exchanges.bybit.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.github.nanachi357.exchanges.bybit.api.PublicApi

class PublicApiTest {
    
    @Test
    fun `test symbols endpoint`() {
        println("Testing symbols()...")
        val result = PublicApi.symbols()
        
        assertTrue(result.isSuccess, "Symbols request should succeed")
        val symbols = result.getOrThrow()
        println("Symbols result: ${symbols.list?.size ?: 0} symbols found")
        
        // Basic validation
        assertNotNull(symbols.category, "Category should not be null")
        assertTrue(symbols.list?.isNotEmpty() == true, "Symbols list should not be empty")
    }
    
    @Test
    fun `test klines endpoint`() {
        println("Testing klines(BTC)...")
        val result = PublicApi.klines("BTC")
        
        assertTrue(result.isSuccess, "Klines request should succeed")
        val klines = result.getOrThrow()
        println("Klines result: ${klines.list?.size ?: 0} klines found")
        
        // Basic validation
        assertNotNull(klines.symbol, "Symbol should not be null")
        assertNotNull(klines.category, "Category should not be null")
    }
    
    @Test
    fun `test currencies endpoint`() {
        println("Testing currencies()...")
        val result = PublicApi.currencies()
        
        assertTrue(result.isSuccess, "Currencies request should succeed")
        val currencies = result.getOrThrow()
        println("Currencies result: ${currencies.rows?.size ?: 0} currencies found")
        
        // Basic validation
        assertNotNull(currencies.rows, "Rows should not be null")
    }
    
    @Test
    fun `test fees endpoint`() {
        println("Testing fees(BTCUSDT)...")
        val result = PublicApi.fees("BTCUSDT")
        
        assertTrue(result.isSuccess, "Fees request should succeed")
        val fees = result.getOrThrow()
        println("Fees result: ${fees.list?.size ?: 0} fees found")
        
        // Basic validation
        assertNotNull(fees.list, "Fees list should not be null")
    }
    
    @Test
    fun `test all endpoints integration`() {
        println("Running integration test for all endpoints...")
        
        val symbolsResult = PublicApi.symbols()
        val klinesResult = PublicApi.klines("BTC")
        val currenciesResult = PublicApi.currencies()
        val feesResult = PublicApi.fees("BTCUSDT")
        
        // Check all requests succeeded
        assertTrue(symbolsResult.isSuccess, "Symbols request should succeed")
        assertTrue(klinesResult.isSuccess, "Klines request should succeed")
        assertTrue(currenciesResult.isSuccess, "Currencies request should succeed")
        assertTrue(feesResult.isSuccess, "Fees request should succeed")
        
        val symbols = symbolsResult.getOrThrow()
        val klines = klinesResult.getOrThrow()
        val currencies = currenciesResult.getOrThrow()
        val fees = feesResult.getOrThrow()
        
        println("All endpoints responded successfully")
        println("Results summary:")
        println("  - Symbols: ${symbols.list?.size ?: 0}")
        println("  - Klines: ${klines.list?.size ?: 0}")
        println("  - Currencies: ${currencies.rows?.size ?: 0}")
        println("  - Fees: ${fees.list?.size ?: 0}")
    }
}
