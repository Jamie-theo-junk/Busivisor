package com.jamie.businessideasevaluator.Data.Repository

import com.jamie.businessideasevaluator.Data.Db.DbHelper
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea

class BusinessIdeaRepository(private val dbHelper: DbHelper) {
fun insertIdea(idea: BusinessIdea): Boolean {
    return dbHelper.insertBusinessIdea(idea)
}

fun getAllIdeas(): List<BusinessIdea> {
    return dbHelper.getAllBusinessIdeas()
}
}

