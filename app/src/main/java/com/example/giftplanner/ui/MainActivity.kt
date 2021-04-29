package com.example.giftplanner.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.giftplanner.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.recipientListMenu, R.id.plansListMenu, R.id.presentsListMenu)
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottom_nav.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

const val ADD_PLAN_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val EDIT_PLAN_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val DELETE_PLAN_RESULT_OK = Activity.RESULT_FIRST_USER + 3

const val ADD_PRESENT_RESULT_OK = Activity.RESULT_FIRST_USER + 4
const val EDIT_PRESENT_RESULT_OK = Activity.RESULT_FIRST_USER + 5
const val DELETE_PRESENT_RESULT_OK = Activity.RESULT_FIRST_USER + 6

const val ADD_RECIPIENT_RESULT_OK = Activity.RESULT_FIRST_USER + 7
const val EDIT_RECIPIENT_RESULT_OK = Activity.RESULT_FIRST_USER + 8
const val DELETE_RECIPIENT_RESULT_OK = Activity.RESULT_FIRST_USER + 9