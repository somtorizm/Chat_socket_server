package com.example.member

import com.example.data.MessageDataSource
import com.example.data.model.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageDataSource
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(username: String, sessionId: String, socketSession: WebSocketSession) {
        if (members.containsKey(username)) throw MemberAlreadyExists()

        members[username] = Member(username, sessionId, socketSession)

    }

    suspend fun sendMessage(senderUsername: String, message: String) {
        members.values.forEach { members ->

            val messageEntity = Message(
                text = message,
                username = senderUsername,
                timeStamp = System.currentTimeMillis()
            )

            messageDataSource.insertMessage(messageEntity)

            val parsedMessage = Json.encodeToString(messageEntity)

            members.socketSession.send(Frame.Text(parsedMessage))
        }

    }

    suspend fun getAllMessages() : List<Message> {
        return  messageDataSource.getAllMessages()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.socketSession?.close()
        if (members.containsKey(username)) {
            members.remove(username)
        }
    }
}