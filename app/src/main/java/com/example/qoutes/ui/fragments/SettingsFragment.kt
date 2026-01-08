package com.example.qoutes.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.activityViewModels
import com.example.qoutes.R
import com.example.qoutes.databinding.FragmentSettingsBinding
import com.example.qoutes.store.Preference
import com.example.qoutes.viewmodels.QuoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by activityViewModels<QuoteViewModel>()

    // تجهيز كود طلب الإذن (للأندرويد 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // لو وافق على الإذن، نفتح السويتش ونحفظ
            binding.switchNotifications.isChecked = true
            viewModel.saveSetting(Preference.ASK_NOTIF_PERM, true)
            Toast.makeText(context, "Notifications Enabled ✅", Toast.LENGTH_SHORT).show()
        } else {
            // لو رفض، نقفل السويتش
            binding.switchNotifications.isChecked = false
            viewModel.saveSetting(Preference.ASK_NOTIF_PERM, false)
            Toast.makeText(context, "Notifications Disabled ❌", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. Notifications Switch Logic ---
        // بنشوف الحالة المحفوظة ونظبط السويتش عليها
        val isNotifEnabled = viewModel.getSetting(Preference.ASK_NOTIF_PERM)
        binding.switchNotifications.isChecked = isNotifEnabled

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // لو شغل السويتش، نتأكد من الإذن الأول
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        viewModel.saveSetting(Preference.ASK_NOTIF_PERM, true)
                    } else {
                        // نطلب الإذن
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    // للأجهزة القديمة بيشتغل علطول
                    viewModel.saveSetting(Preference.ASK_NOTIF_PERM, true)
                }
            } else {
                // لو قفل السويتش
                viewModel.saveSetting(Preference.ASK_NOTIF_PERM, false)
            }
        }


        // --- 2. Dark Mode Logic ---
        val isDark = viewModel.getSetting(Preference.IS_DARK_MODE)
        binding.switchDarkMode.isChecked = isDark

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveSetting(Preference.IS_DARK_MODE, isChecked)
            updateTheme(isChecked)
        }


        // --- 3. Language Logic ---
        val currentLang = viewModel.getSetting(Preference.APP_LANGUAGE)
        if (currentLang == "ar") binding.toggleLanguage.check(R.id.btnArabic)
        else binding.toggleLanguage.check(R.id.btnEnglish)

        binding.toggleLanguage.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val langCode = if (checkedId == R.id.btnArabic) "ar" else "en"
                viewModel.saveSetting(Preference.APP_LANGUAGE, langCode)
                updateLanguage(langCode)
                dismiss() // نقفل القائمة عشان التطبيق يعمل ريستارت للغة
            }
        }
    }

    private fun updateTheme(isDark: Boolean) {
        val mode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun updateLanguage(code: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(code)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}