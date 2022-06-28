package com.keepin.android.presentation.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.keepin.android.R
import com.keepin.android.base.BindingActivity
import com.keepin.android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private var selectedBottomNaviPosition = 0
    private var isKeepInClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomNavigationView()
        isOpenFromFcm()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.bottomNaviMain.selectedItemId = when (selectedBottomNaviPosition) {
            0 -> R.id.homeFragment
            1 -> R.id.archiveFragment
            3 -> R.id.reminderFragment
            4 -> R.id.myPageFragment
            else -> R.id.homeFragment
        }
        isKeepInClicked = false
    }

    private fun isOpenFromFcm() {
        if (intent.getBooleanExtra("openFromFcm", false)) {
            binding.bottomNaviMain.selectedItemId = R.id.reminderFragment
        }
    }

    private fun setBottomNavigationView() {
        val navController = findNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showBottomNav()
                    setSelectedBottomNaviPosition(isKeepInClicked, 0)
                }
                R.id.archiveFragment -> {
                    showBottomNav()
                    setSelectedBottomNaviPosition(isKeepInClicked, 1)
                }
                R.id.keepInFragment -> {
                    hideBottomNav()
                    isKeepInClicked = true
                }
                R.id.reminderFragment -> {
                    showBottomNav()
                    setSelectedBottomNaviPosition(isKeepInClicked, 3)
                }
                R.id.myPageFragment -> {
                    showBottomNav()
                    setSelectedBottomNaviPosition(isKeepInClicked, 4)
                }
                else -> hideBottomNav()
            }
        }
        binding.bottomNaviMain.setupWithNavController(navController)
    }

    private fun setSelectedBottomNaviPosition(isKeepInClicked: Boolean, position: Int) {
        if (!isKeepInClicked) selectedBottomNaviPosition = position
    }

    private fun findNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

    private fun showBottomNav() {
        binding.bottomNaviMain.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNaviMain.visibility = View.GONE
    }
}
