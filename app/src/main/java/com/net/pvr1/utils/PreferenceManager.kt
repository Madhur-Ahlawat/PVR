package com.net.pvr1.utils

import android.content.Context
import android.content.SharedPreferences
import com.net.pvr1.utils.Constant.Companion.CITY
import com.net.pvr1.utils.Constant.Companion.CITY_CC
import com.net.pvr1.utils.Constant.Companion.IS_LOGIN
import com.net.pvr1.utils.Constant.Companion.LATITUDE
import com.net.pvr1.utils.Constant.Companion.LONGITUDE
import com.net.pvr1.utils.Constant.Companion.PREFS_TOKEN_FILE
import com.net.pvr1.utils.Constant.Companion.USER_DOB
import com.net.pvr1.utils.Constant.Companion.USER_ID
import com.net.pvr1.utils.Constant.Companion.USER_MO_NUMBER
import com.net.pvr1.utils.Constant.Companion.USER_NAME
import com.net.pvr1.utils.Constant.Companion.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceManager @Inject constructor(@ApplicationContext context: Context) {
    var editor: SharedPreferences.Editor? = null

    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        editor = prefs.edit()
        editor?.putString(USER_TOKEN, token)
        editor?.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    //User Name
    fun saveUserName(token: String) {
        editor = prefs.edit()
        editor?.putString(USER_NAME, token)
        editor?.apply()
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    //UserId
    fun saveUserId(userId: String) {
        editor = prefs.edit()
        editor?.putString(USER_ID, userId)
        editor?.apply()
    }

    fun getUserId(): String {
        return prefs.getString(USER_ID, "0")!!
    }

    //UserId
    fun saveMobileNumber(mobileNum: String) {
        val editor = prefs.edit()
        editor.putString(USER_MO_NUMBER, mobileNum)
        editor.apply()
    }

    fun geMobileNumber(): String {
        return prefs.getString(USER_MO_NUMBER, "0")!!
    }

    //Is Login
    fun saveIsLogin(checkLogin: Boolean) {
        editor = prefs.edit()
        editor?.putBoolean(IS_LOGIN, checkLogin)
        editor?.apply()
    }

    fun getIsLogin(): Boolean {
        return prefs.getBoolean(IS_LOGIN, false)
    }

    //ISaveDob
    fun saveDob(checkLogin: String) {
        editor = prefs.edit()
        editor?.putString(USER_DOB, checkLogin)
        editor?.apply()
    }

    fun getSaveDob(): String {
        return prefs.getString(USER_DOB, null).toString()
    }

    //City Name
    fun cityName(city: String) {
        editor = prefs.edit()
        editor?.putString(CITY, city)
        editor?.apply()
    }

    fun getCityName(): String {
        return prefs.getString(CITY, "").toString()
    }

    //City Name CC
    fun cityNameCC(city: String) {
        editor = prefs.edit()
        editor?.putString(CITY_CC, city)
        editor?.apply()
    }

    fun getCityNameCC(): String {
        return prefs.getString(CITY_CC, "").toString()
    }

    //Lat
    fun latData(lat: String) {
        editor = prefs.edit()
        editor?.putString(LATITUDE, lat)
        editor?.apply()
    }

    fun getLatData(): String {
        return prefs.getString(LATITUDE, "").toString()
    }

    //Lang
    fun langData(lang: String) {
        editor = prefs.edit()
        editor?.putString(LONGITUDE, lang)
        editor?.apply()
    }

    fun getLangData(): String {
        return prefs.getString(LONGITUDE, "").toString()
    }


    fun clearData() {
        if (editor != null) {
            editor?.clear()
            editor?.commit()
        }
    }
}