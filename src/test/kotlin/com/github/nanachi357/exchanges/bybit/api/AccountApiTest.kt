package com.github.nanachi357.exchanges.bybit.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import com.github.nanachi357.exchanges.bybit.model.account.Account
import com.github.nanachi357.exchanges.bybit.model.account.sub.*
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

class AccountApiTest {
    
    // Real account using environment API keys
    private val testAccount = run {
        val info = Info(
            uid = "test_uid",
            email = "test@example.com",
            platform = "bybit",
            note = null
        )
        
        val status = Status(
            busy = false,
            enabled = true
        )
        
        val api = Api(
            public = System.getenv("BYBIT_API_KEY") ?: "test_api_key",
            secret = System.getenv("BYBIT_API_SECRET") ?: "test_api_secret"
        )
        
        val security = Security(
            password = null
        )
        
        Account(
            info = info,
            status = status,
            api = api,
            security = security
        )
    }
    
    // ============================================================================
    // READ OPERATIONS TESTS
    // ============================================================================
    
    @Test
    fun `test balance endpoint`() {
        println("Testing balance(BTC)...")
        val result = AccountApi.balance(testAccount, "BTC")
        
        // Log error BEFORE assertTrue
        if (result.isFailure) {
            println("Balance request failed: ${result.exceptionOrNull()?.message}")
            result.exceptionOrNull()?.printStackTrace()
        }
        
        assertTrue(result.isSuccess, "Balance request should succeed")
        val balance = result.getOrThrow()
        println("Balance result: ${balance.walletBalance} BTC")
        
        // Basic validation
        assertNotNull(balance.coin, "Coin should not be null")
        assertTrue(balance.coin == "BTC", "Coin should be BTC")
    }
    
    @Test
    fun `test balances endpoint`() {
        println("Testing balances()...")
        
        val result = try {
            AccountApi.balances(testAccount)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        
        if (result.isFailure) {
            println("=== BALANCES ERROR DETAILS ===")
            println("Exception type: ${result.exceptionOrNull()?.javaClass?.simpleName}")
            println("Exception message: ${result.exceptionOrNull()?.message}")
            println("Full exception:")
            result.exceptionOrNull()?.printStackTrace()
            println("=== END BALANCES ERROR ===")
        }
        
        assertTrue(result.isSuccess, "Balances request should succeed")
        val balances = result.getOrThrow()
        println("Balances result: ${balances.size} coins found")
        
        // Basic validation
        assertNotNull(balances, "Balances list should not be null")
        assertTrue(balances.isNotEmpty(), "Balances list should not be empty")
    }
    
    @Test
    fun `test deposits endpoint`() {
        println("Testing deposits()...")
        val result = AccountApi.deposits(testAccount)
        
        assertTrue(result.isSuccess, "Deposits request should succeed")
        val deposits = result.getOrThrow()
        println("Deposits result: ${deposits.size} deposits found")
        
        // Basic validation
        assertNotNull(deposits, "Deposits list should not be null")
        // Deposits might be empty for new accounts, so we don't assert isNotEmpty()
    }
    
    @Test
    fun `test orders endpoint`() {
        println("Testing orders()...")
        val result = AccountApi.orders(testAccount)
        
        assertTrue(result.isSuccess, "Orders request should succeed")
        val orders = result.getOrThrow()
        println("Orders result: ${orders.size} orders found")
        
        // Basic validation
        assertNotNull(orders, "Orders list should not be null")
        // Orders might be empty for new accounts, so we don't assert isNotEmpty()
        
        // If orders exist, show their details
        if (orders.isNotEmpty()) {
            println("=== FOUND ORDERS ===")
            orders.forEach { order ->
                println("Order ID: ${order.orderId}")
                println("Symbol: ${order.symbol}")
                println("Side: ${order.side}")
                println("Order Type: ${order.orderType}")
                println("Quantity: ${order.qty}")
                println("Price: ${order.price}")
                println("Status: ${order.orderStatus}")
                println("---")
            }
        } else {
            println("No orders found in the account")
        }
    }
    
    @Test
    fun `test order by ID endpoint`() {
        println("Testing order by ID...")
        
        // First get all orders to find an order ID
        val ordersResult = AccountApi.orders(testAccount)
        if (ordersResult.isSuccess && ordersResult.getOrThrow().isNotEmpty()) {
            val orderId = ordersResult.getOrThrow().first().orderId
            println("Testing with order ID: $orderId")
            
            val result = AccountApi.order(testAccount, orderId)
            assertTrue(result.isSuccess, "Order by ID request should succeed")
            
            val order = result.getOrThrow()
            println("Order result: ${order.symbol} - ${order.orderStatus}")
            
            // Basic validation
            assertNotNull(order.orderId, "Order ID should not be null")
            assertTrue(order.orderId == orderId, "Order ID should match")
            
            // Show detailed order information
            println("=== ORDER DETAILS ===")
            println("Order ID: ${order.orderId}")
            println("Symbol: ${order.symbol}")
            println("Side: ${order.side}")
            println("Order Type: ${order.orderType}")
            println("Quantity: ${order.qty}")
            println("Price: ${order.price}")
            println("Status: ${order.orderStatus}")
            println("Created Time: ${order.createdTime}")
            println("Updated Time: ${order.updatedTime}")
        } else {
            println("No orders found, skipping order by ID test")
        }
    }
    
    @Test
    fun `test all endpoints integration`() {
        println("Running integration test for all AccountApi endpoints...")
        
        val balanceResult = AccountApi.balance(testAccount, "BTC")
        val balancesResult = AccountApi.balances(testAccount)
        val depositsResult = AccountApi.deposits(testAccount)
        val ordersResult = AccountApi.orders(testAccount)
        
        // Check all requests succeeded
        assertTrue(balanceResult.isSuccess, "Balance request should succeed")
        assertTrue(balancesResult.isSuccess, "Balances request should succeed")
        assertTrue(depositsResult.isSuccess, "Deposits request should succeed")
        assertTrue(ordersResult.isSuccess, "Orders request should succeed")
        
        val balance = balanceResult.getOrThrow()
        val balances = balancesResult.getOrThrow()
        val deposits = depositsResult.getOrThrow()
        val orders = ordersResult.getOrThrow()
        
        println("All AccountApi endpoints responded successfully")
        println("Results summary:")
        println("  - BTC Balance: ${balance.walletBalance}")
        println("  - Total Coins: ${balances.size}")
        println("  - Orders: ${orders.size}")
    }
    
    // ============================================================================
    // WRITE OPERATIONS TESTS - TEMPLATES
    // ============================================================================
    // These are template tests for POST operations - currently commented out
    
    /*
    @Test
    fun `test sell limit order creation template`() {
        // Template for testing sell limit order creation
        val result = AccountApi.sellLimit(
            account = testAccount,
            coin = "BTC",
            amount = "0.001",
            price = "999999"
        )
        
        // Handle both success and business logic errors
        if (result.isSuccess) {
            val order = result.getOrThrow()
            println("Sell order created: ${order.orderId}")
            // Test cancellation if needed
        } else {
            val exception = result.exceptionOrNull()
            if (exception?.message?.contains("Insufficient balance") == true ||
                exception?.message?.contains("Order value exceeded lower limit") == true ||
                exception?.message?.contains("170131") == true ||
                exception?.message?.contains("170140") == true) {
                println("Test PASSED: API correctly rejected order due to business logic")
                return
            } else {
                throw exception ?: RuntimeException("Unknown error occurred")
            }
        }
    }
    
    @Test
    fun `test buy limit order creation template`() {
        // Template for testing buy limit order creation
        val result = AccountApi.buyLimit(
            account = testAccount,
            coin = "BTC",
            amount = "0.001",
            price = "1"
        )
        
        // Handle both success and business logic errors
        if (result.isSuccess) {
            val order = result.getOrThrow()
            println("Buy order created: ${order.orderId}")
            // Test cancellation if needed
        } else {
            val exception = result.exceptionOrNull()
            if (exception?.message?.contains("Insufficient balance") == true ||
                exception?.message?.contains("Order value exceeded lower limit") == true ||
                exception?.message?.contains("170131") == true ||
                exception?.message?.contains("170140") == true) {
                println("Test PASSED: API correctly rejected order due to business logic")
                return
            } else {
                throw exception ?: RuntimeException("Unknown error occurred")
            }
        }
    }
    
    @Test
    fun `test order cancellation template`() {
        // Template for testing order cancellation
        // First create a test order
        val orderResult = AccountApi.sellLimit(
            account = testAccount,
            coin = "ETH",
            amount = "0.01",
            price = "999999"
        )
        
        if (orderResult.isSuccess) {
            val order = orderResult.getOrThrow()
            println("Created order for cancellation test: ${order.orderId}")
            
            // Wait for order processing
            Thread.sleep(1000)
            
            // Cancel the order
            order.orderId?.let { orderId ->
                val cancelResult = AccountApi.cancelOrder(
                    account = testAccount,
                    symbol = "ETHUSDT",
                    orderId = orderId
                )
                
                assertTrue(cancelResult.isSuccess, "Order cancellation should succeed")
                val cancelled = cancelResult.getOrThrow()
                assertTrue(cancelled, "Order should be cancelled successfully")
            }
        } else {
            // Handle business logic errors
            val exception = orderResult.exceptionOrNull()
            if (exception?.message?.contains("Insufficient balance") == true ||
                exception?.message?.contains("Order value exceeded lower limit") == true ||
                exception?.message?.contains("170131") == true ||
                exception?.message?.contains("170140") == true) {
                println("Test PASSED: API correctly rejected order due to business logic")
                return
            } else {
                throw exception ?: RuntimeException("Unknown error occurred")
            }
        }
    }
    
    @Test
    fun `test write operations integration template`() {
        // Template for testing all write operations integration
        
        // Test 1: Create sell order
        val sellResult = AccountApi.sellLimit(
            account = testAccount,
            coin = "BTC",
            amount = "0.001",
            price = "999999"
        )
        
        if (sellResult.isSuccess) {
            val sellOrder = sellResult.getOrThrow()
            println("Sell order created: ${sellOrder.orderId}")
            
            // Test cancellation
            sellOrder.orderId?.let { sellOrderId ->
                val cancelSellResult = AccountApi.cancelOrder(
                    account = testAccount,
                    symbol = "BTCUSDT",
                    orderId = sellOrderId
                )
                assertTrue(cancelSellResult.isSuccess, "Sell order cancellation should succeed")
            }
        } else {
            // Handle business logic errors
            val exception = sellResult.exceptionOrNull()
            if (exception?.message?.contains("Insufficient balance") == true ||
                exception?.message?.contains("Order value exceeded lower limit") == true ||
                exception?.message?.contains("170131") == true ||
                exception?.message?.contains("170140") == true) {
                println("Sell order test PASSED: API correctly rejected order due to business logic")
            } else {
                throw exception ?: RuntimeException("Unknown error occurred in sell order")
            }
        }
        
        // Test 2: Create buy order
        val buyResult = AccountApi.buyLimit(
            account = testAccount,
            coin = "BTC",
            amount = "0.001",
            price = "1"
        )
        
        if (buyResult.isSuccess) {
            val buyOrder = buyResult.getOrThrow()
            println("Buy order created: ${buyOrder.orderId}")
            
            // Test cancellation
            buyOrder.orderId?.let { buyOrderId ->
                val cancelBuyResult = AccountApi.cancelOrder(
                    account = testAccount,
                    symbol = "BTCUSDT",
                    orderId = buyOrderId
                )
                assertTrue(cancelBuyResult.isSuccess, "Buy order cancellation should succeed")
            }
        } else {
            // Handle business logic errors
            val exception = buyResult.exceptionOrNull()
            if (exception?.message?.contains("Insufficient balance") == true ||
                exception?.message?.contains("Order value exceeded lower limit") == true ||
                exception?.message?.contains("170131") == true ||
                exception?.message?.contains("170140") == true) {
                println("Buy order test PASSED: API correctly rejected order due to business logic")
            } else {
                throw exception ?: RuntimeException("Unknown error occurred in buy order")
            }
        }
        
        println("All write operations completed successfully")
    }
    */
    
    // Helper method for order cancellation testing
    private fun testOrderCancellation(orderId: String, symbol: String) {
        // Wait for order processing
        Thread.sleep(1000)
        
        val cancelResult = AccountApi.cancelOrder(
            account = testAccount,
            symbol = symbol,
            orderId = orderId
        )
        
        if (cancelResult.isSuccess) {
            val cancelled = cancelResult.getOrThrow()
            println("Order $orderId cancelled successfully: $cancelled")
            assertTrue(cancelled, "Order should be cancelled successfully")
        } else {
            println("Order $orderId cancellation failed: ${cancelResult.exceptionOrNull()?.message}")
            // Don't fail the test if cancellation fails - order might already be filled
        }
    }
}
