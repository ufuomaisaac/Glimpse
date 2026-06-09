package com.example.glimpse

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform