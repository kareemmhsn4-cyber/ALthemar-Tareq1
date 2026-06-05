package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * دالّة الوصول إلى قاعدة البيانات والعمليات الأساسية لجميع الجداول.
 * تدعم القراءة الفورية عبر التدفقات البرمجية Flow والعمليات المؤجلة suspend.
 */
@Dao
interface ThimarDao {

    // --- العمليات على المستخدمين ---
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("UPDATE users SET dark_mode_preference = :darkMode WHERE id = :userId")
    suspend fun updateThemePreference(userId: String, darkMode: Boolean)

    @Query("SELECT * FROM users ORDER BY created_at DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: String)

    // --- العمليات على الأقسام ---
    @Query("SELECT * FROM sections ORDER BY order_index ASC")
    fun getAllSectionsFlow(): Flow<List<Section>>

    @Query("SELECT * FROM sections WHERE is_visible = 1 ORDER BY order_index ASC")
    fun getVisibleSectionsFlow(): Flow<List<Section>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: Section)

    @Query("DELETE FROM sections WHERE id = :id")
    suspend fun deleteSection(id: String)

    // --- العمليات على المحتويات والصور والوسوم ---
    @Query("SELECT * FROM contents WHERE section_id = :sectionId ORDER BY created_at DESC")
    fun getContentsBySectionFlow(sectionId: String): Flow<List<Content>>

    @Query("SELECT * FROM contents WHERE id = :id LIMIT 1")
    fun getContentByIdFlow(id: String): Flow<Content?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: Content)

    @Query("DELETE FROM contents WHERE id = :id")
    suspend fun deleteContent(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentImage(contentImage: ContentImage)

    @Query("SELECT * FROM content_images WHERE content_id = :contentId ORDER BY order_index ASC")
    fun getContentImagesFlow(contentId: String): Flow<List<ContentImage>>

    @Query("DELETE FROM content_images WHERE content_id = :contentId")
    suspend fun deleteImagesByContent(contentId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentTag(tag: ContentTag)

    @Query("SELECT * FROM content_tags WHERE content_id = :contentId")
    fun getContentTagsFlow(contentId: String): Flow<List<ContentTag>>

    @Query("DELETE FROM content_tags WHERE content_id = :contentId")
    suspend fun deleteTagsByContent(contentId: String)

    // البحث في المحتوى والوسوم
    @Query("""
        SELECT DISTINCT c.* FROM contents c 
        LEFT JOIN content_tags t ON c.id = t.content_id 
        WHERE c.title LIKE '%' || :query || '%' OR c.body LIKE '%' || :query || '%' OR t.tag_name LIKE '%' || :query || '%'
    """)
    fun searchContentsFlow(query: String): Flow<List<Content>>

    // --- العمليات على النصوص القابلة للتحرير ---
    @Query("SELECT * FROM editable_texts")
    fun getAllEditableTextsFlow(): Flow<List<EditableText>>

    @Query("SELECT * FROM editable_texts WHERE text_key = :key LIMIT 1")
    suspend fun getEditableTextByKey(key: String): EditableText?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditableText(editableText: EditableText)

    // --- العمليات على الاستشارات ---
    @Query("SELECT * FROM consultations ORDER BY asked_at DESC")
    fun getAllConsultationsFlow(): Flow<List<Consultation>>

    @Query("SELECT * FROM consultations WHERE user_id = :userId ORDER BY asked_at DESC")
    fun getMyConsultationsFlow(userId: String): Flow<List<Consultation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsultation(consultation: Consultation)

    @Query("DELETE FROM consultations WHERE id = :id")
    suspend fun deleteConsultation(id: String)

    // --- العمليات على المهمات ---
    @Query("SELECT * FROM tasks WHERE user_id = :userId ORDER BY due_date ASC")
    fun getMyTasksFlow(userId: String): Flow<List<FarmTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: FarmTask)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: String)

    // --- العمليات على ديوانية الدردشة الجماعية ---
    @Query("SELECT * FROM chat_messages ORDER BY created_at ASC")
    fun getChatMessagesFlow(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)

    // --- العمليات على الإعلانات المنبثقة ---
    @Query("SELECT * FROM popup_announcements WHERE is_active = 1 ORDER BY created_at DESC")
    fun getActiveAnnouncementsFlow(): Flow<List<PopupAnnouncement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: PopupAnnouncement)

    @Query("UPDATE popup_announcements SET is_active = :active WHERE id = :id")
    suspend fun setAnnouncementActive(id: String, active: Boolean)

    // --- العمليات على مواقع حقول المزارعين ---
    @Query("SELECT * FROM field_locations WHERE user_id = :userId ORDER BY created_at DESC")
    fun getFieldLocationsFlow(userId: String): Flow<List<FieldLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFieldLocation(location: FieldLocation)

    @Query("DELETE FROM field_locations WHERE id = :id")
    suspend fun deleteFieldLocation(id: String)

    // --- العمليات على مشاهدات الأقسام لإنتاج وتقارير الاحصائيات المهندس طارق ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSectionView(sectionView: SectionView)

    @Query("SELECT * FROM section_views")
    suspend fun getAllSectionViews(): List<SectionView>
}

@Database(
    entities = [
        User::class, Section::class, Content::class, ContentImage::class,
        EditableText::class, Consultation::class, FarmTask::class, ContentTag::class,
        Statistics::class, ChatMessage::class, PopupAnnouncement::class, FieldLocation::class,
        SectionView::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ThimarDatabase : RoomDatabase() {
    abstract fun thimarDao(): ThimarDao

    companion object {
        @Volatile
        private var INSTANCE: ThimarDatabase? = null

        fun getDatabase(context: Context): ThimarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ThimarDatabase::class.java,
                    "thimar_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
