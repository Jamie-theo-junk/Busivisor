package com.Derick.Ideagauge.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Derick.Ideagauge.Data.Db.DbHelper
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.Data.Repository.BusinessIdeaRepository


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

    fun loadIdeaByDateId(date: Long) {
        val allIdeas = repository.getAllRankedIdeas()
        val match = allIdeas.find { it.date.time == date }
        match?.let {
            _selectedIdea.value = it
        }
    }

    fun deleteBusinessIdea(targetIdea: BusinessIdea){
        repository.removeBusinessIdea(targetIdea)
    }

    fun getBusinessIdeaRank(targetIdea: BusinessIdea): Int {
        val data = repository.getAllRankedIdeas()
        val sortedIdeas = data.sortedByDescending { it.businessScore }
        return sortedIdeas.indexOfFirst { it == targetIdea } + 1
    }

}