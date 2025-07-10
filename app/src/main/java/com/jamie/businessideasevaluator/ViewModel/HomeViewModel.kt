package com.jamie.businessideasevaluator.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamie.businessideasevaluator.Data.Db.DbHelper
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.Data.Repository.BusinessIdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel() : ViewModel() {

    private lateinit var repository: BusinessIdeaRepository
    val ideas = MutableLiveData<List<BusinessIdea>>()

    fun initRepository(context: Context) {
        val dbHelper = DbHelper(context)
        repository = BusinessIdeaRepository(dbHelper)
    }
    fun loadIdeas() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.getAllIdeas().isEmpty()) {
                val testTags1 = mapOf(
                    "Innovation" to 5,
                    "LowCost" to 4,
                    "Green" to 3
                )

                val testTags2 = mapOf(
                    "Tech" to 5,
                    "Scalable" to 4,
                    "Automotive" to 2
                )

                val testTags3 = mapOf(
                    "Online" to 5,
                    "Retail" to 4,
                    "Ecommerce" to 5
                )

                val testIdea1 = BusinessIdea(
                    businessName = "AutoShop",
                    businessDescription = "Deciding between an Autoshop business, electronics store, and an Online Ecommerce store",
                    businessScore = 90,
                    date = Date(),
                    businessTags = testTags1
                )

                val testIdea2 = BusinessIdea(
                    businessName = "Tech Solutions",
                    businessDescription = "A startup focused on developing innovative tech solutions for small businesses",
                    businessScore = 85,
                    date = Date(),
                    businessTags = testTags2
                )

                val testIdea3 = BusinessIdea(
                    businessName = "Ecommerce Platform",
                    businessDescription = "An online platform to connect local artisans with customers worldwide",
                    businessScore = 88,
                    date = Date(),
                    businessTags = testTags3
                )

                // Insert ideas into DB
                repository.insertIdea(testIdea1)
                repository.insertIdea(testIdea2)
                repository.insertIdea(testIdea3)

                // Optionally insert duplicates or variations if you want more entries
                repository.insertIdea(testIdea1.copy(businessName = "AutoShop Express", businessScore = 80))
                repository.insertIdea(testIdea2.copy(businessName = "Tech Solutions Pro", businessScore = 90))
                repository.insertIdea(testIdea3.copy(businessName = "Global Ecommerce", businessScore = 92))
            }

            val data = repository.getAllIdeas()
            Log.d(TAG, "loadIdeas: data size: ${data.size}")
            ideas.postValue(data)
        }
    }
    companion object{
        private const val TAG = "HomeViewModel"
    }

}