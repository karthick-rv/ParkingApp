package com.example.parkingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.parkingapp.databinding.ActivityMainBinding
import com.example.parkingapp.feature_parking.presentation.system_create.SystemConfigManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var systemConfigManager: SystemConfigManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater )
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHost?.findNavController()
        navController?.let {
            setNavigationGraph(it)
            setDrawerLayout(it)
        }
    }

    private fun setNavigationGraph(navController: NavController) {

        val navGraph =navController.navInflater.inflate(R.navigation.nav_graph)

        runBlocking {
            if(systemConfigManager.getSystemConfig().firstOrNull()?.systemCreated == true){
                navGraph.setStartDestination(R.id.welcomeFragment)
            }else{
                navGraph.setStartDestination(R.id.systemCreateFragment)
            }
        }
        navController.graph = navGraph
    }

    private fun setDrawerLayout(navController: NavController) {

        val appBarConfiguration =
            AppBarConfiguration.Builder(
                setOf(R.id.welcomeFragment))
                .setOpenableLayout(binding.drawerLayout)
                .build()

        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout) || super.onSupportNavigateUp()
    }
}