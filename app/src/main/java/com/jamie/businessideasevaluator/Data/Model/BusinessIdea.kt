package com.jamie.businessideasevaluator.Data.Model

import java.util.Date

data class BusinessIdea(
    val businessName:String,
    val businessDescription:String,
    val businessScore: Int,
    val date:Date,
    val businessTags: Map<String, Int>
)
