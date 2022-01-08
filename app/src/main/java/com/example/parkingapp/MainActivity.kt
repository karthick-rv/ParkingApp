package com.example.parkingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.example.parkingapp.feature_parking.presentation.system_create.SystemConfigManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var systemConfigManager: SystemConfigManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNavigationGraph()
    }

    private fun setNavigationGraph() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHost?.findNavController()
        val navGraph =navController?.navInflater?.inflate(R.navigation.nav_graph)

        runBlocking {
            if(systemConfigManager.getSystemConfig().firstOrNull()?.systemCreated == true){
                navGraph?.setStartDestination(R.id.welcomeFragment)
            }else{
                navGraph?.setStartDestination(R.id.systemCreateFragment)
            }
        }
        navController?.graph = navGraph!!
    }
}