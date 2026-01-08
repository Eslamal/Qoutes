package com.example.qoutes.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.navigation.Navigation
import com.example.qoutes.R
import com.example.qoutes.databinding.ActivityQuotesBinding
import com.example.qoutes.store.Preference
import com.example.qoutes.ui.fragments.SettingsFragment
import com.example.qoutes.viewmodels.QuoteViewModel
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

        // 1. Load Language
        val savedLang = viewModel.getSetting(Preference.APP_LANGUAGE)
        if (!savedLang.isNullOrEmpty()) {
            val appLocale = LocaleListCompat.forLanguageTags(savedLang)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        // 2. Load Theme
        val isDark = viewModel.getSetting(Preference.IS_DARK_MODE)
        val mode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)

        binding = ActivityQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Navigation Logic ---

        // الذهاب للمفضلة
        binding.myBookmarksImgBtn.setOnClickListener {
            try {
                val navController = Navigation.findNavController(binding.quotesNavHostFragment)
                navController.navigate(R.id.action_global_to_bookmarkFragment)

                // تحديث حالة التول بار (إخفاء الإعدادات والمفضلة، إظهار الرجوع)
                atHome = false
                updateToolbarState(isHome = false)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // زر الرجوع (اللي في التول بار)
        binding.backToQuotePage.setOnClickListener {
            onBackPressed() // نستخدم دالة النظام عشان ترجعنا صح
        }

        // زر الإعدادات
        binding.settingsBtn.setOnClickListener {
            SettingsFragment().show(supportFragmentManager, "SettingsFragment")
        }

        // Update theme once everything is set up
        setTheme(R.style.Theme_Quotes)

        // --- Permissions & Notifications Logic ---
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            viewModel.saveSetting(Preference.ASK_NOTIF_PERM, false)
            val message = if (isGranted) "Notifications set up successfully!"
            else  "Daily quotes won't work! Please set up notifications in settings."
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.getSetting(Preference.ASK_NOTIF_PERM)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (!viewModel.getSetting(Preference.CHECK_FOR_UPDATES)) return
    }

    // دالة التحكم في زر الرجوع (System Back Button)
    override fun onBackPressed() {
        if (!atHome) {
            // لو احنا في المفضلة، نرجع للرئيسية
            super.onBackPressed()
            atHome = true
            // تحديث حالة التول بار (إظهار الإعدادات والمفضلة، إخفاء الرجوع)
            updateToolbarState(isHome = true)
        } else {
            // لو احنا في الرئيسية، نخرج من التطبيق
            super.onBackPressed()
        }
    }

    // --- دالة مساعدة لضبط ظهور الأزرار ---
    // دي اللي هتحل المشكلة وتضمن إن الزراير تظهر وتختفي صح
    private fun updateToolbarState(isHome: Boolean) {
        with(binding) {
            if (isHome) {
                // احنا في الرئيسية: اظهر الإعدادات والمفضلة، اخفي الرجوع
                settingsBtn.visibility = View.VISIBLE
                myBookmarksImgBtn.visibility = View.VISIBLE
                backToQuotePage.visibility = View.GONE
                activityTitle.text = resources.getText(R.string.app_name)
            } else {
                // احنا في المفضلة: اخفي الإعدادات والمفضلة، اظهر الرجوع
                settingsBtn.visibility = View.GONE
                myBookmarksImgBtn.visibility = View.GONE
                backToQuotePage.visibility = View.VISIBLE
                activityTitle.text = resources.getText(R.string.myBookMarks)
            }
        }
    }
}