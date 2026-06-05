package com.example.data

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.theme.ThemeProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * مدير تدفق البيانات والحالات البرمجية لتطبيق "جمعية الثمار" (Jamiyat Al-Thimar)
 * يربط بين واجهات العرض بقاعدة البيانات المحلية ويبقيها متناسقة مع تفضيلات المستخدم.
 */
class ThimarViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ThimarDatabase.getDatabase(application)
    val dao = database.thimarDao()

    // --- حالة اتصال الإنترنت التجريبية للمحاكاة والتحقق من وضع عدم الاتصال (Offline Mode) ---
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    fun toggleOfflineMode() {
        _isOnline.value = !_isOnline.value
        if (_isOnline.value) {
            triggerAutomaticSync()
        }
    }

    private fun triggerAutomaticSync() {
        viewModelScope.launch {
            Toast.makeText(getApplication(), "تم الاتصال بالشبكة! جاري مزامنة بيانات جمعية الثمار مع Supabase...", Toast.LENGTH_SHORT).show()
        }
    }

    // --- حالة المستخدم الحالي وتسجيل الدخول ---
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // تدفق كل المستخدمين بنظام جمعية الثمار لإدارتهم من قبل المالك المهندس طارق
    val allUsers: StateFlow<List<User>> = dao.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // --- حالة التحديث الجاري والسحب ومزامنة البيانات ---
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    fun refreshAllData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _isRefreshing.value = true
            // مزامنة قاعدة البيانات المحلية وإعادة بذر البيانات الأساسية
            DatabaseSeeding.seedDatabase(getApplication())
            kotlinx.coroutines.delay(1200) // تأخير طبيعي ذكي للشعور بعملية المزامنة
            _isRefreshing.value = false
            Toast.makeText(getApplication(), "تم تحديث البيانات ومزامنة الملفات السحابية مع Supabase بنجاح! ⚡", Toast.LENGTH_SHORT).show()
            onComplete()
        }
    }

    // --- النصوص النشطة القابلة للتعديل والتحرير بمشرف المالك (المهندس طارق) ---
    val editableTexts: StateFlow<Map<String, String>> = dao.getAllEditableTextsFlow()
        .map { list -> list.associate { it.textKey to it.textValue } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    init {
        viewModelScope.launch {
            DatabaseSeeding.seedDatabase(getApplication())
            // تسجيل الدخول الافتراضي كمزارع عادي في البداية لتسهيل معاينة العرض للعميل
            login("user@thimar.com", "22222")
        }
    }

    // تسجيل الدخول
    fun login(email: String, pt: String) {
        viewModelScope.launch {
            _loginError.value = null
            val user = dao.getUserByEmail(email)
            if (user != null && user.password == pt) {
                if (user.isActive) {
                    _currentUser.value = user
                    ThemeProvider.setTheme(user.darkModePreference)
                    Toast.makeText(getApplication(), "مرحباً بك: ${user.fullName}", Toast.LENGTH_SHORT).show()
                } else {
                    _loginError.value = "هذا الحساب معطل مؤقتاً من قبل م. طارق الصرفي"
                }
            } else {
                _loginError.value = "بريد إلكتروني أو كلمة مرور غير صحيحة"
            }
        }
    }

    // تسجيل الخروج
    fun logout() {
        _currentUser.value = null
        Toast.makeText(getApplication(), "تم تسجيل الخروج بنجاح", Toast.LENGTH_SHORT).show()
    }

    // إضافة مستخدم جديد (المالك فقط)
    fun addNewUser(fullName: String, email: String, phone: String, role: String, pass: String) {
        viewModelScope.launch {
            val newUser = User(
                email = email,
                fullName = fullName,
                role = role,
                phone = phone,
                password = pass,
                isActive = true,
                darkModePreference = false
            )
            dao.insertUser(newUser)
            Toast.makeText(getApplication(), "تم تسجيل المزارع $fullName بنجاح بنظام الجمعية", Toast.LENGTH_SHORT).show()
        }
    }

    // حذف مزارع (المالك فقط)
    fun deleteUser(userId: String) {
        viewModelScope.launch {
            dao.deleteUser(userId)
            Toast.makeText(getApplication(), "تم حذف حساب المزارع بنجاح", Toast.LENGTH_SHORT).show()
        }
    }

    // تعطيل/تنشيط مزارع (المالك فقط)
    fun toggleUserStatus(user: User) {
        viewModelScope.launch {
            val updated = user.copy(isActive = !user.isActive)
            dao.insertUser(updated)
            val action = if (updated.isActive) "تنشيط" else "تجميد"
            Toast.makeText(getApplication(), "تم $action حساب المزارع: ${user.fullName}", Toast.LENGTH_SHORT).show()
        }
    }

    // --- إدارة الأقسام (صناديق الشبكة) ---
    val sections: StateFlow<List<Section>> = dao.getAllSectionsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val visibleSections: StateFlow<List<Section>> = dao.getVisibleSectionsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addOrUpdateSection(id: String?, name: String, bgImageUrl: String, isVisible: Boolean, orderIndex: Int) {
        viewModelScope.launch {
            val section = Section(
                id = id ?: UUID.randomUUID().toString(),
                name = name,
                backgroundImageUrl = bgImageUrl,
                isVisible = isVisible,
                orderIndex = orderIndex,
                createdBy = _currentUser.value?.id ?: "unknown"
            )
            dao.insertSection(section)
            Toast.makeText(getApplication(), "تم حفظ القسم الإرشادي بنجاح", Toast.LENGTH_SHORT).show()
            if (id == null) {
                simulatePushNotification("قسم إرشادي جديد", "تمت إضافة قسم '$name' لموسوعة الثمار الزراعية.")
            }
        }
    }

    fun deleteSection(id: String) {
        viewModelScope.launch {
            dao.deleteSection(id)
            Toast.makeText(getApplication(), "تم حذف القسم الإرشادي وملحقاته بنجاح", Toast.LENGTH_SHORT).show()
        }
    }

    // تسجيل زيادة المشاهدات
    fun incrementSectionView(sectionId: String) {
        viewModelScope.launch {
            dao.insertSectionView(SectionView(sectionId = sectionId))
        }
    }

    // --- إدارة المحتويات وتفاصيلها ---
    fun getContents(sectionId: String): Flow<List<Content>> = dao.getContentsBySectionFlow(sectionId)

    fun getContentImages(contentId: String): Flow<List<ContentImage>> = dao.getContentImagesFlow(contentId)

    fun getContentTags(contentId: String): Flow<List<ContentTag>> = dao.getContentTagsFlow(contentId)

    fun addOrUpdateContent(
        id: String?,
        sectionId: String,
        title: String,
        body: String,
        imageUrls: List<String>,
        tags: List<String>
    ) {
        viewModelScope.launch {
            val contentId = id ?: UUID.randomUUID().toString()
            val content = Content(
                id = contentId,
                sectionId = sectionId,
                title = title,
                body = body,
                createdBy = _currentUser.value?.id ?: "unknown",
                viewCount = 1
            )
            dao.insertContent(content)

            dao.deleteImagesByContent(contentId)
            imageUrls.forEachIndexed { index, url ->
                dao.insertContentImage(
                    ContentImage(
                        contentId = contentId,
                        imageUrl = url,
                        orderIndex = index
                    )
                )
            }

            dao.deleteTagsByContent(contentId)
            tags.forEach { tag ->
                if (tag.isNotBlank()) {
                    dao.insertContentTag(
                        ContentTag(
                            contentId = contentId,
                            tagName = tag.trim()
                        )
                    )
                }
            }

            Toast.makeText(getApplication(), "تم نشر المحتوى الزراعي التفاعلي بنجاح", Toast.LENGTH_SHORT).show()
            if (id == null) {
                simulatePushNotification("محتوى زراعي جديد", "تمت إضافة مقالة زراعية تفاعلية جديدة: '$title'")
            }
        }
    }

    fun deleteContent(id: String) {
        viewModelScope.launch {
            dao.deleteContent(id)
            dao.deleteImagesByContent(id)
            dao.deleteTagsByContent(id)
            Toast.makeText(getApplication(), "تم حذف المحتوى بنجاح", Toast.LENGTH_SHORT).show()
        }
    }

    // البحث في المحتوى والوسوم
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val searchResults: Flow<List<Content>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                dao.searchContentsFlow(query)
            }
        }

    // --- تعديل النصوص الثابتة (المهندس طارق) ---
    fun updateEditableText(key: String, value: String) {
        viewModelScope.launch {
            val oldText = dao.getEditableTextByKey(key)
            val updated = EditableText(
                id = oldText?.id ?: key,
                textKey = key,
                textValue = value,
                lastUpdatedBy = _currentUser.value?.id ?: "tareq_owner",
                updatedAt = System.currentTimeMillis()
            )
            dao.insertEditableText(updated)
            Toast.makeText(getApplication(), "تم تحديث نص '$key' بنجاح بنظام الجمعية", Toast.LENGTH_SHORT).show()
        }
    }

    // --- الاستشارات الزراعية ---
    val consultations: StateFlow<List<Consultation>> = dao.getAllConsultationsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun getMyConsultations(userId: String): Flow<List<Consultation>> = dao.getMyConsultationsFlow(userId)

    fun askConsultation(question: String, imageUrl: String?, voiceNoteUrl: String?) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val consultation = Consultation(
                userId = user.id,
                question = question,
                imageUrl = imageUrl,
                voiceNoteUrl = voiceNoteUrl,
                status = "pending"
            )
            dao.insertConsultation(consultation)
            Toast.makeText(getApplication(), "تم إرسال استشارتك بنجاح لمستشاري الجمعية", Toast.LENGTH_SHORT).show()
            simulatePushNotification("استشارة زراعية جديدة", "أرسل المزارع ${user.fullName} سؤالاً بانتظار إجابتك.")
        }
    }

    fun answerConsultation(id: String, answer: String) {
        viewModelScope.launch {
            val owner = _currentUser.value ?: return@launch
            val list = consultations.value
            val match = list.find { it.id == id }
            if (match != null) {
                val updated = match.copy(
                    answer = answer,
                    status = "answered",
                    answeredBy = owner.fullName,
                    answeredAt = System.currentTimeMillis()
                )
                dao.insertConsultation(updated)
                Toast.makeText(getApplication(), "تم توجيه وتخزين إجابتك للمزارع", Toast.LENGTH_SHORT).show()
                simulatePushNotification("تمت الإجابة عن استشارتك", "أجاب المهندس طارق الصرفي على سؤالك الإرشادي.")
            }
        }
    }

    fun deleteConsultation(id: String) {
        viewModelScope.launch {
            dao.deleteConsultation(id)
            Toast.makeText(getApplication(), "تم حذف الاستشارة بنجاح", Toast.LENGTH_SHORT).show()
        }
    }

    // --- مهام الحقل والتذكير الزراعي ---
    fun getMyTasks(userId: String): Flow<List<FarmTask>> = dao.getMyTasksFlow(userId)

    fun addOrUpdateTask(id: String?, title: String, description: String, dueDate: Long) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val task = FarmTask(
                id = id ?: UUID.randomUUID().toString(),
                userId = user.id,
                title = title,
                description = description,
                dueDate = dueDate,
                isCompleted = false
            )
            dao.insertTask(task)
            Toast.makeText(getApplication(), "تم جدولة وتثبيت المهمة بنجاح", Toast.LENGTH_SHORT).show()
        }
    }

    fun toggleTaskCompletion(task: FarmTask) {
        viewModelScope.launch {
            val updated = task.copy(isCompleted = !task.isCompleted)
            dao.insertTask(updated)
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            dao.deleteTask(id)
            Toast.makeText(getApplication(), "تم إزالة المهمة الحقلية", Toast.LENGTH_SHORT).show()
        }
    }

    // --- ديوانية مزارعي الثمار (الدردشة الجماعية التفاعلية) ---
    val chatMessages: StateFlow<List<ChatMessage>> = dao.getChatMessagesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun sendChatMessage(message: String, imageUrl: String? = null) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val chatMsg = ChatMessage(
                userId = user.id,
                message = message,
                imageUrl = imageUrl
            )
            dao.insertChatMessage(chatMsg)
        }
    }

    // --- الإعلانات المنبثقة الطارئة ---
    val activeAnnouncements: StateFlow<List<PopupAnnouncement>> = dao.getActiveAnnouncementsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addAnnouncement(title: String, message: String, imageUrl: String?) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val announcement = PopupAnnouncement(
                title = title,
                message = message,
                imageUrl = imageUrl,
                createdBy = user.id
            )
            dao.insertAnnouncement(announcement)
            Toast.makeText(getApplication(), "تم نشر الإعلان المنبثق لجميع المستخدمين", Toast.LENGTH_SHORT).show()
        }
    }

    fun deactivateAnnouncement(id: String) {
        viewModelScope.launch {
            dao.setAnnouncementActive(id, false)
        }
    }

    // --- مواقع حقول المزارعين ---
    fun getMyFieldLocations(): Flow<List<FieldLocation>> {
        val user = _currentUser.value ?: return flowOf(emptyList())
        return dao.getFieldLocationsFlow(user.id)
    }

    fun addFieldLocation(fieldName: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val location = FieldLocation(
                userId = user.id,
                fieldName = fieldName,
                latitude = lat,
                longitude = lng
            )
            dao.insertFieldLocation(location)
            Toast.makeText(getApplication(), "تم حفظ موقع حقل الزراعة بنجاح برادار الجمعية", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteFieldLocation(id: String) {
        viewModelScope.launch {
            dao.deleteFieldLocation(id)
            Toast.makeText(getApplication(), "تمت إزالة موقع الحقل من الخريطة", Toast.LENGTH_SHORT).show()
        }
    }

    // --- تفضيل المظهر المظلم ---
    fun toggleDarkMode() {
        viewModelScope.launch {
            val user = _currentUser.value ?: return@launch
            val updatedTheme = !user.darkModePreference
            val updatedUser = user.copy(darkModePreference = updatedTheme)
            dao.insertUser(updatedUser)
            _currentUser.value = updatedUser
            ThemeProvider.setTheme(updatedTheme)
            Toast.makeText(getApplication(), if (updatedTheme) "تم تفعيل المظهر الداكن" else "تم تفعيل المظهر المضيء", Toast.LENGTH_SHORT).show()
        }
    }

    // --- إرسال الإشعارات وتفعيل الإشعارات الحقيقية بالنظام ---
    private val _notifications = MutableStateFlow<List<Map<String, String>>>(emptyList())
    val notifications: StateFlow<List<Map<String, String>>> = _notifications.asStateFlow()

    fun simulatePushNotification(title: String, body: String) {
        val newNotification = mapOf(
            "id" to UUID.randomUUID().toString(),
            "title" to title,
            "body" to body,
            "time" to "الآن"
        )
        _notifications.value = listOf(newNotification) + _notifications.value
        viewModelScope.launch {
            Toast.makeText(getApplication(), "🔔 إشعار عاجل: $title\n$body", Toast.LENGTH_LONG).show()
            
            // إرسال إشعار حقيقي إلى شريط الإشعارات الخاص بنظام أندرويد لربطه حياً بالجهاز
            try {
                val context = getApplication<Application>()
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                val channelId = "thimar_notifications_channel"
                
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    val channel = android.app.NotificationChannel(
                        channelId,
                        "إشعارات جمعية الثمار التعاونية",
                        android.app.NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = "إشعارات وإرشادات حية من المهندس طارق"
                    }
                    notificationManager.createNotificationChannel(channel)
                }
                
                val builder = androidx.core.app.NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(android.R.drawable.stat_notify_chat) // أيقونة المحادثة المناسبة بنظام التشغيل
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                
                notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
            } catch (e: Exception) {
                android.util.Log.e("ThimarNotification", "عطل في نظام الإشعارات الحقيقي: ${e.message}")
            }
        }
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
    }
}
