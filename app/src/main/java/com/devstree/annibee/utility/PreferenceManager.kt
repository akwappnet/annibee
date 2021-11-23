package com.devstree.annibee.utility

import android.content.Context
import android.content.SharedPreferences
import com.devstree.annibee.BuildConfig
import com.devstree.annibee.Controller

class PreferenceManager {
    companion object {
        private var instance: PreferenceManager? = null
        private var sp: SharedPreferences = Controller.instance.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)


        fun putString(key: String?, value: String?) {
            sp.edit().putString(key, value).apply()
        }

        fun getString(key: String?): String? {
            return sp.getString(key, "")
        }

        fun getString(key: String?, defaultString: String?): String? {
            return sp.getString(key, defaultString)
        }

        fun putBoolean(key: String?, value: Boolean) {
            sp.edit().putBoolean(key, value).apply()
        }

        fun getBoolean(key: String?): Boolean {
            return sp.getBoolean(key, false)
        }

        fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
            return sp.getBoolean(key, defaultValue)
        }

        fun removeKey(key: String?) {
            sp.edit().remove(key).apply()
        }

        fun clearPreference() {
            sp.edit().clear().apply()
        }
    }

}