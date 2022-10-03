package com.net.pvr1.di.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class AppPreferences @Inject constructor(@ApplicationContext context: Context) {
    var preferenceName = "PVR"
    var preference: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    //create context in app module for using dagger in shared Preferecees
    @SuppressLint("CommitPrefEdits")
    @Inject
    fun consultantPreferences(ctx: Context) {
        try {
            preference =
                ctx.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
            editor = preference!!.edit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearData() {
        if (editor != null) {
            editor!!.clear()
            editor!!.commit()
        }
    }


    fun putString(key: String?, value: String?) {
        if (editor != null) {
            editor!!.putString(key, value)
            editor!!.commit()
        }
    }

    fun putInt(key: String?, value: Int) {
        if (editor != null) {
            editor!!.putInt(key, value)
            editor!!.commit()
        }
    }

    fun putBoolean(key: String?, value: Boolean) {
        if (editor != null) {
            editor!!.putBoolean(key, value)
            editor!!.commit()
        }
    }


    fun getBoolean(key: String?): Boolean {
        return if (preference != null) preference!!.getBoolean(key, false) else false
    }

    fun getString(key: String?): String? {
        return if (preference != null) preference!!.getString(key, "") else ""
    }

}

