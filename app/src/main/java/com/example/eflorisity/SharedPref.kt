package com.example.eflorisity

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context, PrefName: String) {
    val sp = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putInt(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: String?) {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString(KEY_NAME, value)
        editor!!.commit()
    }

    fun save(KEY_NAME: String, value: Long) {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putLong(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putBoolean(KEY_NAME, value!!)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: Float) {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putFloat(KEY_NAME, value)
        editor.commit()
    }

    fun saveStringSet(KEY_NAME: String, value: HashSet<String>){
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putStringSet(KEY_NAME, value)
        editor.commit()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sp.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sp.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sp.getBoolean(KEY_NAME, defaultValue)
    }

    fun getValueLong(KEY_NAME: String, defaultValue: Long): Long {
        return sp.getLong(KEY_NAME, defaultValue)
    }

    fun getValueFloat(KEY_NAME: String, defaultValue: Float): Float {
        return sp.getFloat(KEY_NAME, defaultValue)
    }

    fun getValueStringSet(KEY_NAME: String, defaultValue: MutableSet<String>): MutableSet<String>? {
        return sp.getStringSet(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.clear()
        editor.commit()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sp.edit()
        editor.remove(KEY_NAME)
        editor.commit()
    }
}


