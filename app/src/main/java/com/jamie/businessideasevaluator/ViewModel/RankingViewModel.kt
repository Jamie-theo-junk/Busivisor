package com.jamie.businessideasevaluator.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea

class RankingViewModel() : ViewModel() {

    private val _businessIdea = MutableLiveData(BusinessIdea())
    val businessIdea: LiveData<BusinessIdea> get() = _businessIdea

    fun updateBusinessAnalysis(data: Map<String, Int>) {
        val current = _businessIdea.value ?: BusinessIdea()
        _businessIdea.value = current.copy(businessAnalysis = data)
    }

    fun updatePersonalSkills(data: Map<String, Int>) {
        val current = _businessIdea.value ?: BusinessIdea()
        _businessIdea.value = current.copy(personalSkills = data)
    }

    fun updateOwnCriteria(data: Map<String, Int>) {
        val current = _businessIdea.value ?: BusinessIdea()
        _businessIdea.value = current.copy(ownCriteria = data)
    }
}
