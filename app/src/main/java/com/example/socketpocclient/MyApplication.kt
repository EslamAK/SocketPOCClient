package com.example.socketpocclient

import android.app.Application
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URISyntaxException

class MyApplication : Application() {
    lateinit var socket: Socket

    override fun onCreate() {
        super.onCreate()
        try {
            val options = IO.Options()
            options.transports = arrayOf("websocket")
            socket = IO.socket("http://10.0.2.2:3000", options)   // For emulator
//            socket = IO.socket("http://192.168.0.114:3000")   // For real device
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }
}
