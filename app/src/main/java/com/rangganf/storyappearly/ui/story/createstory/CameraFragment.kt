package com.rangganf.storyappearly.ui.story.createstory

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rangganf.storyappearly.R
import com.rangganf.storyappearly.databinding.FragmentCameraBinding
import com.rangganf.storyappearly.utils.createFile
import java.util.*

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set click listeners for UI elements
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener {
            toggleCamera()
            startCamera()
        }
    }

    // Toggle between front and back cameras
    private fun toggleCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
            CameraSelector.DEFAULT_FRONT_CAMERA
        else CameraSelector.DEFAULT_BACK_CAMERA
    }

    // Capture a photo using the camera
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(requireActivity().application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    // Handle error when capturing image
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Image capture successful, navigate to create story
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                    navigateToCreateStory(savedUri)
                }
            }
        )
    }

    // Navigate to create story fragment with the captured image
    private fun navigateToCreateStory(savedUri: Uri) {
        val bundle = Bundle().apply {
            putBoolean("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
            putParcelable("selected_image", savedUri)
        }
        findNavController().navigate(
            R.id.action_cameraFragment_to_createStoryFragment,
            bundle
        )
    }

    // Start the camera preview
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                // Unbind any existing use cases and bind new ones
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                // Handle exceptions when starting the camera
                Toast.makeText(
                    requireContext(),
                    "Gagal meluncurkan kamera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    // Hide or show system UI elements
    private fun hideSystemUI(state: Boolean) {
        @Suppress("DEPRECATION")
        if (state) {
            // Hide system UI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        } else {
            // Show system UI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                requireActivity().window.clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                )
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            (requireActivity() as AppCompatActivity).supportActionBar?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Hide system UI when the fragment is resumed
        hideSystemUI(true)
        // Start the camera preview
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        // Show system UI when the fragment is stopped
        hideSystemUI(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding
        _binding = null
    }
}
