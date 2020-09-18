package com.example.jarvis

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.jarvis.brain.BrainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissions = arrayOf(
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_CONTACTS
        )
        var askedPermissions = 0

        ActivityCompat.requestPermissions(
            this,
            permissions,
            1
        )

        permissions.forEach {
            if (ContextCompat.checkSelfPermission(
                    this,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            ) askedPermissions += 1
        }

        if (askedPermissions == permissions.size) {
            Handler().postDelayed({
                val intent = Intent(this, BrainActivity::class.java)
                startActivity(intent)
            }, 1500)
        }

    }

}