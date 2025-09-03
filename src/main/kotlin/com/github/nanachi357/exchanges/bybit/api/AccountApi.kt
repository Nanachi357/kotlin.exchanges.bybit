package com.github.nanachi357.exchanges.bybit.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.runBlocking
import com.google.gson.Gson
import com.github.nanachi357.exchanges.bybit.model.response.DefaultResponse
import com.github.nanachi357.exchanges.bybit.model.account.Account
import com.github.nanachi357.exchanges.bybit.utils.ApiUtils
import com.github.nanachi357.exchanges.bybit.model.response.balance.*
import com.github.nanachi357.exchanges.bybit.model.response.deposit.*
import com.github.nanachi357.exchanges.bybit.model.response.order.*
import com.github.nanachi357.exchanges.bybit.model.request.order.*
import com.github.nanachi357.exchanges.bybit.model.request.transfer.*
import com.github.nanachi357.exchanges.bybit.model.response.transfer.*
import com.github.nanachi357.exchanges.bybit.model.enums.OrderSide
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object AccountApi {
    
    private val gson = Gson()
    
    // Production network
    private const val BASE = "https://api.bybit.com"
    
    private const val DEPOSITS = "/v5/asset/deposit/query-record"
    private const val ORDERS = "/v5/order/realtime"
    private const val BALANCE = "/v5/asset/transfer/query-account-coin-balance"
    private const val BALANCES = "/v5/asset/transfer/query-account-coins-balance"
    private const val TRANSFER = "/v5/asset/transfer/universal-transfer"
    private const val PLACE_ORDER = "/v5/order/create"
    private const val CANCEL_ORDER = "/v5/order/cancel"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(WebSockets)
    }

    /**
     * Sell coin with limit order
     * 
     * @param account The account to place order for
     * @param coin The coin symbol to sell (e.g., "BTC", "ETH")
     * @param amount The amount to sell
     * @param price The price to sell at
     * @return Result containing NewOrder with order details
     */
    fun sellLimit(account: Account, coin: String, amount: String, price: String): Result<NewOrder> {
        return runCatching {
            val limitOrder = LimitOrder(
                symbol = "${coin}USDT",
                side = OrderSide.Sell,
                qty = amount,
                price = price
            )
            
            val newOrder = limitOrder(account, limitOrder)
            newOrder ?: throw IllegalStateException("Failed to create sell limit order")
        }
    }
    
    /**
     * Buy coin with limit order
     * 
     * @param account The account to place order for
     * @param coin The coin symbol to buy (e.g., "BTC", "ETH")
     * @param amount The amount to buy
     * @param price The price to buy at
     * @return Result containing NewOrder with order details
     */
    fun buyLimit(account: Account, coin: String, amount: String, price: String): Result<NewOrder> {
        return runCatching {
            val limitOrder = LimitOrder(
                symbol = "${coin}USDT",
                side = OrderSide.Buy,
                qty = amount,
                price = price
            )
            
            val newOrder = limitOrder(account, limitOrder)
            newOrder ?: throw IllegalStateException("Failed to create buy limit order")
        }
    }
    
    /**
     * Get balance for specific coin in account
     * 
     * @param account The account to query balance for
     * @param coin The coin symbol (e.g., "BTC", "ETH")
     * @return Result containing SingleBalance with wallet and transfer balances
     */
    fun balance(account: Account, coin: String): Result<SingleBalance> {
        val query = mapOf(
            "accountType" to "FUND",
            "coin" to coin
        )
        
        return runCatching {
            val response = sendRequest<SingleBalanceResponse>(HttpMethod.Get, BALANCE, query = query, account = account)
            
            if (response == null) {
                throw IllegalStateException("Null response for coin: $coin")
            }
            
            response.result?.balance
                ?: throw IllegalStateException("Empty balance response for coin: $coin")
        }
    }

    /**
     * Get all balances for account
     * 
     * @param account The account to query balances for
     * @return Result containing list of all coin balances
     */
    fun balances(account: Account): Result<List<SingleBalance>> {
        val query = mapOf("accountType" to "FUND")
        
        return runCatching {
            val response = sendRequest<Balances>(HttpMethod.Get, BALANCES, query = query, account = account)
            
            if (response == null) {
                throw IllegalStateException("Null balances response")
            }
            
            response.result?.balance
                ?: throw IllegalStateException("Empty balances response")
        }
    }

    /**
     * Get deposit history for account
     * 
     * @param account The account to query deposits for
     * @return Result containing list of deposit transactions
     */
    fun deposits(account: Account): Result<List<DepositTransaction>> {
        return runCatching {
            val response = sendRequest<Deposits>(HttpMethod.Get, DEPOSITS, account = account)
            response?.result?.rows
                ?: throw IllegalStateException("Empty deposits response")
        }
    }

    /**
     * Get all orders for account
     * 
     * @param account The account to query orders for
     * @return Result containing list of all orders
     */
    fun orders(account: Account): Result<List<BybitOrder>> {
        val query = mapOf("category" to "spot", "limit" to "50")
        return runCatching {
            val response = sendRequest<Orders>(HttpMethod.Get, ORDERS, query = query, account = account)
            response?.result?.list
                ?: throw IllegalStateException("Empty orders response")
        }
    }

    /**
     * Get specific order by ID
     * 
     * @param account The account that owns the order
     * @param orderId The unique order identifier
     * @return Result containing the specific order details
     */
    fun order(account: Account, orderId: String): Result<BybitOrder> {
        val query = mapOf(
            "category" to "spot", 
            "limit" to "50", 
            "orderId" to orderId
        )
        return runCatching {
            val response = sendRequest<Orders>(HttpMethod.Get, ORDERS, query = query, account = account)
            response?.result?.list?.firstOrNull()
                ?: throw IllegalStateException("Order not found: $orderId")
        }
    }
    
    /**
     * Cancel existing order
     * 
     * @param account The account that owns the order
     * @param symbol The trading symbol (e.g., "BTCUSDT")
     * @param orderId The unique order identifier to cancel
     * @return Result containing cancellation status
     */
    fun cancelOrder(account: Account, symbol: String, orderId: String): Result<Boolean> {
        return runCatching {
            val cancelOrder = CancelOrder(
                symbol = symbol,
                orderId = orderId
            )
            
            val response = sendRequest<DefaultResponse<Any>>(
                HttpMethod.Post, 
                CANCEL_ORDER, 
                body = cancelOrder, 
                account = account
            )
            
            response?.retCode == 0
        }
    }
    
    // TODO: Implement transfer operations
    // fun transferToMaster(account: Account, coin: String, amount: BigDecimal)
    // fun transferAllToMaster()
    
    /**
     * Create limit order using LimitOrder model
     * 
     * @param account The account to place order for
     * @param limitOrder The LimitOrder request model
     * @return NewOrder response or null if failed
     */
    private fun limitOrder(account: Account, limitOrder: LimitOrder): NewOrder? {
        return runCatching {
            val response = sendRequest<NewOrder>(
                HttpMethod.Post,
                PLACE_ORDER,
                body = limitOrder,
                account = account
            )
            
            response?.result
        }.getOrNull()
    }
    
    private inline fun <reified T> sendRequest(
        httpMethod: HttpMethod,
        prefix: String = "",
        path: String = "",
        body: Any? = null,
        query: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap(),
        account: Account,
    ): DefaultResponse<T>? {
        val json = gson.toJson(body)
        
        val apiHeaders = if (httpMethod == HttpMethod.Get) {
            ApiUtils.genGetSign(query, account)
        } else {
            ApiUtils.genPostSign(json, account)
        }
        
        return runBlocking {
            runCatching {
                client.request {
                    method = httpMethod
                    url(BASE + prefix + path)
                    contentType(ContentType.Application.Json)
                    if (body != null) setBody(body)
                    headers {
                        headers.onEach { append(it.key, it.value) }
                        apiHeaders.onEach { append(it.key, it.value) }
                    }
                    url { query.onEach { parameters.append(it.key, it.value) } }
                }.body<DefaultResponse<T>>()
            }.onFailure { exception ->
                // Silent failure handling
            }.getOrNull()
        }
    }
}
