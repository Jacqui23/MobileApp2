package com.example.login2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.login2.databinding.ActivityMainBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }
        viewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }
                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }
                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.btnRegister.setOnClickListener {
            doSignup()
        }
    }
    private fun navigateToHome() {
        val intent = Intent(this, LogoutActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }
    fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        viewModel.loginUser(email = email, pwd = pwd)
    }
    fun doSignup() {
    }
    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }
    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }
    fun processLogin(data: LoginResponse?) {
        showToast("Success:" + data?.message)
        if (!data?.data?.token.isNullOrEmpty()) {
            data?.data?.token?.let { SessionManager.saveAuthToken(this,it) }
            navigateToHome()
        }
    }
    fun processError(msg: String?) {
        showToast("Error:" + msg)
    }
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()}
}