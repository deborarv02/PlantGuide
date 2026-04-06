package com.plantguide.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.plantguide.databinding.ActivityLoginBinding
import com.plantguide.ui.home.HomeActivity
import com.plantguide.util.PreferenceHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefs: PreferenceHelper

    companion object {
        private const val VALID_EMAIL = "admin@plantguide.com"
        private const val VALID_PASSWORD = "plantas123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferenceHelper(this)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener { performLogin() }
        binding.tvDemoHint.setOnClickListener {
            binding.etEmail.setText(VALID_EMAIL)
            binding.etPassword.setText(VALID_PASSWORD)
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.tilEmail.error = "Informe seu e-mail"
            return
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Informe sua senha"
            return
        } else {
            binding.tilPassword.error = null
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "E-mail inválido"
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        binding.root.postDelayed({
            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true

            val isValid = (email == VALID_EMAIL && password == VALID_PASSWORD)
                    || (email.isNotEmpty() && password.length >= 6)

            if (isValid) {
                prefs.saveLoginState(true, email)
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Senha muito curta. Use pelo menos 6 caracteres.", Toast.LENGTH_LONG).show()
            }
        }, 1200L)
    }
}
