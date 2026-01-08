package com.example.qoutes.util

import android.content.Context
import com.example.qoutes.db.QuoteDao
import com.example.qoutes.models.Quote
import com.example.qoutes.store.Preference
import com.example.qoutes.store.PreferenceStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class JsonDataLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val quoteDao: QuoteDao,
    private val preferenceStore: PreferenceStore
) {

    private val jsonFiles = listOf(
        "prayers.json",     // أدعية
        "wisdom.json",      // حكم
        "proverbs.json",    // أمثال (جديد)
        "books.json",       // كتب (جديد)
        "motivation.json",  // تحفيز
        "success.json",     // نجاح (جديد)
        "stoicism.json",    // رواقية (جديد)
        "technology.json"   // تكنولوجيا (جديد)
    )

    fun loadDataIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            // بنستخدم مفتاح في الإعدادات عشان نتأكد إننا مش بنحمل الداتا كل مرة نفتح التطبيق
            // لو عايز تعمل تحديث للداتا ممكن تغير اسم المفتاح ده لـ data_loaded_v2 مثلا
            val isLoaded = preferenceStore.getPreference(Preference(androidx.datastore.preferences.core.booleanPreferencesKey("data_loaded_v1"), false))

            if (!isLoaded) {
                try {
                    val gson = Gson()
                    val allQuotes = mutableListOf<Quote>()

                    jsonFiles.forEach { fileName ->
                        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
                        val listType = object : TypeToken<List<Quote>>() {}.type
                        val quotes: List<Quote> = gson.fromJson(jsonString, listType)

                        // بنتأكد إن التصنيف (category) مظبوط، ولو مش موجود في الملف بناخده من اسم الملف
                        val categoryName = fileName.replace(".json", "")

                        val quotesWithCategory = quotes.map { it.copy(category = it.category.ifEmpty { categoryName }, isBookmarked = false) }
                        allQuotes.addAll(quotesWithCategory)
                    }

                    if (allQuotes.isNotEmpty()) {
                        quoteDao.insertAll(allQuotes)
                        // نعلم إن التحميل تم بنجاح
                        preferenceStore.putPreference(Preference(androidx.datastore.preferences.core.booleanPreferencesKey("data_loaded_v1"), true), true)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}