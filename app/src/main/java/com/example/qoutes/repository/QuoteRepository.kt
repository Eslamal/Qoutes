/*
 * MIT License
 *
 * Copyright (c) 2021 Gourav Khunger
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.qoutes.repository


import com.example.qoutes.api.QuoteAPI
import com.example.qoutes.db.QuoteDao
import com.example.qoutes.models.Quote
import com.example.qoutes.store.Preference
import com.example.qoutes.store.PreferenceStore
import javax.inject.Inject

class QuoteRepository @Inject constructor(
    private val dao: QuoteDao,
    private val api: QuoteAPI,
    private val preferenceStore: PreferenceStore
) {

    suspend fun getRandomQuote() = api.getRandomQuote()

    suspend fun getRandomQuoteForNotification() = dao.getRandomQuoteForNotification()

    suspend fun getQuoteOfTheDay() = api.getQuoteOfTheDay()

    suspend fun upsert(quote: Quote) = dao.upsert(quote)

    // ضيفها مع باقي الدوال
    fun getQuotesByCategory(category: String) = dao.getQuotesByCategory(category)

    suspend fun deleteQuote(quote: Quote) =
        dao.deleteSavedQuote(quote)

    fun getSavedQuotes() = dao.getSavedQuotes()

    suspend fun <T> getSetting(preference: Preference<T>) = preferenceStore.getPreference(preference)

    suspend fun <T> saveSetting(preference: Preference<T>, value: T) =
        preferenceStore.putPreference(preference, value)
}
