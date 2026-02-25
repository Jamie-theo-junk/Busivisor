package com.Derick.Ideagauge.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Derick.Ideagauge.Data.Db.DbHelper
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.Data.Repository.BusinessIdeaRepository

class BusinessPageViewModel : ViewModel() {

    private lateinit var repository: BusinessIdeaRepository
    private lateinit var dbHelper: DbHelper

    private val _selectedIdea = MutableLiveData<BusinessIdea>()
    val selectedIdea: LiveData<BusinessIdea> = _selectedIdea

    fun initRepository(context: Context) {
        dbHelper = DbHelper(context.applicationContext)
        repository = BusinessIdeaRepository(dbHelper)
    }

    fun loadIdeaByPosition(position: Int) {
        repository.getIdeaAt(position)?.let { _selectedIdea.value = it }
    }

    fun loadIdeaByDateId(date: Long) {
        val match = repository.getAllRankedIdeas().find { it.date.time == date }
        match?.let { _selectedIdea.value = it }
    }

    fun deleteBusinessIdea(targetIdea: BusinessIdea) {
        repository.removeBusinessIdea(targetIdea)
    }

    fun getBusinessIdeaRank(targetIdea: BusinessIdea): Int {
        val data = repository.getAllRankedIdeas()
        val sorted = data.sortedByDescending { it.businessScore }
        // Rank by identity or name (name is safer across copies)
        val idx = sorted.indexOfFirst { it.businessName == targetIdea.businessName }
        return if (idx >= 0) idx + 1 else 0
    }

    /**
     * Optional helper: after the Activity saves the AI text to DB, call this to
     * reflect it in UI immediately without a DB re-query.
     */
    fun setAiSuggestionOnSelected(suggestion: String) {
        _selectedIdea.value = _selectedIdea.value?.copy(aiSuggestion = suggestion)
    }

    /**
     * Optional helper: if you want to re-read the latest row from DB (e.g., after save).
     */
    fun refreshSelectedIdea() {
        val current = _selectedIdea.value ?: return
        val updated = repository.getAllRankedIdeas()
            .find { it.businessName == current.businessName }
        if (updated != null) _selectedIdea.value = updated
    }
}
