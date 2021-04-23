package com.example.socketpocclient

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.socket.client.Socket
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var socket: Socket
    private lateinit var id: String
    private var broadcastsCount = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        socket = (application as MyApplication).socket
        if (socket.connected()) id = socket.id()

        socket.on(Socket.EVENT_CONNECT) {
            runOnUiThread {
                id = socket.id()
                textViewIdentity.text = "Socket with id: $id is connected ✅"
                Log.d("Eslam", "Socket with id: $id is connected ✅")
            }
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            runOnUiThread {
                textViewIdentity.text = "Socket with id: $id is disconnected 🚫"
                Log.d("Eslam", "Socket with id: $id is disconnected 🚫")
            }
        }

        socket.on("pong") {
            runOnUiThread {
                Toast.makeText(applicationContext, "Pong is received ✅", Toast.LENGTH_SHORT).show()
            }
        }

        socket.on("broadcast") {
            runOnUiThread {
                broadcastsCount++
                textViewCounter.text = "Broadcast count = $broadcastsCount"
            }
        }

        buttonConnect.setOnClickListener { socket.connect() }

        buttonDisconnect.setOnClickListener { socket.disconnect() }

        buttonSendPing.setOnClickListener { socket.emit("ping") }

        buttonSendVolatilePing.setOnClickListener { if (socket.connected()) socket.emit("ping") }

        buttonSendBroadcast.setOnClickListener { socket.emit("broadcast") }

        buttonSendThousand.setOnClickListener { repeat(1000) {socket.emit("broadcast")} }

        buttonRunOnBackground.setOnClickListener {
            val intent = Intent(applicationContext, MyService::class.java)
            ContextCompat.startForegroundService(this, intent)
        }
    }

    override fun onDestroy() {
        socket.disconnect()
        super.onDestroy()
    }
}
