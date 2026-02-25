package com.Derick.Ideagauge.Data.Db


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.Derick.Ideagauge.Data.Model.BusinessIdea
import org.json.JSONObject
import java.util.Date

class DbHelper(context: Context) : SQLiteOpenHelper(context, "BusinessIdeas.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE business_ideas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                businessName TEXT,
                businessDescription TEXT,
                businessScore INTEGER,
                date INTEGER,
                businessTags TEXT,
                businessAnalysis TEXT,
                personalSkills TEXT,
                ownCriteria TEXT,
                aiSuggestion TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Preserve existing data; only add new columns as needed
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE business_ideas ADD COLUMN aiSuggestion TEXT")
        }
    }

    fun insertBusinessIdea(idea: BusinessIdea): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("businessName", idea.businessName)
            put("businessDescription", idea.businessDescription)
            put("businessScore", idea.businessScore)
            put("date", idea.date.time)
            put("businessTags", JSONObject(idea.businessTags).toString())
            put("businessAnalysis", JSONObject(idea.businessAnalysis).toString())
            put("personalSkills", JSONObject(idea.personalSkills).toString())
            put("ownCriteria", JSONObject(idea.ownCriteria).toString())
            put("aiSuggestion", idea.aiSuggestion) // <-- NEW
        }

        val ok = db.insert("business_ideas", null, values) > 0
        Log.d(TAG, "insertBusinessIdea: outcome result: $ok")
        return ok
    }

    fun getAllBusinessIdeas(): List<BusinessIdea> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM business_ideas", null)

        val ideas = mutableListOf<BusinessIdea>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("businessName"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("businessDescription"))
            val score = cursor.getInt(cursor.getColumnIndexOrThrow("businessScore"))
            val dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow("date"))

            val tags = parseJsonToMap(cursor.getString(cursor.getColumnIndexOrThrow("businessTags")))
            val analysis = parseJsonToMap(cursor.getString(cursor.getColumnIndexOrThrow("businessAnalysis")))
            val skills = parseJsonToMap(cursor.getString(cursor.getColumnIndexOrThrow("personalSkills")))
            val criteria = parseJsonToMap(cursor.getString(cursor.getColumnIndexOrThrow("ownCriteria")))

            // aiSuggestion is new; guard if column doesn't exist on some devices
            val aiSuggestionIdx = cursor.getColumnIndex("aiSuggestion")
            val aiSuggestion = if (aiSuggestionIdx != -1) cursor.getString(aiSuggestionIdx) ?: "" else ""

            ideas.add(
                BusinessIdea(
                    businessName = name,
                    businessDescription = description,
                    businessScore = score,
                    date = Date(dateMillis),
                    businessTags = tags,
                    businessAnalysis = analysis,
                    personalSkills = skills,
                    ownCriteria = criteria,
                    aiSuggestion = aiSuggestion // <-- NEW
                )
            )
        }
        cursor.close()
        return ideas
    }

    fun deleteBusinessIdeaByName(name: String): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete("business_ideas", "businessName = ?", arrayOf(name))
        Log.d(TAG, "deleteBusinessIdeaByName: rows deleted = $rowsDeleted")
        return rowsDeleted > 0
    }

    /** NEW: Update the saved AI suggestion for a given idea name */
    fun updateAiSuggestionByName(name: String, suggestion: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply { put("aiSuggestion", suggestion) }
        val rows = db.update("business_ideas", values, "businessName = ?", arrayOf(name))
        Log.d(TAG, "updateAiSuggestionByName: rows=$rows")
        return rows > 0
    }

    private fun parseJsonToMap(jsonString: String?): Map<String, Int> {
        if (jsonString.isNullOrEmpty()) return emptyMap()
        return try {
            val json = JSONObject(jsonString)
            val map = mutableMapOf<String, Int>()
            json.keys().forEach { key -> map[key] = json.getInt(key) }
            map
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse JSON to Map: $jsonString", e)
            emptyMap()
        }
    }

    companion object {
        private const val TAG = "DbHelper"
    }
}
