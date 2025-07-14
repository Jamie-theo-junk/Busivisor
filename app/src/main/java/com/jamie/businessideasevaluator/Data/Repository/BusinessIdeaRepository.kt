package com.jamie.businessideasevaluator.Data.Repository

import android.util.Log
import com.jamie.businessideasevaluator.Data.Db.DbHelper
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea


class BusinessIdeaRepository(private val dbHelper: DbHelper) {

    fun insertIdea(idea: BusinessIdea): Boolean {
        return dbHelper.insertBusinessIdea(idea)
    }

    fun getAllIdeas(): List<BusinessIdea> {
        val ideas = dbHelper.getAllBusinessIdeas()
            .sortedByDescending { it.date } // Newest to oldest
        Log.d(TAG, "getAllIdeas: Retrieved ${ideas.size} ideas sorted by date")
        return ideas
    }

    fun getIdeaAt(position: Int): BusinessIdea? {
        val allIdeas = getAllIdeas() // Sorted by date
        return allIdeas.getOrNull(position)
    }

    fun getRankedIdeaAt(position: Int): BusinessIdea? {
        val rankedIdeas = getAllRankedIdeas()
        return rankedIdeas.getOrNull(position)
    }

    fun getAllRankedIdeas(): List<BusinessIdea> {
        val rankedIdeas = dbHelper.getAllBusinessIdeas()
            .sortedByDescending { it.businessScore }

        Log.d(TAG, "getAllRankedIdeas: Retrieved ${rankedIdeas.size} ideas ranked by score")
        rankedIdeas.forEachIndexed { index, idea ->
            Log.d(TAG, "Rank #${index + 1}: ${idea.businessName} (Score: ${idea.businessScore})")
        }

        return rankedIdeas
    }

    companion object {
        private const val TAG = "BusinessIdeaRepository"
    }
}


