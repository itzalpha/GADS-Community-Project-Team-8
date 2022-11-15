package com.example.gadshealthteam8

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gadshealthteam8.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainActivity : AppCompatActivity() {

    private lateinit var personName : String
    private lateinit var personEmail : String
    private lateinit var personPhoto :  String
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.fab.setOnClickListener { view ->
          startActivity(Intent(this, AdminLogin::class.java))
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
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                        R.id.nav_daily , R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow ,  R.id.nav_books , R.id.nav_about_developer , R.id.nav_feedback
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){

            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.app_name)
                builder.setIcon(R.mipmap.ic_launcher)
                builder.setMessage("Do you want to Log out")
                builder.setPositiveButton("Yes") { _, _ ->

                    val authUI = AuthUI.getInstance()
                    authUI
                        .signOut(this)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "log out successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, SplashMainActivity::class.java))
                                finish()
                            } else {
                                Log.e("settings", "Sign-out failed", task.exception)
                                Toast.makeText(this, "Sign-out failed", Toast.LENGTH_LONG).show()
                            }
                        }

                }
                builder.setNegativeButton("No"){ dialogInterface, _ ->

                    dialogInterface.cancel()
                }
                val alertDialog:AlertDialog=builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()


                true
            }

            else
            -> super.onOptionsItemSelected(item)
        }

    }

    override fun onBackPressed() {

        if(supportActionBar?.title.toString() contentEquals "LifeStyle"){
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.app_name)
            builder.setIcon(R.mipmap.ic_launcher)
            builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .setNegativeButton(
                    "No"
                ) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }else{
            super.onBackPressed();
        }
    }


}