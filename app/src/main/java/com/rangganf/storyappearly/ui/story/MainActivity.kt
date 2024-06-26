package com.rangganf.storyappearly.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rangganf.storyappearly.R
import com.rangganf.storyappearly.databinding.ActivityMainBinding
import com.rangganf.storyappearly.ui.auth.RegisterActivity
import com.rangganf.storyappearly.ui.maps.MapActivity
import com.rangganf.storyappearly.utils.Preference

class MainActivity : AppCompatActivity() {

    // Binding untuk ActivityMain
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    // Metode yang dipanggil saat izin diminta oleh pengguna
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // Memeriksa apakah semua izin diberikan
            if (!allPermissionsGranted()) {
                showToast("Tidak mendapatkan izin kamera.")
                finish()
            }
        }
    }

    // Fungsi untuk memeriksa apakah semua izin telah diberikan
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // Metode yang dipanggil saat aktivitas dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menginisialisasi binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memeriksa dan meminta izin jika belum diberikan
        if (!allPermissionsGranted()) {
            requestPermissions()
        }
    }

    // Metode untuk membuat opsi menu di ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Metode yang dipanggil saat opsi menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout_menu -> {
                // Menjalankan fungsi logout dari Preference dan memulai aktivitas RegisterActivity
                Preference.logOut(this)
                startRegisterActivity()
            }
            R.id.action_map -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Fungsi untuk menampilkan Toast dengan pesan yang diberikan
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Fungsi untuk meminta izin
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    // Fungsi untuk memulai RegisterActivity
    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
