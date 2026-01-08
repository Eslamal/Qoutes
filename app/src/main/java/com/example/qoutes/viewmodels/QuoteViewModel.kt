package com.example.qoutes.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qoutes.models.Quote
import com.example.qoutes.repository.QuoteRepository
import com.example.qoutes.store.Preference
import com.example.qoutes.util.CheckInternet
import com.example.qoutes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val internet: CheckInternet,
    private val quoteRepository: QuoteRepository
) : ViewModel() {

    // المتغيرات اللي الـ UI بيسمع لها
    val quote: MutableLiveData<Resource<Quote>> = MutableLiveData()
    val bookmarked: MutableLiveData<Boolean> = MutableLiveData(false)

    // قائمة مؤقتة بتشيل اقتباسات القسم الحالي عشان نقلب فيها بسرعة
    private var currentQuotesList: List<Quote> = emptyList()

    // 1. دالة بنناديها أول ما نفتح القسم (بتاخد اسم القسم ID)
    fun loadQuotesForCategory(categoryId: String) {
        quote.postValue(Resource.Loading())

        // بنراقب الداتا بيز عشان لو حصل أي تغيير، وبنجيب الداتا الخاصة بالقسم
        // ملحوظة: observeForever بتفضل شغالة، في التطبيقات الكبيرة بنستخدم LiveData في الـ Fragment
        // بس هنا عشان التبسيط وسرعة التنفيذ
        quoteRepository.getQuotesByCategory(categoryId).observeForever { quotes ->
            if (quotes.isNotEmpty()) {
                currentQuotesList = quotes
                // لو دي أول مرة واللستة كانت فاضية، اعرض أول واحد
                if (quote.value !is Resource.Success) {
                    showRandomQuoteFromList()
                }
            } else {
                quote.postValue(Resource.Error("لا توجد اقتباسات في هذا القسم حالياً"))
            }
        }
    }

    // 2. دالة تختار واحد عشوائي من القائمة المحلية (بدون نت)
    fun showRandomQuoteFromList() {
        if (currentQuotesList.isNotEmpty()) {
            val randomIndex = Random().nextInt(currentQuotesList.size)
            val randomQuote = currentQuotesList[randomIndex]

            // تحديث زرار المفضلة بناءً على حالة الاقتباس
            bookmarked.postValue(randomQuote.isBookmarked)

            // عرض الاقتباس
            quote.postValue(Resource.Success(randomQuote))
        }
    }

    // 3. حفظ الاقتباس (تعديل حالته لـ Bookmarked)
    fun saveQuote(quote: Quote) = viewModelScope.launch {
        quote.isBookmarked = true
        quoteRepository.upsert(quote) // upsert هتعمل تحديث للصف
        bookmarked.postValue(true)
    }

    // 4. إزالة من المفضلة (تعديل حالته لـ Un-bookmarked)
    // ملحوظة: مش بنستخدم deleteQuote عشان ده هيمسحه من القسم كمان!
    // إحنا بس بنلغي علامة الحفظ
    fun deleteQuote(quote: Quote) = viewModelScope.launch {
        quote.isBookmarked = false
        quoteRepository.upsert(quote)
        bookmarked.postValue(false)
    }

    // دالة لجلب الاقتباسات المحفوظة فقط (لصفحة الـ Bookmarks)
    fun getSavedQuotes() = quoteRepository.getSavedQuotes()

    // --- إعدادات التطبيق (زي ما هي) ---
    fun <T> saveSetting(pref: Preference<T>, value: T) = viewModelScope.launch {
        quoteRepository.saveSetting(pref, value)
    }

    fun <T> getSetting(pref: Preference<T>): T = runBlocking {
        return@runBlocking quoteRepository.getSetting(pref)
    }
}