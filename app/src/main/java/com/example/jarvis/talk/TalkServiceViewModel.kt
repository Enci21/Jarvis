package com.example.jarvis.talk

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TalkServiceViewModel : ViewModel() {

    val binder: MutableLiveData<TalkService.TalkBinder> = MutableLiveData()
    val serviceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            binder.postValue(null)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val iBinder = service as TalkService.TalkBinder
            binder.postValue(iBinder)
        }
    }

}