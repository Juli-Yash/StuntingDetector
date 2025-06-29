package com.aplikasistunting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aplikasistunting.R
import com.aplikasistunting.auth.SessionManager

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)

        // Jika sudah login, langsung lempar ke MainActivity
        if (session.isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        // Jika belum login, tampilkan activity_auth.xml (yang akan menampung nav_host_fragment)
        setContentView(R.layout.activity_auth)
    }
}