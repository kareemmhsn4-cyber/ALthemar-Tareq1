package com.example.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * دالة بذر البيانات الأولية (Database Seeding) في تطبيق جمعية الثمار
 * لضمان وجود مستخدم المالك (المهندس طارق) والمستخدم التجريبي والنصوص القابلة للتعديل والتحرير بمجرد تشغيل التطبيق.
 */
object DatabaseSeeding {

    suspend fun seedDatabase(context: Context) = withContext(Dispatchers.IO) {
        val database = ThimarDatabase.getDatabase(context)
        val dao = database.thimarDao()

        // 1. بذر المستخدمين (المالك والمستخدم العادي)
        val ownerEmail = "tareq@thimar.com"
        val existingOwner = dao.getUserByEmail(ownerEmail)
        if (existingOwner == null) {
            dao.insertUser(
                User(
                    id = "owner_tareq",
                    email = ownerEmail,
                    fullName = "المهندس طارق الصرفي",
                    role = "OWNER",
                    phone = "0501234567",
                    password = "1234567890",
                    isActive = true,
                    darkModePreference = false
                )
            )
        }

        val userEmail = "user@thimar.com"
        val existingUser = dao.getUserByEmail(userEmail)
        if (existingUser == null) {
            dao.insertUser(
                User(
                    id = "regular_user",
                    email = userEmail,
                    fullName = "مزارع الثمار التجريبي",
                    role = "REGULAR_USER",
                    phone = "0507654321",
                    password = "22222",
                    isActive = true,
                    darkModePreference = false
                )
            )
        }

        // 2. بذر النصوص القابلة للتحرير من قبل المهندس طارق
        val defaultTexts = mapOf(
            "app_name" to "جمعية الثمار",
            "welcome_message" to "أهلاً ومرحباً بكم في منصة جمعية الثمار التعاونية للخدمات الزراعية المتكاملة تحت إشراف م. طارق الصرفي.",
            "consultation_button_text" to "اطلب استشارة زراعية طارئة",
            "footer_text" to "جميع الحقوق محفوظة - جمعية الثمار التعاونية",
            "developer_name" to "Engineer Kareem Al-Sarfi",
            "no_sections_message" to "لا توجد أقسام زراعية مضافة حالياً. يرجى إضافة أقسام جديدة.",
            "owner_password" to "1234567890",
            "user_password" to "22222",
            "dark_mode_text" to "تفعيل المظهر الداكن لتوفير طاقة الشاشة",
            "chat_placeholder" to "اكتب رسالتك في ديوانية مزارعي الثمار..."
        )

        for ((key, value) in defaultTexts) {
            val existingText = dao.getEditableTextByKey(key)
            if (existingText == null) {
                dao.insertEditableText(
                    EditableText(
                        id = key,
                        textKey = key,
                        textValue = value,
                        lastUpdatedBy = "system"
                    )
                )
            }
        }

        // 3. بذر قسمين افتراضيين ليكون التطبيق ممتلئاً وجميلاً من أول استخدام
        val sectionsCount = database.thimarDao().getAllSectionsFlow()
        // سنضيف أقساماً برمجياً إذا كانت قاعدة البيانات فارغة تماماً
        // نتحقق يدوياً لتجنب تكرار الإضافات الافتراضية
    }
}
