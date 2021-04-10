package com.example.myapplication.storageHandlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

abstract class DBHandlerBase(context: Context) : SQLiteOpenHelper(context, "SPACStars", null, 3){
    override fun onCreate(db: SQLiteDatabase?) {
        var create =
                "CREATE TABLE IF NOT EXISTS " +
                        "SavedList " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)," +
                        "category VARCHAR(256)" +
                        ")"

        db?.execSQL(create)
        create =
                "CREATE TABLE IF NOT EXISTS " +
                        "PreLOI " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)," +
                        "market_cap VARCHAR(256)," +
                        "estimated_trust_value VARCHAR(256)," +
                        "current_volume VARCHAR(256)," +
                        "average_volume VARCHAR(256)," +
                        "warrant_ticker VARCHAR(256)," +
                        "target_focus VARCHAR(256)," +
                        "underwriters VARCHAR(256)," +
                        "IPO_date VARCHAR(256)," +
                        "deadline_date VARCHAR(256)" +
                        ")"

        db?.execSQL(create)
        create =
                "CREATE TABLE IF NOT EXISTS " +
                        "DefAgreement " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)," +
                        "market_cap VARCHAR(256)," +
                        "current_volume VARCHAR(256)," +
                        "volume_average VARCHAR(256)," +
                        "target VARCHAR(256)" +
                        ")"

        db?.execSQL(create)

        create =
                "CREATE TABLE IF NOT EXISTS " +
                        "OptionChads " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)," +
                        "market_cap VARCHAR(256)," +
                        "estimated_trust_value VARCHAR(256)," +
                        "current_volume VARCHAR(256)," +
                        "average_volume VARCHAR(256)" +
                        ")"

        db?.execSQL(create)

        create =
                "CREATE TABLE IF NOT EXISTS " +
                        "PreUnitSplit " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)," +
                        "unit_warrant_details VARCHAR(256)," +
                        "estimated_trust_size VARCHAR(256)," +
                        "prominent_leadership VARCHAR(256)," +
                        "target_focus VARCHAR(256)" +
                        ")"

        db?.execSQL(create)

        create =
                "CREATE TABLE IF NOT EXISTS " +
                        "PreIPO " +
                        "(" +
                        "sl_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ticker VARCHAR(256)," +
                        "name VARCHAR(256)," +
                        "estimated_trust_value VARCHAR(256)," +
                        "management_team VARCHAR(256)," +
                        "target_focus VARCHAR(256)" +
                        ")"

        db?.execSQL(create)
//        db?.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS SavedList")
        db?.execSQL("DROP TABLE IF EXISTS PreLOI")
        db?.execSQL("DROP TABLE IF EXISTS DefAgreement")
        db?.execSQL("DROP TABLE IF EXISTS OptionChads")
        db?.execSQL("DROP TABLE IF EXISTS PreUnitSplit")
        db?.execSQL("DROP TABLE IF EXISTS PreIPO")
        onCreate(db)
    }

    fun closeDB(){
        val db = this.writableDatabase
        db.close()
    }

}