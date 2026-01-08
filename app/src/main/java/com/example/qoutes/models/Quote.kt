package com.example.qoutes.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "quotes")
data class Quote(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @SerializedName("q")
    val quote: String = "",

    @SerializedName("a")
    val author: String = "",

    @SerializedName("h")
    val formatted: String = "",

    // ده الحقل الجديد اللي هنستخدمه لتصنيف الاقتباس
    // القيم هتكون مثلا: "prayers", "wisdom", "motivation"
    val category: String = "general",

    // ضيف السطر ده ضروري عشان شاشة المفضلة
    var isBookmarked: Boolean = false
)