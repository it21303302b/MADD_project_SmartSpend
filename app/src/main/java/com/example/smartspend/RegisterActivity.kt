package com.example.smartspend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val TV_alreadyLogin = findViewById<TextView>(R.id.TV_alreadyLogin)
        TV_alreadyLogin.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}