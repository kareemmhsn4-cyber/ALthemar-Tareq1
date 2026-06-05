package com.example.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * ملف النماذج البرمجية لتطبيق جمعية الثمار (Jamiyat Al-Thimar)
 * يحتوي على كل البيانات والجداول المتوافقة تماماً مع قاعدة بيانات Supabase.
 */

// --- جدول المستخدمين ---
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "email")
    val email: String,
    
    @ColumnInfo(name = "full_name")
    val fullName: String,
    
    @ColumnInfo(name = "role")
    val role: String, // OWNER أو REGULAR_USER
    
    @ColumnInfo(name = "phone")
    val phone: String,
    
    @ColumnInfo(name = "password")
    val password: String,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "dark_mode_preference")
    val darkModePreference: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

// --- جدول الأقسام (صناديق الشبكة) ---
@Entity(tableName = "sections")
data class Section(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "background_image_url")
    val backgroundImageUrl: String,
    
    @ColumnInfo(name = "order_index")
    val orderIndex: Int = 0,
    
    @ColumnInfo(name = "is_visible")
    val isVisible: Boolean = true,
    
    @ColumnInfo(name = "created_by")
    val createdBy: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

// --- جدول المحتويات الزراعية ---
@Entity(tableName = "contents")
data class Content(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "section_id")
    val sectionId: String,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "body")
    val body: String,
    
    @ColumnInfo(name = "created_by")
    val createdBy: String,
    
    @ColumnInfo(name = "view_count")
    val viewCount: Int = 0,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

// --- جدول صور المحتويات (معرض الصور) ---
@Entity(tableName = "content_images")
data class ContentImage(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "content_id")
    val contentId: String,
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    
    @ColumnInfo(name = "order_index")
    val orderIndex: Int = 0
)

// --- جدول النصوص القابلة للتحرير من قبل المهندس طارق ---
@Entity(tableName = "editable_texts")
data class EditableText(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "text_key")
    val textKey: String, // مثلاً: app_name, welcome_message, consultation_button_text ...
    
    @ColumnInfo(name = "text_value")
    val textValue: String,
    
    @ColumnInfo(name = "last_updated_by")
    val lastUpdatedBy: String,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

// --- جدول الاستشارات الزراعية ---
@Entity(tableName = "consultations")
data class Consultation(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "user_id")
    val userId: String,
    
    @ColumnInfo(name = "question")
    val question: String,
    
    @ColumnInfo(name = "answer")
    val answer: String? = null,
    
    @ColumnInfo(name = "status")
    val status: String = "pending", // pending أو answered
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,
    
    @ColumnInfo(name = "voice_note_url")
    val voiceNoteUrl: String? = null,
    
    @ColumnInfo(name = "answered_by")
    val answeredBy: String? = null,
    
    @ColumnInfo(name = "asked_at")
    val askedAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "answered_at")
    val answeredAt: Long? = null
)

// --- جدول المهمات التذكيرية للمزارعين ---
@Entity(tableName = "tasks")
data class FarmTask(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "user_id")
    val userId: String,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "due_date")
    val dueDate: Long, // تاريخ الاستحقاق بالملي ثانية
    
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    
    @ColumnInfo(name = "reminder_sent")
    val reminderSent: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

// --- جدول وسوم المحتويات ---
@Entity(tableName = "content_tags")
data class ContentTag(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "content_id")
    val contentId: String,
    
    @ColumnInfo(name = "tag_name")
    val tagName: String
)

// --- جدول الإحصائيات العامة ---
@Entity(tableName = "statistics")
data class Statistics(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "stat_key")
    val statKey: String,
    
    @ColumnInfo(name = "stat_value")
    val statValue: String,
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

// --- جدول غرف المحادثات لغرفة "ديوانية الثمار الجماعية" ---
@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "user_id")
    val userId: String,
    
    @ColumnInfo(name = "message")
    val message: String,
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

// --- جدول الإعلانات المنبثقة الطارئة ---
@Entity(tableName = "popup_announcements")
data class PopupAnnouncement(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "message")
    val message: String,
    
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "created_by")
    val createdBy: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

// --- جدول مواقع حقول المزارعين على الخريطة ---
@Entity(tableName = "field_locations")
data class FieldLocation(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "user_id")
    val userId: String,
    
    @ColumnInfo(name = "field_name")
    val fieldName: String,
    
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

// --- جدول تتبع لمشاهدات الأقسام لعمل إحصائيات المهندس طارق ---
@Entity(tableName = "section_views")
data class SectionView(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    
    @ColumnInfo(name = "section_id")
    val sectionId: String,
    
    @ColumnInfo(name = "viewed_at")
    val viewedAt: Long = System.currentTimeMillis()
)
