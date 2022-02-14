package hr.rainbow.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import hr.rainbow.R
import hr.rainbow.databinding.ActivityMainBinding
import hr.rainbow.util.CHANNEL_ID_MEDIA
import hr.rainbow.util.createNotificationChannel
import hr.rainbow.util.uncheckAllItems
import android.app.NotificationManager
import android.content.Intent
import androidx.core.app.ActivityCompat
import hr.rainbow.domain.model.UiHandler

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UiHandler {

    companion object {
        fun startActivity(context: Context) {
            ActivityCompat.startActivity(
                context,
                Intent(
                    context,
                    MainActivity::class.java
                ),
                null
            )
        }

        fun startActivity(context: Context, flag: Int) {
            ActivityCompat.startActivity(
                context,
                Intent(
                    context,
                    MainActivity::class.java
                ).apply {
                    addFlags(flag)
                },
                null
            )
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel(
            this,
            CHANNEL_ID_MEDIA,
            getString(R.string.media_channel_id_title),
            getString(R.string.media_channel_id_description),
            NotificationManager.IMPORTANCE_HIGH
        )

        initNavigation()
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(this, R.id.navHostView)
        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menuDiscover,
                R.id.menuMyStories,
                R.id.menuFavoriteStories,
                R.id.menuScheduler,
                R.id.menuProfile,
                R.id.menuPictureGallery
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.navHostView).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuProfile -> {
                navController.navigate(R.id.menuProfile)
                binding.bottomNavView.uncheckAllItems()
                true
            }
            R.id.menuPictureGallery -> {
                navController.navigate(R.id.menuPictureGallery)
                binding.bottomNavView.uncheckAllItems()
                true
            }
            R.id.menuSearch -> {
                navController.navigate(R.id.menuSearch)
                binding.bottomNavView.uncheckAllItems()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showActionBar() {
        supportActionBar?.show()
    }

    override fun hideActionBar() {
        supportActionBar?.hide()
    }
}