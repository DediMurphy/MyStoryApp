package com.latihan.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.latihan.storyapp.data.api.Result
import com.latihan.storyapp.databinding.ActivitySignupBinding
import com.latihan.storyapp.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30F,30F).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA,1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA,1f).setDuration(100)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA,1f).setDuration(100)
        val nameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA,1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(100)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)


        val together = AnimatorSet().apply {
            playTogether(signup)
        }

        AnimatorSet().apply {
            playSequentially(title,together,nameText,nameEdit,email,emailText,password,passwordText)
            start()
        }
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
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                signupViewModel.register(name, email, password).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            AlertDialog.Builder(this).apply {
                                setTitle(result.data.message)
                                setMessage("Akun dengan $email sudah jadi nih!")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Error: " + result.error, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }
            } else {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            }

        }
    }
}