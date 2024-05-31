package com.latihan.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.latihan.storyapp.data.api.Result
import com.latihan.storyapp.databinding.ActivityDetailBinding
import com.latihan.storyapp.view.StoryViewModel
import com.latihan.storyapp.view.StoryViewFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<StoryViewModel> {
        StoryViewFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id") ?: ""

        viewModel.getDetailStory(id).observe(this) {result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val storyData = result.data
                    binding.imgDetail.loadImage(storyData.photoUrl)
                    binding.ivtitle.text = storyData.name
                    binding.ivDesc.text = storyData.description
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error ${result.error} : Cek internet anda!", Toast.LENGTH_SHORT).show()
                    Log.d("DetailStory", "onCreate: ${result.error}")
                }
            }
        }
    }
}

private fun ImageView.loadImage(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
        .into(this)
}