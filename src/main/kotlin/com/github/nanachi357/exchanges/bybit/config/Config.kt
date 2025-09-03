package com.github.nanachi357.exchanges.bybit.config

object Config {
    var API_KEY : String = System.getenv("BYBIT_API_KEY")
    var API_SECRET : String = System.getenv("BYBIT_API_SECRET")
    
    const val PLATFORM_NAME = "bybit"
}
