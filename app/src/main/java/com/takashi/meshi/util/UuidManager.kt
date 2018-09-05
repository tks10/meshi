package com.takashi.meshi.util

import android.content.Context
import java.util.*


class UuidManager(val context: Context){
    companion object {
        val PREF_NAME = "ID"
        val KEY = "UUID"

        fun generateUUID(): String{
            return UUID.randomUUID().toString()
        }
    }

    fun getIdFromPreference(): String?{
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY, null)
    }

    fun storeToPreference(uuid: String){
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY, uuid)
        editor.apply()
    }
}
