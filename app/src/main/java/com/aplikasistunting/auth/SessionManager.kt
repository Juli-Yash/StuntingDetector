package com.aplikasistunting.auth

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveParentLoginId(parentId: Int) {
        prefs.edit().putInt("parent_id", parentId).apply()
    }

    fun getParentLoginId(): Int {
        return prefs.getInt("parent_id", -1)
    }

    fun saveActiveChildId(childId: Int) {
        prefs.edit().putInt("active_child_id", childId).apply()
    }

    fun getActiveChildId(): Int {
        return prefs.getInt("active_child_id", -1)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return getParentLoginId() != -1
    }
}