package com.jamie.businessideasevaluator.Data.Db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.jamie.businessideasevaluator.Data.Model.BusinessIdea
import org.json.JSONObject
import java.util.Date

class DbHelper(context: Context) : SQLiteOpenHelper(context, "BusinessIdeas.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE business_ideas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                businessName TEXT,
                businessDescription TEXT,
                businessScore INTEGER,
                date INTEGER,
                businessTags TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS business_ideas")
        onCreate(db)
    }

    fun insertBusinessIdea(idea: BusinessIdea): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("businessName", idea.businessName)
            put("businessDescription", idea.businessDescription)
            put("businessScore", idea.businessScore)
            put("date", idea.date.time)
            put("businessTags", JSONObject(idea.businessTags).toString()) // Map<String, Int> → JSON
        }

        val outcomeResult = db.insert("business_ideas", null, values) > 0
        Log.d(TAG, "insertBusinessIdea: outcome result: $outcomeResult")
        return outcomeResult
        
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
            val tagsJson = cursor.getString(cursor.getColumnIndexOrThrow("businessTags"))

            // Converts JSON back to Map<String, Int>
            val tags = mutableMapOf<String, Int>()
            val jsonObject = JSONObject(tagsJson)
            jsonObject.keys().forEach { key ->
                tags[key] = jsonObject.getInt(key)
            }

            ideas.add(
                BusinessIdea(
                    businessName = name,
                    businessDescription = description,
                    businessScore = score,
                    date = Date(dateMillis),
                    businessTags = tags
                )
            )
        }

        cursor.close()
        return ideas
    }
    companion object{
        private const val TAG = "DbHelper"
    }
}