package com.aplikasistunting.auth

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // Simpan ID akun Parent (Orang Tua) yang sedang login
    fun saveParentLoginId(parentId: Int) {
        prefs.edit().putInt("parent_id", parentId).apply()
    }

    fun getParentLoginId(): Int {
        return prefs.getInt("parent_id", -1)
    }

    // Simpan ID anak aktif yang sedang dipilih
    fun saveActiveChildId(childId: Int) {
        prefs.edit().putInt("active_child_id", childId).apply()
    }

    fun getActiveChildId(): Int {
        return prefs.getInt("active_child_id", -1)
    }

    // Logout atau hapus semua session
    fun clearSession() {
        prefs.edit().clear().apply()
    }

    // Optional: cek apakah user sudah login
    fun isLoggedIn(): Boolean {
        return getParentLoginId() != -1
    }
}