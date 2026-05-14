package com.rangganf.storyappearly.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rangganf.storyappearly.R
import com.rangganf.storyappearly.utils.Preference

class SplashFragment : Fragment() {

    companion object {
        private const val DURATION: Long = 1500
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menginflate layout fragment_splash
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inisialisasi shared preferences untuk menyimpan informasi autentikasi
        val sharedPref = Preference.initPref(requireContext(), "onSignIn")

        // Mendapatkan token dari shared preferences
        val token = sharedPref.getString("token", "")

        // Inisialisasi default action sebagai navigasi ke LoginFragment
        var action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()

        // Cek apakah token sudah ada (user sudah login) atau belum
        if (token != "") {
            // Jika token sudah ada, ubah action menjadi navigasi ke MainActivity
            action = SplashFragmentDirections.actionSplashFragmentToMainActivity()

            // Setelah menunggu sejenak (DURATION), navigasikan ke MainActivity dan tutup activity ini
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(action)
                requireActivity().finish()
            }, DURATION)
        } else {
            // Jika token belum ada, navigasikan ke LoginFragment setelah menunggu sejenak (DURATION)
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(action)
            }, DURATION)
        }
    }
}