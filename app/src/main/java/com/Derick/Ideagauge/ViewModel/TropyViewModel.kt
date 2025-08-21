package com.Derick.Ideagauge.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Derick.Ideagauge.Data.Db.DbHelper
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.Data.Repository.BusinessIdeaRepository
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