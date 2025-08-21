package com.Derick.Ideagauge.Data.Model

import java.util.Date

data class BusinessIdea(
    val businessName: String = "",
    val businessDescription: String = "",
    val businessScore: Int = 0,
    val date: Date = Date(),
    val businessTags: Map<String, Int> = emptyMap(),
    val businessAnalysis: Map<String, Int> = emptyMap(),
    val personalSkills: Map<String, Int> = emptyMap(),
    val ownCriteria: Map<String, Int> = emptyMap()
)
