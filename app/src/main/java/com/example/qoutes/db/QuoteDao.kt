package com.example.qoutes.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qoutes.models.Quote

@Dao
interface QuoteDao {
    // غيرنا الاستراتيجية لـ IGNORE أو REPLACE حسب تفضيلك
    // بس عشان الـ AutoGenerate يشتغل صح مع الداتا الجديدة
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(quote: Quote): Long

    // دالة جديدة تجيب الاقتباسات حسب القسم
    @Query("SELECT * FROM quotes WHERE category = :category ORDER BY RANDOM()")
    fun getQuotesByCategory(category: String): LiveData<List<Quote>>

    // هات لي بس الحاجات اللي المستخدم عملها حفظ
    @Query("SELECT * FROM quotes WHERE isBookmarked = 1")
    fun getSavedQuotes(): LiveData<List<Quote>>

    @Delete
    suspend fun deleteSavedQuote(quote: Quote)

    // دالة عشان نمسح كل الداتا القديمة لو حبيت تعمل تحديث
    @Query("DELETE FROM quotes")
    suspend fun deleteAllQuotes()

    @Query("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomQuoteForNotification(): Quote?
}