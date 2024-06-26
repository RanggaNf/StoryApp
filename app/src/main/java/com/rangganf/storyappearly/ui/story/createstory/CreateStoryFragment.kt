package com.rangganf.storyappearly.ui.story.createstory

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rangganf.storyappearly.R
import com.rangganf.storyappearly.data.Result
import com.rangganf.storyappearly.databinding.FragmentCreateStoryBinding
import com.rangganf.storyappearly.utils.ViewModelFactory
import com.rangganf.storyappearly.utils.reduceFileImage
import com.rangganf.storyappearly.utils.rotateBitmap
import com.rangganf.storyappearly.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryFragment : Fragment() {
    private var _binding: FragmentCreateStoryBinding? = null
    private val binding get() = _binding!!

    private val createStoryViewModel: CreateStoryViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    // Fungsi ini dipanggil ketika tata letak fragment dibuat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout fragment_create_story.xml untuk tata letak ini
        _binding = FragmentCreateStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Fungsi ini dipanggil setelah tata letak fragment telah dibuat
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menetapkan listener untuk membuka kamera
        binding.btnOpenCamera.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.cameraFragment))

        // Menetapkan listener untuk membuka galeri
        binding.btnOpenGallery.setOnClickListener {
            startGallery()
        }

        // Menetapkan listener untuk menambahkan cerita
        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }

        // Mendapatkan URI gambar dari argumen jika ada
        val fileUri = arguments?.get("selected_image")
        if (fileUri != null) {
            // Memproses URI untuk menampilkan gambar yang dipilih
            val uri: Uri = fileUri as Uri
            val isBackCamera = arguments?.get("isBackCamera") as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(uri.path),
                isBackCamera
            )
            getFile = uri.toFile()
            binding.imagePreview.setImageBitmap(result)
        }
    }

    private var getFile: File? = null

    // Fungsi untuk memulai galeri untuk memilih gambar
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    // Pengaturan kontrak untuk hasil aktivitas memilih gambar dari galeri
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            // Mendapatkan URI gambar yang dipilih dan menampilkan di ImageView
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.imagePreview.setImageURI(selectedImg)
        }
    }

    // Fungsi untuk mengunggah gambar ke server
    private fun uploadImage() {
        if (getFile != null) {
            // Menampilkan loading UI
            showLoading(true)

            // Mengurangi ukuran file gambar untuk pengiriman yang lebih efisien
            val file = reduceFileImage(getFile as File)

            // Mendapatkan teks deskripsi dari EditText
            val descriptionText = binding.addDescription.text.toString()

            if (descriptionText.isNotEmpty()) {
                // Membuat RequestBody untuk teks deskripsi dan file gambar
                val description = descriptionText.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                // Membuat bagian Multipart untuk gambar
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                // Memanggil fungsi postStory dari ViewModel untuk mengirim cerita ke server
                createStoryViewModel.postStory(imageMultipart, description)
                    .observe(viewLifecycleOwner) {
                        when (it) {
                            is Result.Success -> {
                                // Menyembunyikan loading UI setelah sukses
                                showLoading(false)
                                // Menampilkan pesan sukses dan navigasi ke ListStoryFragment
                                Toast.makeText(context, it.data.message, Toast.LENGTH_LONG).show()
                                findNavController().navigate(CreateStoryFragmentDirections.actionCreateStoryFragmentToListStoryFragment())
                            }
                            is Result.Loading -> {
                                // Menampilkan loading UI
                                showLoading(true)
                            }
                            is Result.Error -> {
                                // Menyembunyikan loading UI setelah error dan menampilkan pesan error
                                showLoading(false)
                                Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            } else {
                // Menyembunyikan loading UI karena deskripsi kosong dan menampilkan pesan
                showLoading(false)
                Toast.makeText(
                    requireContext(),
                    "Silakan masukkan deskripsi gambar terlebih dahulu.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Menyembunyikan loading UI karena file gambar kosong dan menampilkan pesan
            showLoading(false)
            Toast.makeText(
                requireContext(),
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Fungsi untuk menampilkan atau menyembunyikan komponen UI selama proses loading
    private fun showLoading(state: Boolean) {
        binding.barCreateStory.isVisible = state
        binding.addDescription.isInvisible = state
        binding.imagePreview.isInvisible = state
        binding.btnOpenCamera.isInvisible = state
        binding.btnOpenGallery.isInvisible = state
        binding.buttonAdd.isInvisible = state
    }

    // Fungsi ini dipanggil ketika tata letak fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
