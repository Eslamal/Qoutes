package com.example.qoutes.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Update
import com.example.qoutes.R
import com.example.qoutes.databinding.ActivityQuotesBinding
import com.example.qoutes.store.Preference
import com.example.qoutes.ui.fragments.QuoteFragmentDirections
import com.example.qoutes.ui.fragments.SettingsFragment
import com.example.qoutes.viewmodels.QuoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuotesActivity : AppCompatActivity() {

    // variables
    var atHome = true
    private lateinit var binding: ActivityQuotesBinding
    private val viewModel by viewModels<QuoteViewModel>()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // logic to switch between fragments
        binding.myBookmarksImgBtn.setOnClickListener {
            val action = QuoteFragmentDirections
                .actionQuoteFragmentToBookmarkFragment()
            val navController = Navigation.findNavController(binding.quotesNavHostFragment)

            navController.navigate(action)
            it.visibility = View.GONE

            with(binding) {
                settingsBtn.visibility = View.GONE
                backToQuotePage.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.myBookMarks)
            }

            atHome = false
        }

        binding.backToQuotePage.setOnClickListener {
            super.onBackPressed()
            it.visibility = View.GONE

            with(binding) {
                settingsBtn.visibility = View.VISIBLE
                myBookmarksImgBtn.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.app_name)
            }

            atHome = true
        }

        binding.settingsBtn.setOnClickListener {
            SettingsFragment().show(supportFragmentManager, SettingsFragment.TAG)
        }

        // Update theme once everything is set up
        setTheme(R.style.Theme_Quotes)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            viewModel.saveSetting(Preference.ASK_NOTIF_PERM, false)

            val message = if (isGranted) "Notifications set up successfully!"
            else  "Daily quotes won't work! Please set up notifications in settings."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        }

        Log.d("QuotesActivity", "onCreate: ${viewModel.getSetting(Preference.ASK_NOTIF_PERM)}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.getSetting(Preference.ASK_NOTIF_PERM)) {
            if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (!viewModel.getSetting(Preference.CHECK_FOR_UPDATES)) return

    }

    // implementation to handle error cases regarding navigation icons
    // this function updates the icons and sets variables according
    // to how navigation was carried out
    override fun onBackPressed() {
        super.onBackPressed()
        if (!atHome) {
            with(binding) {
                settingsBtn.visibility = View.VISIBLE
                backToQuotePage.visibility = View.GONE
                myBookmarksImgBtn.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.app_name)
            }

            atHome = true
        }
    }
}
