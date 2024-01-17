package com.teamtriad.forpets.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.teamtriad.forpets.R
import com.teamtriad.forpets.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.transportFragment, R.id.adoptFragment, R.id.chatFragment, R.id.profileFragment
            )
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bnv.setupWithNavController(navController)

        hideAppBar()
        hideBottomNavigationView()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun hideAppBar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.transportFragment
                || destination.id == R.id.adoptFragment
                || destination.id == R.id.chatFragment
                || destination.id == R.id.profileFragment
            ) {
                binding.toolbar.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }

    private fun hideBottomNavigationView() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination.id == R.id.transportFragment
                || destination.id == R.id.adoptFragment
                || destination.id == R.id.chatFragment
                || destination.id == R.id.profileFragment
            ) {
                binding.bnv.visibility = View.VISIBLE
            } else {
                binding.bnv.visibility = View.GONE
            }
        }
    }
}