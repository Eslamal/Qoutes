package com.example.qoutes.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.qoutes.R
import com.example.qoutes.databinding.FragmentSettingsBinding
import com.example.qoutes.store.Preference
import com.example.qoutes.viewmodels.QuoteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : DialogFragment() {
    companion object {
        const val TAG = "SettingsFragment"
    }

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by activityViewModels<QuoteViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            binding = FragmentSettingsBinding.inflate(inflater)

            binding.manageNotification.setOnClickListener {
                openNotificationSettings()
            }

            val builder = MaterialAlertDialogBuilder(it, R.style.MaterialAlertDialog_Rounded)
            builder.setView(binding.root)



            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun openNotificationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startActivity(
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                }
            )
        } else {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                data = Uri.fromParts("package", requireContext().packageName, null)
            })
        }
    }
}

