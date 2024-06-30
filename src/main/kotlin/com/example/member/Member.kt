package com.example.member

import io.ktor.websocket.*

data class Member(
    val username: String,
    val sessionId: String,
    val socketSession: WebSocketSession,

)
