package com.latihan.storyapp.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.latihan.storyapp.data.api.Result
import com.latihan.storyapp.databinding.ActivityCameraBinding
import com.latihan.storyapp.view.StoryViewFactory
import com.latihan.storyapp.view.StoryViewModel
import com.latihan.storyapp.view.ViewModelFactory
import com.latihan.storyapp.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class CameraActivity : AppCompatActivity() {

    private val viewModel by viewModels<StoryViewModel> {
        StoryViewFactory.getInstance(this)
    }
    
    private lateinit var binding: ActivityCameraBinding
    private var currentImageUri: Uri? = null
    
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this,"Permission request grated", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Permission request denied", Toast.LENGTH_LONG).show()
            }
        }


    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()){
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){ uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker","No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI","showImage: $it")
            binding.previewImageVIew.setImageURI(it)
        }
    }

    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.tvdescription.text.toString()

            binding.progressBar.visibility = View.VISIBLE

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImage = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImage
            )

            viewModel.uploadStory(multipartBody,requestBody).observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            val response = result.data.message
                            Log.d("AddStory", "startUpload: $response")
                            AlertDialog.Builder(this).apply {
                                setTitle("Upload Berhasil")
                                setMessage("Story telah berhasil diposting!")
                                setPositiveButton("Lanjut") {_,_ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}