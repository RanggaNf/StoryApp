package com.rangganf.storyappearly.ui.auth.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rangganf.storyappearly.R
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.remote.response.login.LoginResponse
import com.rangganf.storyappearly.databinding.FragmentLoginBinding
import com.rangganf.storyappearly.utils.Preference
import com.rangganf.storyappearly.utils.ViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menambahkan aksi klik untuk navigasi ke SignUpFragment
        binding.tvLoginNotHaveAccount.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.signUpFragment))

        // Menambahkan aksi klik untuk proses login
        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            // Menutup keyboard setelah tombol login ditekan
            hideKeyboard(it)

            // Mengamati hasil dari proses login menggunakan ViewModel
            loginViewModel.login(email, password).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        processLogin(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // Menjalankan onBackPressed jika fragment ini dipanggil dari SignUpFragment
        val isFromSignUp: Boolean? = arguments?.getBoolean("is_from_sign_up")
        if (isFromSignUp == true) {
            onBackPressed()
        }
    }

    private fun processLogin(data: LoginResponse) {
        if (data.error) {
            Toast.makeText(requireContext(), data.message, Toast.LENGTH_LONG).show()
        } else {
            // Menyimpan token dan melakukan navigasi ke MainActivity
            Preference.saveToken(data.loginResult.token, requireContext())
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            requireActivity().finish()
        }
    }

    private fun onBackPressed() {
        // Menambahkan aksi onBackPressed untuk menutup aktivitas jika dipanggil dari SignUpFragment
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun showLoading(state: Boolean) {
        // Menampilkan atau menyembunyikan komponen UI selama proses loading
        with(binding) {
            barLogin.isVisible = state
            loginEmail.isInvisible = state
            loginPassword.isInvisible = state
            btnLogin.isInvisible = state
            textViewLogin.isInvisible = state
            tvLoginNotHaveAccount.isInvisible = state
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard(view: View) {
        // Menyembunyikan keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
