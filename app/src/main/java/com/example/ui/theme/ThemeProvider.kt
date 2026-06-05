package com.example.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * مدير مظهر التطبيق (ThemeProvider)
 * يتحكم بحالة المظهر المظلم والتطبيق الفوري للتغييرات عبر جميع الواجهات.
 */
object ThemeProvider {
    // حالة المظهر الداكن المفعلة حالياً في التطبيق
    var isDarkMode by mutableStateOf(false)

    /**
     * تغيير حالة مظهر التطبيق بين المضيء والمظلم
     */
    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }

    /**
     * تعيين حالة المظهر بناءً على تفضيل مخزن مسبقاً بقاعدة البيانات
     */
    fun setTheme(dark: Boolean) {
        isDarkMode = dark
    }
}
