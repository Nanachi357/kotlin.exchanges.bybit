package com.github.nanachi357.exchanges.bybit.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import com.github.nanachi357.exchanges.bybit.model.account.Account

object ApiUtils {
    private const val RECV_WINDOW = "5000"
    
    private val API_KEY = com.github.nanachi357.exchanges.bybit.config.Config.API_KEY
    private val API_SECRET = com.github.nanachi357.exchanges.bybit.config.Config.API_SECRET
    private const val PLATFORM_NAME = "bybit"

    /**
     * Format withdrawal amount with proper precision
     * TODO: Implement withdrawPrecision method
     * TODO: Add coin repository integration
     */
    fun withdrawFormat(amount: BigDecimal, coin: String = "USDT"): BigDecimal {
        val precision = withdrawPrecision(coin)
        return amount.setScale(precision, RoundingMode.DOWN).stripTrailingZeros()
    }

    /**
     * Get withdrawal precision for specific coin
     * TODO: Implement coin repository integration
     * TODO: Add coin data models
     */
    private fun withdrawPrecision(coin: String): Int {
        // TODO: Replace with actual coin repository call
        // return getCoin(name = coin, platform = PLATFORM_NAME).precision.withdraw
        return 2 // Default precision for now
    }

    /**
     * Get withdrawal chain name for coin and network
     * TODO: Implement networks cache integration
     * TODO: Add network models and repository
     */
    fun withdrawChainName(coin: String, network: String): String {
        // TODO: Replace with actual implementation
        // val currency = getCoin(name = coin, platform = PLATFORM_NAME)
        // return networks.filter { it.info.coin == currency.info.uid }
        //     .filter { it.info.name == network }
        //     .find { it.internal.direction == NetworkDirection.Withdraw }!!
        //     .info.chain
        return network // Return network name as fallback
    }

    /**
     * Get deposit chain name for coin and network
     * TODO: Implement networks cache integration
     * TODO: Add network models and repository
     */
    fun depositChainName(coin: String, network: String): String {
        // TODO: Replace with actual implementation
        // val currency = getCoin(name = coin, platform = PLATFORM_NAME)
        // return networks.filter { it.info.coin == currency.info.uid }
        //     .filter { it.info.name == network }
        //     .find { it.internal.direction == NetworkDirection.Deposit }!!
        //     .info.chain
        return network // Return network name as fallback
    }

    /**
     * Generate signature for GET requests
     * @param params Query parameters map
     * @param subAccount Optional sub-account (currently not implemented)
     * @return Map of authorization headers
     */
    fun genGetSign(params: Map<String, String>, subAccount: Account? = null): Map<String, String> {
        // Validate API configuration
        require(API_KEY.isNotBlank() && API_KEY != "your_api_key_here") { 
            "API_KEY not configured. Set BYBIT_API_KEY environment variable" 
        }
        require(API_SECRET.isNotBlank() && API_SECRET != "your_api_secret_here") { 
            "API_SECRET not configured. Set BYBIT_API_SECRET environment variable" 
        }
        
        val timestamp = Date().time.toString()

        val apikey = subAccount?.api?.public ?: API_KEY
        val secret = subAccount?.api?.secret ?: API_SECRET

        // Encode parameters in URL format
        val query = params.entries.joinToString("&") { (key, value) ->
            "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
        }

        // Create signature string
        val sb = "${timestamp}${apikey}${RECV_WINDOW}${query}"

        // Generate HMAC-SHA256 signature
        val secretKeySpec = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)

        val sign = mac
            .doFinal(sb.toByteArray(StandardCharsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

        return mapOf(
            "X-BAPI-API-KEY" to apikey,
            "X-BAPI-TIMESTAMP" to timestamp,
            "X-BAPI-RECV-WINDOW" to RECV_WINDOW,
            "X-BAPI-SIGN" to sign
        )
    }

    /**
     * Generate signature for POST requests
     * TODO: Implement when POST requests are needed
     * TODO: Add JSON serialization (kotlinx-serialization instead of Gson)
     */
    fun genPostSign(body: Any?, subAccount: Account? = null): Map<String, String> {
        // TODO: Implement POST signature generation
        // TODO: Replace Gson with kotlinx-serialization
        // TODO: Add proper JSON serialization
        throw NotImplementedError("POST signature generation not implemented yet")
    }
}
