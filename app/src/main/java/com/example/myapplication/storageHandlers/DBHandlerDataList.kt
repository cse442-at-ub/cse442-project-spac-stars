package com.example.myapplication.storageHandlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.PreparedStatement

interface DBHandlerDataList {

    fun insertNewSPAC(ticker: String?, writableDB: SQLiteDatabase, tablename: String?, columns: Int?, data: Map<String, String>){
        println("inserting")
        if(getSPACData(ticker, writableDB, tablename, columns).isEmpty()){
            val values = ContentValues()
            for((k, v) in data){
                values.put(k,v)
            }
            val db = writableDB
            db.insert(tablename, null, values)
            db.close()
        }
    }

    fun getSPACData(ticker: String?, writableDB: SQLiteDatabase, tablename: String?, columns: Int?): MutableList<String>{
        var info = mutableListOf<String>()
        val db = writableDB

        val result = db.query(false, tablename, null, "ticker=?", arrayOf(ticker), null, null, null, null)


        if (result.moveToFirst()) {
            for(i in 1 until columns as Int + 1){
                info.add(result.getString(i))
            }
            result.close()
        }
        result.close()
        return info
    }

    fun getAllSPACData(writableDB: SQLiteDatabase, tablename: String?, columns: Int?): MutableList<Array<String>>{
        val db = writableDB
        val result = db.query(false, tablename, null, null, null, null, null, null, null)

        val finalList: MutableList<Array<String>> = mutableListOf()
        if (result.moveToFirst()) {
            do {
                val row = mutableListOf<String>()
                for(i in 1 until columns as Int + 1){
                    row.add(result.getString(i))
                }
                val info = row.toTypedArray()
                finalList.add(info)
            }
            while (result.moveToNext())
            result.close()
        }

        db.close()

        return finalList
    }

    fun removeSPAC(ticker: String?, writableDB: SQLiteDatabase, tablename: String?){
        val db = writableDB
        val result = db.query(false, "SavedList", null, "ticker=?", arrayOf(ticker), null, null, null, null)

        if (result.moveToFirst()) {
            db.delete(tablename, "ticker = ?", arrayOf(ticker))
            result.close()
        }
        db.close()
    }

    abstract fun rebuildTable()
    abstract fun createTable()



}