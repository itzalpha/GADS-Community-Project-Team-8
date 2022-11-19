package com.example.gadshealthteam8.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gadshealthteam8.AdminNavigationMainActivity
import com.example.gadshealthteam8.databinding.ActivityAdminLoginBinding

class AdminLogin : AppCompatActivity() {

    private lateinit var binding : ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Admin Login"

        binding.loginButton.setOnClickListener {


            //to get the filled in text
            val password = binding.adminPassword.text.toString().trim()
            if (password == "HealthTeam8"){
                startActivity(Intent(this , AdminNavigationMainActivity::class.java))
            }else{
                Toast.makeText(this, "Empty or Wrong Credentials!", Toast.LENGTH_SHORT).show()

            }


        }







    }
}