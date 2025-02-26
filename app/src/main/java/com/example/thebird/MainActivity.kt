package com.example.thebird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.thebird.identity.SignUpScreen

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup the navigation between fragments in the main Activity.
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        val navigationHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navigationHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }


    override fun onBackPressed() {
        if (navController.currentDestination?.label == "Login" || navController.currentDestination?.label == "SignUp"
        ) {
            finish()

        } else {
            super.onBackPressed()
        }

    }
}
