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

class TropyViewModel : ViewModel() {
    private lateinit var repository: BusinessIdeaRepository

    val ideas = MutableLiveData<List<BusinessIdea>>()

    fun initRepository(context: Context) {
        val dbHelper = DbHelper(context)
        repository = BusinessIdeaRepository(dbHelper)
    }

    fun loadRankedIdeas() {
        viewModelScope.launch(Dispatchers.IO) {
            val allIdeas = repository.getAllRankedIdeas()
            ideas.postValue(allIdeas)
        }
    }
}