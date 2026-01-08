package com.example.qoutes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qoutes.databinding.ItemCategoryBinding
import com.example.qoutes.R

// مودل بسيط للأقسام
data class Category(
    val id: String,        // المفتاح اللي هنستخدمه في الفلترة (زي prayers, wisdom)
    val name: String,      // الاسم اللي هيظهر للمستخدم
    val color: Int,        // لون الكارت
    val icon: Int          // أيقونة الكارت
)

class CategoryAdapter(
    private val categories: List<Category>,
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        with(holder.binding) {
            tvCategoryName.text = category.name
            ivCategoryIcon.setImageResource(category.icon)
            cardCategory.setCardBackgroundColor(root.context.getColor(category.color))

            root.setOnClickListener {
                onClick(category)
            }
        }
    }

    override fun getItemCount(): Int = categories.size
}