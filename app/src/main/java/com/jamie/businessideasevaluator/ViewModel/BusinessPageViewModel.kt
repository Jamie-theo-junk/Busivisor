package com.jamie.businessideasevaluator.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jamie.businessideasevaluator.Data.Db.DbHelper
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import com.jamie.businessideasevaluator.Data.Repository.BusinessIdeaRepository


class BusinessPageViewModel() : ViewModel() {


    private lateinit var repository: BusinessIdeaRepository
    private val _selectedIdea = MutableLiveData<BusinessIdea>()
    val selectedIdea: LiveData<BusinessIdea> = _selectedIdea



    fun initRepository(context: Context) {
        val dbHelper = DbHelper(context)
        repository = BusinessIdeaRepository(dbHelper)
    }

    fun loadRankedIdeaByPosition(position: Int) {
        val idea = repository.getRankedIdeaAt(position)
        idea?.let {
            _selectedIdea.value = it
        }
    }

    fun loadIdeaByPosition(position: Int) {
        val idea = repository.getIdeaAt(position)
        idea?.let {
            _selectedIdea.value = it
        }
    }

    fun getBusinessIdeaRank(targetIdea: BusinessIdea): Int {
        val data = repository.getAllRankedIdeas()
        val sortedIdeas = data.sortedByDescending { it.businessScore }
        return sortedIdeas.indexOfFirst { it == targetIdea } + 1
    }

}