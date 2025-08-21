package com.Derick.Ideagauge.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Derick.Ideagauge.Data.Db.DbHelper
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.Data.Repository.BusinessIdeaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    companion object {
        private const val TAG = "HomeViewModel"
    }

    suspend fun removeIdea(idea: BusinessIdea): Boolean {
        return withContext(Dispatchers.IO) {
            val success = repository.removeBusinessIdea(idea)
            if (success) {
                val updatedList = repository.getAllIdeas()
                withContext(Dispatchers.Main) {
                    ideas.value = updatedList
                }
            }
            success
        }
    }
}