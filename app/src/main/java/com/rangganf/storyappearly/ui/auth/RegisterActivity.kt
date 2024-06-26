package com.rangganf.storyappearly.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rangganf.storyappearly.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur layout untuk tampilan registrasi
        setContentView(R.layout.activity_auth)

        // Menyembunyikan ActionBar (Toolbar)
        supportActionBar?.hide()
    }
}
