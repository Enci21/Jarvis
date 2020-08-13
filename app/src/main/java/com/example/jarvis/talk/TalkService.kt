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

    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    fun findAnswer(input: String): String {
        when (input) {
            HI -> return HELLO

            HELLO -> return HI

            INTRODUCE_YOURSELF -> return IM_JARVIS

            SAY_HELLO -> return HELLO

            LOVE_YOU -> return LOVE_YOU_MORE

            SHOW_DAD -> return OK

        }
        return SORRY_I_CANT_UNDERSTAND
    }

    class TalkBinder : Binder() {

        fun getService(): TalkService {
            return TalkService()
        }
    }
}