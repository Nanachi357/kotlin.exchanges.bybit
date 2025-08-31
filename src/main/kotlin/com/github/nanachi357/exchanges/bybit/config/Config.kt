package com.github.nanachi357.exchanges.bybit.config

object Config {
    val API_KEY = System.getenv("BYBIT_API_KEY") ?: throw IllegalStateException("BYBIT_API_KEY environment variable not set")
    val API_SECRET = System.getenv("BYBIT_API_SECRET") ?: throw IllegalStateException("BYBIT_API_SECRET environment variable not set")
    
    const val PLATFORM_NAME = "bybit"
}
