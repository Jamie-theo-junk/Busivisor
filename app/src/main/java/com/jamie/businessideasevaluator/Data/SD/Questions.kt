package com.jamie.businessideasevaluator.Data.SD

import kotlin.collections.Map

class Questions {
    val businessAnalysisQuestions: Map<String, List<String>> = mapOf(
        "Has it been done before" to listOf("No", "Yes"),
        "Does it do things in a more valuable way" to listOf("No", "Yes"),
        "Do I solve a problem" to listOf("No", "Yes"),
        "Do I create delight" to listOf("No", "Yes"),
        "Do I give a benefit" to listOf("No", "Yes"),
        "Can I have competitive advantage" to listOf("No", "Yes"),
        "Is the space crowded" to listOf("Crowded", "Empty"),
        "Does it have better value than rivals" to listOf("No", "Yes"),
        "Is it rare" to listOf("Common", "Rare"),
        "Can it be copied" to listOf("Easily", "Difficult"),
        "Can it  be substituted" to listOf("Yes", "Not Really"),
        "Do I solve a need" to listOf("No", "Yes"),
        "Is it scalable" to listOf("No", "Yes"),
        "Is it uncomplicated" to listOf("No", "Yes"),
        "Does it have a predictable customer base" to listOf("Unpredictable", "Predictable"),
        "Does it have a predictable revenue" to listOf("No", "Yes"),
        "Does it have a high income potential" to listOf("No", "Yes"),
        "Does it have a low entry barrier" to listOf("Low", "High"),
        "Is it in a blue ocean, or red ocean" to listOf("Red Ocean", "Blue Ocean")
    )
   val personalSkillsQuestions: Map<String, List<String>> = mapOf(
        "Does it apply effectuation" to listOf("No", "Yes"),
        "Does it fit my personality" to listOf("No", "Yes"),
        "Am I interested in this" to listOf("No", "Yes"),
        "Do I have passion for this" to listOf("No", "Yes"),
        "Do I have the expertise" to listOf("No", "Yes"),
        "Do I have knowledge" to listOf("No", "Yes"),
        "Can expertise be acquired" to listOf("No", "Yes"),
        "Can I network in this field" to listOf("No", "Yes"),
        "Can I execute it" to listOf("No", "Yes"),
        "Do I have the needed resources" to listOf("No", "Yes"),
        "Can I potentially achieve my goals" to listOf("No", "Yes"),
        "What is the probability for failure" to listOf("Low", "High"),
        "Do I have support from family/friends" to listOf("No", "Yes"),
    )
    var ownCriteria: Map<String, List<String>> = mapOf(
        "Do you have Personal Freedom" to listOf("No", "Yes"),
        "Is it Membership Based" to listOf("No", "Yes"),
        "Is it Meaningful for you" to listOf("No", "Yes")
    )
}