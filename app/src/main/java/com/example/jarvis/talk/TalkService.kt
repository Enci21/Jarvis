package com.example.jarvis.talk

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.jarvis.*

class TalkService : Service() {

    private val binder = TalkBinder()

    override fun onCreate() {
        super.onCreate()
//ez nem kell
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    fun findAnswer(input: String): String {
        return when (input) {
            HI -> HELLO

            HELLO -> HI

            INTRODUCE_YOURSELF -> IM_JARVIS

            SAY_HELLO -> HELLO

            LOVE_YOU -> LOVE_YOU_MORE

            BLUETOOTH_TURN_ON -> BLUETOOTH_ON

            BLUETOOTH_TURN_OFF -> BLUETOOTH_OFF

            BLUETOOTH_ALREADY_ON -> BLUETOOTH_ALREADY_ON

            BLUETOOTH_ALREADY_OFF -> BLUETOOTH_ALREADY_OFF

            SHOW_DAD -> OK

            else -> SORRY_I_CANT_UNDERSTAND
        }
    }

    class TalkBinder : Binder() {

        fun getService(): TalkService {
            return TalkService()
        }
    }
}