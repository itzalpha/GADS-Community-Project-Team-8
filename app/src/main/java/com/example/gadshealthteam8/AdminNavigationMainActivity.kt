package com.example.gadshealthteam8

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gadshealthteam8.admin.AdminAddTipsActivity
import com.example.gadshealthteam8.databinding.ActivityAdminNavigationMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn

class AdminNavigationMainActivity : AppCompatActivity() {

    private lateinit var personName : String
    private lateinit var personEmail : String
    private lateinit var personPhoto :  String
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminNavigationMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminNavigationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdminNavigationMain.toolbar)

        binding.appBarAdminNavigationMain.fab.setOnClickListener { view ->
            startActivity(Intent(this, AdminAddTipsActivity::class.java))
        }

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val header =  navigationView.getHeaderView(0)

        val headerEmailText : TextView = header.findViewById(R.id.header_email_text)
        val headerNameText : TextView =  header.findViewById(R.id.header_name_text)
        val headerBookImage : ImageView = header.findViewById(R.id.header_book_image)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null){
            personName = acct.displayName.toString()
            personEmail = acct.email.toString()
            personPhoto = acct.photoUrl.toString()
            headerEmailText.text = personEmail
            headerNameText.text = personName
            Glide.with(this).load(personPhoto).into(headerBookImage)
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_admin_navigation_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow ,  R.id.nav_books ,   R.id.nav_daily
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_admin_navigation_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
            super.onBackPressed();

    }
}