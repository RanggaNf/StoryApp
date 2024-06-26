package com.rangganf.storyappearly.ui.story.detailstory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import coil.imageLoader
import coil.request.ImageRequest
import com.rangganf.storyappearly.data.remote.response.stories.Story
import com.rangganf.storyappearly.databinding.FragmentDetailStoryBinding

class DetailStoryFragment : Fragment() {

    // Variabel untuk binding menggunakan View Binding
    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating layout menggunakan View Binding
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        // Menunda transisi masuk
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengatur transisi berbagi elemen ketika Fragment ini dimasukkan
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        // Membaca data cerita dari argument dan menetapkannya ke binding
        binding.story = Story(
            createdAt = "",
            description = arguments?.getString("description") ?: "",
            id = arguments?.getString("id") ?: "",
            lat = arguments?.getDouble("lat") ?: 0.0,
            lon = arguments?.getDouble("lon") ?: 0.0,
            name = arguments?.getString("name") ?: "",
            photoUrl = arguments?.getString("photo_url") ?: ""
        )

        // Membuat permintaan untuk memuat gambar menggunakan Coil
        val request = ImageRequest.Builder(requireContext())
            .data(arguments?.getString("photo_url"))
            .target(
                onSuccess = {
                    // Memulai transisi setelah gambar berhasil dimuat
                    startPostponedEnterTransition()
                },
                onError = {
                    // Memulai transisi jika terjadi kesalahan saat memuat gambar
                    startPostponedEnterTransition()
                }
            )
            .build()

        // Menambahkan permintaan gambar ke dalam antrian pemrosesan gambar Coil
        requireActivity().application.imageLoader.enqueue(request)

        // Menjalankan binding agar memproses perubahan dan melakukan pengikatan data
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Membersihkan referensi binding untuk menghindari kebocoran memori saat Fragment dihancurkan
        _binding = null
    }
}