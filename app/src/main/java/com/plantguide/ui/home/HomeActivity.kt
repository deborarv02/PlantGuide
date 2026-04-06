package com.plantguide.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.plantguide.R
import com.plantguide.databinding.ActivityHomeBinding
import com.plantguide.ui.about.AboutActivity
import com.plantguide.ui.favorites.FavoritesActivity
import com.plantguide.ui.login.LoginActivity
import com.plantguide.ui.scan.PlantScanActivity
import com.plantguide.util.PreferenceHelper

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var prefs: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PreferenceHelper(this)
        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        val headerView = binding.navView.getHeaderView(0)
        val tvUserEmail = headerView.findViewById<android.widget.TextView>(R.id.tvNavHeaderEmail)
        tvUserEmail?.text = prefs.getUserEmail()

        if (savedInstanceState == null) {
            loadFragment(PlantListFragment())
            binding.navView.setCheckedItem(R.id.nav_home)
            supportActionBar?.title = getString(R.string.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(PlantListFragment())
                supportActionBar?.title = getString(R.string.nav_home)
            }
            R.id.nav_scan -> {
                startActivity(Intent(this, PlantScanActivity::class.java))
            }
            R.id.nav_favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
            }
            R.id.nav_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.nav_logout -> {
                prefs.clearLoginState()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
