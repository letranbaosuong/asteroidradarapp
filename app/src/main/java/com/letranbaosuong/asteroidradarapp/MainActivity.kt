package com.letranbaosuong.asteroidradarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    //    private lateinit var _appBarConfiguration: AppBarConfiguration
//    private lateinit var _appNavControl: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        _appNavControl =
//            (supportFragmentManager
//                .findFragmentById(R.id.nav_main_fragment)
//                    as NavHostFragment).navController
//        _appBarConfiguration = AppBarConfiguration(_appNavControl.graph)
//        NavigationUI.setupActionBarWithNavController(this, _appNavControl)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_main)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return _appNavControl.navigateUp() || super.onSupportNavigateUp() || _appNavControl.popBackStack()
//    }
}