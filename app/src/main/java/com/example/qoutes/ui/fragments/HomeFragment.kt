package com.example.qoutes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.qoutes.R
import com.example.qoutes.databinding.FragmentHomeBinding
import com.example.qoutes.ui.adapters.Category
import com.example.qoutes.ui.adapters.CategoryAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // تجهيز قائمة الأقسام
        // ملاحظة: الألوان دي (R.color.xxx) لازم تكون موجودة أو تستخدم ألوان مؤقتة
        val categories = listOf(
            // لاحظ استخدام getString عشان يجيب الاسم حسب لغة الموبايل
            Category("prayers", getString(R.string.cat_prayers), R.color.purple_500, R.drawable.ic_prayers),
            Category("wisdom", getString(R.string.cat_wisdom), R.color.teal_700, R.drawable.ic_wisdom),
            Category("proverbs", getString(R.string.cat_proverbs), R.color.deep_blue, R.drawable.ic_proverbs),
            Category("books", getString(R.string.cat_books), R.color.backg, R.drawable.ic_books),

            Category("motivation", getString(R.string.cat_motivation), R.color.light_blue, R.drawable.ic_motivation),
            Category("success", getString(R.string.cat_success), R.color.purple_700, R.drawable.ic_success),
            Category("stoicism", getString(R.string.cat_stoicism), R.color.black, R.drawable.ic_stoicism),
            Category("technology", getString(R.string.cat_tech), R.color.teal_200, R.drawable.ic_tech)
        )

        val adapter = CategoryAdapter(categories) { selectedCategory ->
            // هنا الكود اللي بينقل لصفحة الاقتباسات
            // لسه هنظبط الـ Navigation Graph عشان يستقبل الـ category
            val action = HomeFragmentDirections.actionHomeFragmentToQuoteFragment(selectedCategory.id)
            findNavController().navigate(action)
        }

        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }
    }
}