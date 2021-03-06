package com.andriiprudyus.myresume

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andriiprudyus.myresume.base.view.AppFragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.navHostFragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.companies, R.id.about),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
