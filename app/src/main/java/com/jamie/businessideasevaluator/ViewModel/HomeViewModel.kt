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

            val data = repository.getAllIdeas()

            Log.d(TAG, "loadIdeas: data size: ${data.size}")
            ideas.postValue(data)
        }
    }

    companion object{
        private const val TAG = "HomeViewModel"
    }

}