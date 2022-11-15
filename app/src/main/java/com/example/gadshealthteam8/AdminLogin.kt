package com.example.gadshealthteam8

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gadshealthteam8.databinding.ActivityAdminLoginBinding
import java.util.*

class AdminLogin : AppCompatActivity() {

    private lateinit var binding : ActivityAdminLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {


            //to get the filled in text
            val password = binding.adminPassword.text.toString().trim()
            if (password == "HealthTeam8"){
                startActivity(Intent(this , AdminMainActivity::class.java))
            }else{
                Toast.makeText(this, "Empty or Wrong Credentials!", Toast.LENGTH_SHORT).show()

            }


        }







    }
}