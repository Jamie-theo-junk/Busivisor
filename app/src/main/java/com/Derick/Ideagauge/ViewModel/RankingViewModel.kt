package com.Derick.Ideagauge.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Derick.Ideagauge.Data.Db.DbHelper
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import com.Derick.Ideagauge.Data.Repository.BusinessIdeaRepository
import kotlin.math.roundToInt

class RankingViewModel() : ViewModel() {
    private lateinit var repository: BusinessIdeaRepository
    private val _businessIdea = MutableLiveData(BusinessIdea())
    val businessIdea: LiveData<BusinessIdea> get() = _businessIdea

    var businessAnalysis: MutableMap<String, Int> = mutableMapOf()
    var personalSkills: MutableMap<String, Int> = mutableMapOf()
    var ownCriteria: MutableMap<String, Int> = mutableMapOf()


    private var analysisAvg:Double = 0.0
    private var skillsAvg:Double = 0.0
    private var ownAvg:Double = 0.0

    fun initRepository(context: Context) {
        val dbHelper = DbHelper(context)
        repository = BusinessIdeaRepository(dbHelper)
    }

    fun updateBusinessAnalysis(data: Map<String, Int>) {
        businessAnalysis.putAll(data)
    }

    fun updatePersonalSkills(data: Map<String, Int>) {
        personalSkills.putAll(data)
    }

    fun updateOwnCriteria(data: MutableMap<String, Int>) {
        ownCriteria = data
    }
    fun finalizeAndInsertIdea(
        name: String,
        description: String
    ) {
        val current = _businessIdea.value ?: return
        Log.d(TAG, "finalizeAndInsertIdea: ${current.businessAnalysis}")
        val score = calculateBalancedScore(
            businessAnalysis,
            personalSkills,
            ownCriteria
        )
        val tag = mapOf(
            "Business Analysis" to analysisAvg.roundToInt(),
            "Personal Skills" to skillsAvg.roundToInt(),
            "Own Criteria" to ownAvg.roundToInt()
        )

        val newIdea = current.copy(
            businessName = name,
            businessDescription = description,
            businessTags = tag,
            businessScore = score,
            businessAnalysis = businessAnalysis,
            personalSkills = personalSkills,
            ownCriteria = ownCriteria
        )

        repository.insertIdea(newIdea)
    }
    private fun calculateBalancedScore(
        analysis: Map<String, Int>,
        skills: Map<String, Int>,
        own: Map<String, Int>
    ): Int {
         analysisAvg = if (analysis.isNotEmpty()) analysis.values.average() else 0.0
         skillsAvg = if (skills.isNotEmpty()) skills.values.average() else 0.0
         ownAvg = if (own.isNotEmpty()) own.values.average() else 0.0


        return ((analysisAvg + skillsAvg + ownAvg) / 3.0).roundToInt()
    }
    companion object{
        private const val TAG = "RankingViewModel"
    }
}
