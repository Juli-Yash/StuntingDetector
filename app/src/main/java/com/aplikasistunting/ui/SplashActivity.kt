package com.aplikasistunting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aplikasistunting.auth.SessionManager
import com.aplikasistunting.ui.AuthActivity
import com.aplikasistunting.ui.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        val intent = if (session.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java) // menampilkan login/register
        }

        startActivity(intent)
        finish()
    }
}
