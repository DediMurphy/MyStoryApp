package com.latihan.storyapp.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.storyapp.R
import com.latihan.storyapp.databinding.ActivityMainBinding
import com.latihan.storyapp.view.adapter.StoryAdapter
import com.latihan.storyapp.view.StoryViewFactory
import com.latihan.storyapp.view.StoryViewModel
import com.latihan.storyapp.view.ViewModelFactory
import com.latihan.storyapp.view.adapter.LoadingStateAdapter
import com.latihan.storyapp.view.camera.CameraActivity
import com.latihan.storyapp.view.maps.MapsActivity
import com.latihan.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val viewModelStory by viewModels<StoryViewModel> {
        StoryViewFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyAdapter = StoryAdapter()

        viewModel.getUser().observe(this) { user ->
                if (!user.isLogin) {
                    binding.progressBar.visibility = View.VISIBLE
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    viewModelStory.getStory.observe(this) {
                        storyAdapter.submitData(lifecycle, it)
                    }
                }
            }

        binding.storyItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        }

        setupView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> {
                viewModel.logout()
            }
            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

}