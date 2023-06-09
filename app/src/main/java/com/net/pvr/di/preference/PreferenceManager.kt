package com.net.pvr.di.preference

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.net.pvr.utils.Constant.Companion.CITY
import com.net.pvr.utils.Constant.Companion.CITY_CC
import com.net.pvr.utils.Constant.Companion.IS_LOGIN
import com.net.pvr.utils.Constant.Companion.LATITUDE
import com.net.pvr.utils.Constant.Companion.LONGITUDE
import com.net.pvr.utils.Constant.Companion.PREFS_TOKEN_FILE
import com.net.pvr.utils.Constant.Companion.USER_DOB
import com.net.pvr.utils.Constant.Companion.USER_EMAIL
import com.net.pvr.utils.Constant.Companion.USER_ID
import com.net.pvr.utils.Constant.Companion.USER_MO_NUMBER
import com.net.pvr.utils.Constant.Companion.USER_NAME
import com.net.pvr.utils.Constant.Companion.USER_TOKEN
import com.net.pvr.utils.Constant.SharedPreference.Companion.PS
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.net.pvr.R
import com.net.pvr.ui.login.LoginActivity
import kotlin.reflect.jvm.internal.impl.load.java.Constant

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

    //UserId
    fun saveEmail(mobileNum: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, mobileNum)
        editor.apply()
    }

    fun getString(key: String): String {
        return prefs.getString(key, "").toString()
    }

    fun getEmail(): String {
        return prefs.getString(USER_EMAIL, "0")!!
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
    fun saveCityName(city: String) {
        editor = prefs.edit()
        editor?.putString(CITY, city)
        editor?.apply()
    }

    fun getCityName(): String {
        return prefs.getString(CITY, "").toString()
    }

    //City Name Coming Soon
    fun cityNameCinema(cityCinema: String) {
        editor = prefs.edit()
        editor?.putString(CITY_CC, cityCinema)
        editor?.apply()
    }

    fun getCityNameCinema(): String {
        return prefs.getString(CITY_CC, "").toString()
    }

    //Lat
    fun saveLatitudeData(lat: String) {
        editor = prefs.edit()
        editor?.putString(LATITUDE, lat)
        editor?.apply()
    }

    fun getLatitudeData(): String {
        return prefs.getString(LATITUDE, "0.0").toString()
    }

    fun saveString(key:String,bal :String) {
        editor = prefs.edit()
        editor?.putString(key, bal).toString()
        editor?.apply()

    }

    fun saveBoolean(key:String,bal :Boolean) {
        editor = prefs.edit()
        editor?.putBoolean(key, bal).toString()
        editor?.apply()

    }

    fun getBoolean(key:String):Boolean {
        return prefs.getBoolean(key, false)
    }

    //Lang
    fun saveLongitudeData(lang: String) {
        editor = prefs.edit()
        editor?.putString(LONGITUDE, lang)
        editor?.apply()
    }

    //saveLong
    fun saveLong(key: String,bal:Long) {
        editor = prefs.edit()
        editor?.putLong(key, bal)
        editor?.apply()
    }

    fun getLongitudeData(): String {
        return prefs.getString(LONGITUDE, "0.0").toString()
    }


    fun getLong(s: String): Long {
        return prefs.getLong(s, 0)
    }


    fun savePS(ps: String) {
        editor = prefs.edit()
        editor?.putString(PS, ps)
        editor?.apply()
    }

    fun getPS(): String {
        return prefs.getString(PS, "0.0").toString()
    }




    fun clearData(requireActivity: Activity) {
        editor = prefs.edit()
        editor?.putBoolean(IS_LOGIN, false)
        editor?.putString(USER_ID, "")
        editor?.putString(USER_MO_NUMBER, "")
        editor?.putString(USER_NAME, "")
        editor?.putString(com.net.pvr.utils.Constant.SharedPreference.USER_NUMBER, "")
        editor?.putString(com.net.pvr.utils.Constant.SharedPreference.USER_NAME, "")
        editor?.putString(com.net.pvr.utils.Constant.SharedPreference.USER_ID, "")
        editor?.apply()
        editor?.commit()
        val intent = Intent(requireActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity.startActivity(intent)
        requireActivity.finish()
    }
}