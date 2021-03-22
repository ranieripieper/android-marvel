package me.ranieripieper.android.marvel.feature.characters.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import me.ranieripieper.android.marvel.R
import me.ranieripieper.android.marvel.databinding.ActivityCharactersBinding


class CharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharactersBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharactersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        setupActionBarAndNavController()
    }

    private fun setupActionBarAndNavController() {
        val navController = getNavController()
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navController
        )

        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.characters_list, R.id.characters_favorites))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            getNavController().popBackStack()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getNavController(): NavController {
        return (supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController
    }

}
