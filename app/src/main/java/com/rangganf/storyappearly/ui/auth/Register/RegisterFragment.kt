package com.rangganf.storyappearly.ui.auth.Register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rangganf.storyappearly.R
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.data.remote.response.Register.RegisterResponse
import com.rangganf.storyappearly.databinding.FragmentRegisterBinding
import com.rangganf.storyappearly.utils.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menambahkan aksi klik untuk navigasi ke LoginFragment
        binding.tvRegisterHaveAccount.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.loginFragment))

        binding.btSignUp.setOnClickListener { view ->
            val name = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()

            // Menutup keyboard setelah tombol sign up ditekan
            hideKeyboard(view)

            // Mengamati hasil dari proses sign up menggunakan ViewModel
            registerViewModel.signUp(name, email, password).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        processSignUp(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(context, result.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun processSignUp(data: RegisterResponse) {
        if (data.error) {
            Toast.makeText(requireContext(), "Gagal Sign Up", Toast.LENGTH_LONG).show()
        } else {
            // Menampilkan pesan toast jika sign up berhasil dan melakukan navigasi ke LoginFragment
            Toast.makeText(requireContext(), "Sign Up berhasil, silahkan login!", Toast.LENGTH_LONG).show()
            findNavController().navigate(RegisterFragmentDirections.actionSignUpFragmentToLoginFragment(isFromSignUp = true))
        }
    }

    private fun showLoading(state: Boolean) {
        // Menampilkan atau menyembunyikan komponen UI selama proses loading
        binding.barCreateSignup.isVisible = state
        binding.registerEmail.isInvisible = state
        binding.registerName.isInvisible = state
        binding.registerPassword.isInvisible = state
        binding.textView2.isInvisible = state
        binding.tvRegisterHaveAccount.isInvisible = state
        binding.btSignUp.isInvisible = state
    }

    private fun hideKeyboard(view: View) {
        // Menyembunyikan keyboard
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
