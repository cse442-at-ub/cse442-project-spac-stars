package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//Source: https://www.techotopia.com/index.php/A_Kotlin_Android_SQLite_Database_Tutorial
//https://www.tutorialspoint.com/how-to-use-a-simple-sqlite-database-in-kotlin-android

class DBHandlerSavedList(context: Context) : SQLiteOpenHelper(context, "SPACStars", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSaved =
                "CREATE TABLE IF NOT EXISTS " +
                        "SavedList " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)" +
                        ")"
        db?.execSQL(createTableSaved)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertNewSavedSPAC(ticker: String, name: String){
        if(!getSavedSPACExists(ticker)){
            val values = ContentValues()
            values.put("ticker", ticker)
            values.put("name", name)
            val db = this.writableDatabase
            db.insert("SavedList", null, values)
            db.close()
        }
    }

    fun getAllSavedSPAC(): MutableList<Array<String>>{
        val query = "SELECT * FROM SavedList"
        val db = this.writableDatabase
        val result = db.rawQuery(query, null)

        val finalList: MutableList<Array<String>> = mutableListOf()
        if (result.moveToFirst()) {
            do {
                finalList.add(arrayOf(result.getString(1), result.getString(2)))
            }
            while (result.moveToNext())
            result.close()
        }

        db.close()
        return finalList
    }

    fun getSavedSPACExists(ticker: String): Boolean{
        var exists = false
        val query = "SELECT * FROM SavedList WHERE ticker = \"$ticker\""
        val db = this.writableDatabase
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            exists = true
            result.close()
        }
        result.close()
        return exists
    }


    fun removeSPAC(ticker: String){
        val query = "SELECT * FROM SavedList WHERE ticker = \"$ticker\""
        val db = this.writableDatabase
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            db.delete("SavedList", "ticker = ?", arrayOf(ticker))
            result.close()
        }
        db.close()
    }


}