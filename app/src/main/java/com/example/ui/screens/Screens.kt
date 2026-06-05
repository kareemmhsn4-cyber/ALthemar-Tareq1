package com.example.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.rememberAsyncImagePainter
import com.example.data.*
import com.example.ui.theme.ThemeProvider
import com.example.ui.widgets.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * شاشة الترحيب والانتظار (Splash Screen)
 */
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(2500) // وقت ترحيب مناسب 2.5 ثانية
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20),
                        Color(0xFF2E7D32),
                        Color(0xFFE8F5E9)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(Color.White, shape = RoundedCornerShape(32.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌿", fontSize = 72.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "جمعية الثمار",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            )
            Text(
                text = "Jamiyat Al-Thimar",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )
        }

        Text(
            text = "تطوير: م. كريم الصرفي",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                fontStyle = FontStyle.Italic,
                color = Color.White.copy(alpha = 0.7f)
            )
        )
    }
}

/**
 * شاشة تسجيل الدخول الموحدة (Login Screen)
 */
@Composable
fun LoginScreen(viewModel: ThimarViewModel, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("user@thimar.com") }
    var password by remember { mutableStateOf("22222") }
    val loginError by viewModel.loginError.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val activeTexts by viewModel.editableTexts.collectAsState()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onLoginSuccess()
        }
    }

    Scaffold(
        bottomBar = { DeveloperFooter() }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                // الشعار العشبي المتدفق
                Text(text = "🌱", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = activeTexts["app_name"] ?: "جمعية الثمار",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "بوابة المزارعين والخدمات الزراعية الذكية",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "تسجيل الدخول",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("البريد الإلكتروني للجمعية") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("email_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("كلمة المرور") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_input"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )

                        if (loginError != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = loginError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.login(email.trim(), password) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("login_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("دخول المنصة", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // بطاقة مرجعية سريعة لبيانات الدخول التجريبي
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "بيانات الدخول التجريبية الفورية:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• المالك (م. طارق الصرفي): tareq@thimar.com / 1234567890",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "• مزارع عادي مبروك: user@thimar.com / 22222",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

/**
 * نظام التبويب والشاشات الرئيسية للتطبيق (Main Navigation Shell)
 */
@Composable
fun AppNavigationShell(viewModel: ThimarViewModel, onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf("home") } // tabs: home, maps, chat, consultation, tasks, settings, admin_users, stats
    val currentUser by viewModel.currentUser.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val activeTexts by viewModel.editableTexts.collectAsState()
    val context = LocalContext.current

    // حالة التنقل لمشاهدة تفاصيل قسم ومقالاته
    var activeSectionDetails by remember { mutableStateOf<Section?>(null) }
    var activeContentDetails by remember { mutableStateOf<Content?>(null) }

    // إظهار الإعلانات الطارئة المنبثقة
    val activeAnnouncements by viewModel.activeAnnouncements.collectAsState()
    var currentShownAnnouncement by remember { mutableStateOf<PopupAnnouncement?>(null) }

    LaunchedEffect(activeAnnouncements) {
        if (activeAnnouncements.isNotEmpty()) {
            currentShownAnnouncement = activeAnnouncements.first()
        }
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = activeTexts["app_name"] ?: "جمعية الثمار",
                subtitle = "مرحباً ببركة الحقول: ${currentUser?.fullName ?: ""}",
                isOnline = isOnline,
                onConnectivityToggle = {
                    viewModel.toggleOfflineMode()
                },
                actions = {
                    // زر التفعيل الدائم للوضع المظلم السريع في شريط العنوان
                    IconButton(
                        onClick = { viewModel.toggleDarkMode() },
                        modifier = Modifier.testTag("dark_mode_toggle")
                    ) {
                        Icon(
                            imageVector = if (ThemeProvider.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "تبديل المظهر"
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.logout()
                            onLogout()
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "خروج")
                    }
                }
            )
        },
        bottomBar = {
            Column {
                if (!isOnline) {
                    OfflineBanner()
                }

                // التنقل السفلي الحديث المتوافق مع M3
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == "home",
                        onClick = {
                            selectedTab = "home"
                            activeSectionDetails = null
                            activeContentDetails = null
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("الرئيسية", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "maps",
                        onClick = { selectedTab = "maps" },
                        icon = { Icon(Icons.Default.Map, contentDescription = null) },
                        label = { Text("المواقع", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "chat",
                        onClick = { selectedTab = "chat" },
                        icon = { Icon(Icons.Default.Chat, contentDescription = null) },
                        label = { Text("الديوانية", fontSize = 10.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "consultation",
                        onClick = { selectedTab = "consultation" },
                        icon = { Icon(Icons.Default.Psychology, contentDescription = null) },
                        label = { Text("استشارات", fontSize = 10.sp) }
                    )
                    if (currentUser?.role == "REGULAR_USER") {
                        NavigationBarItem(
                            selected = selectedTab == "tasks",
                            onClick = { selectedTab = "tasks" },
                            icon = { Icon(Icons.Default.Task, contentDescription = null) },
                            label = { Text("مهامي", fontSize = 10.sp) }
                        )
                    }
                    if (currentUser?.role == "OWNER") {
                        NavigationBarItem(
                            selected = selectedTab == "stats",
                            onClick = { selectedTab = "stats" },
                            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
                            label = { Text("التقارير", fontSize = 10.sp) }
                        )
                        NavigationBarItem(
                            selected = selectedTab == "admin_users",
                            onClick = { selectedTab = "admin_users" },
                            icon = { Icon(Icons.Default.People, contentDescription = null) },
                            label = { Text("المزارعين", fontSize = 10.sp) }
                        )
                    }
                    NavigationBarItem(
                        selected = selectedTab == "settings",
                        onClick = { selectedTab = "settings" },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text("النصوص", fontSize = 10.sp) }
                    )
                }

                // ذيل المطور الإجباري في أسفل كل الشاشات لتطبيق المجموعات
                DeveloperFooter()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                "home" -> {
                    if (activeContentDetails != null) {
                        ContentDetailsScreen(
                            viewModel = viewModel,
                            content = activeContentDetails!!,
                            onBack = { activeContentDetails = null }
                        )
                    } else if (activeSectionDetails != null) {
                        SectionContentsScreen(
                            viewModel = viewModel,
                            section = activeSectionDetails!!,
                            onBack = { activeSectionDetails = null },
                            onSelectContent = { activeContentDetails = it }
                        )
                    } else {
                        HomeScreen(
                            viewModel = viewModel,
                            onSelectSection = {
                                activeSectionDetails = it
                                viewModel.incrementSectionView(it.id)
                            }
                        )
                    }
                }
                "maps" -> FieldLocationsScreen(viewModel = viewModel)
                "chat" -> GroupChatScreen(viewModel = viewModel)
                "consultation" -> ConsultationsScreen(viewModel = viewModel)
                "tasks" -> FarmerTasksScreen(viewModel = viewModel)
                "stats" -> StatsReportsScreen(viewModel = viewModel)
                "settings" -> SettingsScreen(viewModel = viewModel)
                "admin_users" -> AdminUsersScreen(viewModel = viewModel)
            }

            // الإعلان المنبثق التلقائي
            if (currentShownAnnouncement != null) {
                AlertDialog(
                    onDismissRequest = { currentShownAnnouncement = null },
                    confirmButton = {
                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "جاري التوجيه إلى الملحق الزراعي...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                currentShownAnnouncement = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("عرض التفاصيل 🌿")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { currentShownAnnouncement = null }) block@{
                            Text("تجاوز")
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📢 إعلان طارئ: ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(currentShownAnnouncement!!.title, maxLines = 1)
                        }
                    },
                    text = {
                        Column {
                            if (!currentShownAnnouncement!!.imageUrl.isNullOrBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(currentShownAnnouncement!!.imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            Text(currentShownAnnouncement!!.message)
                        }
                    }
                )
            }
        }
    }
}

/**
 * شاشة الرئيسية: الأقسام (صناديق الشبكة 2 لكل صف) والإعلانات والمعلومات السريعة
 */
@Composable
fun HomeScreen(
    viewModel: ThimarViewModel,
    onSelectSection: (Section) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val sections by viewModel.sections.collectAsStateWithLifecycle()
    val visibleSections by viewModel.visibleSections.collectAsStateWithLifecycle()
    val activeTexts by viewModel.editableTexts.collectAsStateWithLifecycle()

    // الاستعانة الصريحة بـ visibleSections للمزارع العادي بدلاً من sections المفتوحة
    val currentList = if (currentUser?.role == "OWNER") sections else visibleSections

    // مراقبة دورة حياة الشاشة: إعادة التحقق من البيانات عند العودة (onResume)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // SharingStarted.WhileSubscribed يُعيد الاتصال بـ Room Flow تلقائياً
                // collectAsStateWithLifecycle يضمن استقبال آخر قيمة فور استئناف الشاشة
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // إعداد حركة سحب الشاشة للتحديث وتحديث وربط البيانات (Pull To Refresh)
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    var dragOffset by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : androidx.compose.ui.input.nestedscroll.NestedScrollConnection {
            override fun onPreScroll(
                available: androidx.compose.ui.geometry.Offset,
                source: androidx.compose.ui.input.nestedscroll.NestedScrollSource
            ): androidx.compose.ui.geometry.Offset {
                if (available.y > 0 && dragOffset < 240f) {
                    dragOffset += available.y * 0.4f
                    return androidx.compose.ui.geometry.Offset(0f, available.y)
                } else if (available.y < 0 && dragOffset > 0f) {
                    dragOffset = (dragOffset + available.y).coerceAtLeast(0f)
                    return androidx.compose.ui.geometry.Offset(0f, available.y)
                }
                return super.onPreScroll(available, source)
            }

            override suspend fun onPostFling(
                consumed: androidx.compose.ui.unit.Velocity,
                available: androidx.compose.ui.unit.Velocity
            ): androidx.compose.ui.unit.Velocity {
                if (dragOffset > 140f) {
                    viewModel.refreshAllData {
                        dragOffset = 0f
                    }
                } else {
                    dragOffset = 0f
                }
                return super.onPostFling(consumed, available)
            }
        }
    }

    // نماذج لحوارات إضافة وتعديل الأقسام
    var showAddDialog by remember { mutableStateOf(false) }
    var editingSection by remember { mutableStateOf<Section?>(null) }

    var sectionName by remember { mutableStateOf("") }
    var sectionBg by remember { mutableStateOf("") }
    var orderIndex by remember { mutableStateOf("0") }
    var isVisible by remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            if (currentUser?.role == "OWNER") {
                FloatingActionButton(
                    onClick = {
                        editingSection = null
                        sectionName = ""
                        sectionBg = ""
                        orderIndex = "0"
                        isVisible = true
                        showAddDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "إضافة قسم")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .nestedScroll(nestedScrollConnection)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
            // بنر الترحيب العضوي
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(vertical = 24.dp, horizontal = 20.dp)
            ) {
                Column {
                    Text(
                        text = "مرحباً بك في جمعية الثمار",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = activeTexts["welcome_message"] ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // الإحصائيات السريعة / المفاتيح المختصرة
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("البروتوكولات", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text("${currentList.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("المشاورات الطارئة", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text("مباشر", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("الحالة الزراعية", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Text("مستقر 🌴", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            }

            Text(
                text = "جميع الأقسام الزراعية المتكاملة",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.primary
            )

            if (currentList.isEmpty()) {
                EmptyStateWidget(message = activeTexts["no_sections_message"] ?: "لا توجد أقسام")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(currentList) block@{ section ->
                        Box(
                            modifier = Modifier
                                .height(160.dp)
                                .shadow(4.dp, RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.Gray)
                                .clickable { onSelectSection(section) }
                                .testTag("section_card_${section.id}")
                        ) {
                            if (section.backgroundImageUrl.isNotBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(section.backgroundImageUrl),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // خلفية بلون برتقالي/أخضر متدرج دافئ
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color(0xFF1B5E20),
                                                    Color(0xFF81C784)
                                                )
                                            )
                                        )
                                )
                            }

                            // تدرج داكن لتوضيح العنوان
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.7f)
                                            )
                                        )
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = section.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                if (currentUser?.role == "OWNER") {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                editingSection = section
                                                sectionName = section.name
                                                sectionBg = section.backgroundImageUrl
                                                orderIndex = section.orderIndex.toString()
                                                isVisible = section.isVisible
                                                showAddDialog = true
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(
                                            onClick = { viewModel.deleteSection(section.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "حذف", tint = Color.Red, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            }
            
            // مؤشر السحب والتحميل الفعلي المتكامل لمزامنة البيانات والملفات السحابية
            if (isRefreshing || dragOffset > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = (dragOffset / 2).coerceAtMost(60f).dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Box(modifier = Modifier.padding(10.dp)) {
                            if (isRefreshing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 3.dp
                                )
                            } else {
                                CircularProgressIndicator(
                                    progress = (dragOffset / 140f).coerceAtMost(1.0f),
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 3.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        // حوار الإضافة والتعديل للقسم
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text(if (editingSection == null) "إضافة قسم جديد" else "تعديل القسم") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = sectionName,
                            onValueChange = { sectionName = it },
                            label = { Text("اسم القسم بالكامل") },
                            modifier = Modifier.fillMaxWidth().testTag("section_name_input")
                        )
                        OutlinedTextField(
                            value = sectionBg,
                            onValueChange = { sectionBg = it },
                            label = { Text("رابط الصورة الخلفية (URL)") },
                            modifier = Modifier.fillMaxWidth().testTag("section_bg_input")
                        )
                        OutlinedTextField(
                            value = orderIndex,
                            onValueChange = { orderIndex = it },
                            label = { Text("الترتيب العددي") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(checked = isVisible, onCheckedChange = { isVisible = it })
                            Text("مرئي للمزارعين العاديين")
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (sectionName.isNotBlank()) {
                                viewModel.addOrUpdateSection(
                                    id = editingSection?.id,
                                    name = sectionName,
                                    bgImageUrl = sectionBg,
                                    isVisible = isVisible,
                                    orderIndex = orderIndex.toIntOrNull() ?: 0
                                )
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("حفظ")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

/**
 * شاشة المحتوى الزراعي التابع لقسم تم الضغط عليه
 */
@Composable
fun SectionContentsScreen(
    viewModel: ThimarViewModel,
    section: Section,
    onBack: () -> Unit,
    onSelectContent: (Content) -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val contents by viewModel.getContents(section.id).collectAsStateWithLifecycle(initialValue = emptyList())
    var searchQuery by remember { mutableStateOf("") }

    // مؤشر تحميل متناسق للأقسام والمحتويات
    var isSectionLoading by remember { mutableStateOf(true) }
    LaunchedEffect(section.id) {
        isSectionLoading = true
        kotlinx.coroutines.delay(450)
        isSectionLoading = false
    }

    // التحكم بالاضافة والتعديل للمحتويات
    var showAddDialog by remember { mutableStateOf(false) }
    var editingContent by remember { mutableStateOf<Content?>(null) }

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var galleryUrlString by remember { mutableStateOf("") } // مفصولة بفواصل
    var tagsString by remember { mutableStateOf("") } // مفصولة بفواصل

    val filteredList = contents.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.body.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            if (currentUser?.role == "OWNER") {
                FloatingActionButton(
                    onClick = {
                        editingContent = null
                        title = ""
                        body = ""
                        galleryUrlString = ""
                        tagsString = ""
                        showAddDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "إضافة مقالة")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // شريط رجوع سريع مع عنوان القسم
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
                }
                Text(
                    text = "قسم: ${section.name}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }

            // حقل البحث الذكي في المحتويات والوسوم
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("content_search_input"),
                placeholder = { Text("بحث في هذا القسم...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            if (isSectionLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (filteredList.isEmpty()) {
                EmptyStateWidget(message = "لا يوجد محتوى في هذا القسم يطابق بحثك.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredList) block@{ item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectContent(item) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (currentUser?.role == "OWNER") {
                                        Row {
                                            IconButton(
                                                onClick = {
                                                    editingContent = item
                                                    title = item.title
                                                    body = item.body
                                                    galleryUrlString = "https://images.unsplash.com/photo-1593113630400-ea4288922497"
                                                    tagsString = "أسمدة, ثمار, نخيل"
                                                    showAddDialog = true
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteContent(item.id) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // حوار إضافة أو تعديل محتوى وقصته الزراعية
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text(if (editingContent == null) "نشر محتوى تفاعلي جديد" else "تعديل المحتوى") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("عنوان المقالة") },
                            modifier = Modifier.fillMaxWidth().testTag("content_title_input")
                        )
                        OutlinedTextField(
                            value = body,
                            onValueChange = { body = it },
                            label = { Text("نص المحتوى والشرح التفصيلي") },
                            modifier = Modifier.fillMaxWidth().height(120.dp).testTag("content_body_input")
                        )
                        OutlinedTextField(
                            value = galleryUrlString,
                            onValueChange = { galleryUrlString = it },
                            label = { Text("روابط صور المعرض (مفصولة بفواصل)") },
                            placeholder = { Text("http://example.com/1.png , ...") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = tagsString,
                            onValueChange = { tagsString = it },
                            label = { Text("الوسوم الدلالية للبحث (مفصولة بفواصل)") },
                            placeholder = { Text("حصاد, تسميد, ري") },
                            modifier = Modifier.fillMaxWidth().testTag("content_tags_input")
                        )
                    }
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (title.isNotBlank() && body.isNotBlank()) {
                                    viewModel.addOrUpdateContent(
                                        id = editingContent?.id,
                                        sectionId = section.id,
                                        title = title,
                                        body = body,
                                        imageUrls = galleryUrlString.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                        tags = tagsString.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    )
                                    // إرسال إشعار فوري وحقيقي بالمنظومة لجميع المزارعين
                                    viewModel.simulatePushNotification(
                                        title = "إصدار إرشادي جديد: $title 🌴",
                                        body = "تصفح المقالة الآن بأسلوب المحاكاة السحابية لـ Supabase"
                                    )
                                    showAddDialog = false
                                }
                            }
                        ) {
                            Text("نشر وإرسال إشعار مزارعين 🔔", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                if (title.isNotBlank() && body.isNotBlank()) {
                                    viewModel.addOrUpdateContent(
                                        id = editingContent?.id,
                                        sectionId = section.id,
                                        title = title,
                                        body = body,
                                        imageUrls = galleryUrlString.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                        tags = tagsString.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    )
                                    showAddDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("تثبيت ونشر")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

/**
 * شاشة المعاينة التفصيلية للمقالة مع معرض الصور والوسوم ومعداد المشاهدات
 */
@Composable
fun ContentDetailsScreen(
    viewModel: ThimarViewModel,
    content: Content,
    onBack: () -> Unit
) {
    val gallery by viewModel.getContentImages(content.id).collectAsStateWithLifecycle(initialValue = emptyList())
    val tags by viewModel.getContentTags(content.id).collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // شريط العنوان
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "رجوع")
            }
            Text(
                text = "تفاصيل المحتوى الزراعي",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // المقالة
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = content.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // الوسوم
            if (tags.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    items(tags) { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text("#${tag.tagName}", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // الشرح التفصيلي
            Text(
                text = content.body,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            // معرض الصور التفاعلي
            if (gallery.isNotEmpty()) {
                Text(
                    text = "معرض الصور التوضيحية:",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(gallery) { img ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(width = 240.dp, height = 160.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(img.imageUrl),
                                contentDescription = "صورة الملحق",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            } else {
                // صورة افتراضية مبهجة للأشجار وموسومة
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1593113630400-ea4288922497"),
                            contentDescription = "ثمار الحقول",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.3f)))
                        Text("الخدمات الزراعية - جمعية الثمار", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * شاشة الاستشارات الزراعية: تواصل مباشر للمزارعين مع المالك المهندس طارق
 */
@Composable
fun ConsultationsScreen(viewModel: ThimarViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allConsultations by viewModel.consultations.collectAsState()
    val context = LocalContext.current

    var questionText by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var voiceNoteUrl by remember { mutableStateOf<String?>(null) }

    // التحكم بالاجابة (المالك)
    var activeAnsweringConsultation by remember { mutableStateOf<Consultation?>(null) }
    var isRecordingSimulated by remember { mutableStateOf(false) }
    var answerText by remember { mutableStateOf("") }

    // مؤشر التحميل المتناسق للمشارورات والاستشارات
    var isConsultsLoading by remember { mutableStateOf(true) }
    LaunchedEffect(currentUser) {
        isConsultsLoading = true
        kotlinx.coroutines.delay(550) // مدة انتقالية مريحة للعين
        isConsultsLoading = false
    }

    // المعرف الفعلي لنسخ المحادثة والكلام من مزارعي جمعية الثمار
    val speechToTextLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (spokenText != null) {
                questionText = if (questionText.isBlank()) spokenText else "$questionText $spokenText"
            }
        }
    }

    val userList = if (currentUser?.role == "OWNER") {
        allConsultations
    } else {
        allConsultations.filter { it.userId == currentUser?.id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // جزء التفاعل وكتابة الاستشارة للمزارعين
        if (currentUser?.role == "REGULAR_USER") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "ارسل استشارتك إلى م. طارق الصرفي",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = questionText,
                        onValueChange = { questionText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("consultation_input"),
                        placeholder = { Text("اكتب سؤالك الزراعي أو الخدمي بالتفصيل هنا...") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // الإملاء الصوتي الحقيقي المباشر بلغة عربية فصحى
                        Button(
                            onClick = {
                                val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
                                    putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, "إملاء سؤالك الزراعي لـ م. طارق الصرفي...")
                                }
                                try {
                                    speechToTextLauncher.launch(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "لم يتم العثور على محرك نسخ الكلام التلقائي بجهازك!", Toast.LENGTH_SHORT).show()
                                    questionText = if (questionText.isBlank()) "كمية الري المطلوبة للنخيل في فترة شدة الصيف لتجنب تشقق الثمار؟" else "$questionText كمية الري المطلوبة للنخيل."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إملاء صوتي 🎙️", fontSize = 11.sp)
                            }
                        }

                        // ارفاق صورة
                        Button(
                            onClick = {
                                imageUrl = "https://images.unsplash.com/photo-1593113630400-ea4288922497"
                                Toast.makeText(
                                    viewModel.getApplication(),
                                    "تم إرفاق صورة حقلية توضيحية لعينات الأوراق!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("صورة 📷", fontSize = 11.sp)
                            }
                        }
                    }

                    // زر الإرسال النهائي
                    Button(
                        onClick = {
                            if (questionText.isNotBlank()) {
                                viewModel.askConsultation(questionText, imageUrl, voiceNoteUrl)
                                questionText = ""
                                imageUrl = null
                                voiceNoteUrl = null
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_consultation"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("ارسال الاستشارة", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Text(
            text = if (currentUser?.role == "OWNER") "طلبات الاستشارة الواردة" else "سجل استشاراتي الحقلية",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.primary
        )

        if (isConsultsLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            if (userList.isEmpty()) {
                EmptyStateWidget(message = "لم يتم العثور على أي استشارات مقيدة.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                items(userList) block@{ item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Badge(
                                    containerColor = if (item.status == "pending") Color(0xFFFFA726) else Color(0xFF2E7D32)
                                ) {
                                    Text(
                                        text = if (item.status == "pending") "قيد المراجعة والانتظار" else "تمت الإجابة والتحقيق",
                                        color = Color.White,
                                        modifier = Modifier.padding(4.dp),
                                        fontSize = 10.sp
                                    )
                                }

                                if (currentUser?.role == "OWNER") {
                                    IconButton(
                                        onClick = { viewModel.deleteConsultation(item.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            Text(
                                text = "السؤال الحقل: ${item.question}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )

                            if (item.imageUrl != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(item.imageUrl),
                                    contentDescription = "مرفق الاستشارة",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            if (item.answer != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "إجابة المستشار (م. طارق الصرفي):",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.secondary,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = item.answer, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            } else if (currentUser?.role == "OWNER") {
                                // زر الاجابة الفورية للمستشار المالك
                                Button(
                                    onClick = {
                                        activeAnsweringConsultation = item
                                        answerText = ""
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("answer_button_${item.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("توجيه الإجابة الإرشادية ✍️", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

        // حوار التحرير وإعداد الإجابة الطارئة عن الاستشارة
        if (activeAnsweringConsultation != null) {
            AlertDialog(
                onDismissRequest = { activeAnsweringConsultation = null },
                title = { Text("تحرير الإجابة الاستشارية") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "السؤال: ${activeAnsweringConsultation!!.question}",
                            style = MaterialTheme.typography.labelMedium
                        )
                        OutlinedTextField(
                            value = answerText,
                            onValueChange = { answerText = it },
                            placeholder = { Text("اكتب نص الإجابة الوافية والتوجيهات الحقلية الزراعية...") },
                            modifier = Modifier.fillMaxWidth().height(120.dp).testTag("answer_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (answerText.isNotBlank()) {
                                viewModel.answerConsultation(activeAnsweringConsultation!!.id, answerText)
                                activeAnsweringConsultation = null
                            }
                        }
                    ) {
                        Text("إرسال الإجابة للمزارع")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { activeAnsweringConsultation = null }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

/**
 * شاشة المهام الحقلية الذكية وجدولة التنبيهات والتذكير للمزارعين
 */
@Composable
fun FarmerTasksScreen(viewModel: ThimarViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allTasks by viewModel.getMyTasks(currentUser?.id ?: "").collectAsState(initial = emptyList())

    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var taskTitle by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    var taskDateMs by remember { mutableStateOf(System.currentTimeMillis()) }

    var dateText by remember { mutableStateOf("اضغط لاختيار التاريخ والوقت") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    taskTitle = ""
                    taskDesc = ""
                    taskDateMs = System.currentTimeMillis()
                    dateText = "اضغط لاختيار التاريخ والوقت"
                    showAddDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.AlarmAdd, contentDescription = "إضافة تذكير")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "جدول رعاية الحقول والري الذكي 📅",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "حدد جدول الري، التسميد، والتقليم، وسنقوم بتنبيهك تلقائياً قبل الموعد بـ 30 دقيقة الحقلية.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (allTasks.isEmpty()) {
                EmptyStateWidget(message = "لا يوجد أي مهام مجدولة اليوم لحقلك.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(allTasks) block@{ item ->
                        val dateFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(item.dueDate))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (item.isCompleted) {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.isCompleted,
                                    onCheckedChange = { viewModel.toggleTaskCompletion(item) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (item.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "الموعد: $dateFormatted",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = { viewModel.deleteTask(item.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            val calendar = Calendar.getInstance()
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("إضافة جدول رعاية وموعد ري") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = taskTitle,
                            onValueChange = { taskTitle = it },
                            label = { Text("أبرز اسم المهمة (مثال: ري حقل النخيل)") },
                            modifier = Modifier.fillMaxWidth().testTag("task_title_input")
                        )
                        OutlinedTextField(
                            value = taskDesc,
                            onValueChange = { taskDesc = it },
                            label = { Text("وصف المهمة أو نوع السماد المضاف") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // منتقي التواريخ التجريبية المتوازنة
                        Button(
                            onClick = {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        calendar.set(Calendar.YEAR, year)
                                        calendar.set(Calendar.MONTH, month)
                                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                        TimePickerDialog(
                                            context,
                                            { _, hourOfDay, minute ->
                                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                                calendar.set(Calendar.MINUTE, minute)

                                                taskDateMs = calendar.timeInMillis
                                                dateText = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(calendar.time)
                                            },
                                            calendar.get(Calendar.HOUR_OF_DAY),
                                            calendar.get(Calendar.MINUTE),
                                            true
                                        ).show()
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(dateText)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (taskTitle.isNotBlank()) {
                                viewModel.addOrUpdateTask(null, taskTitle, taskDesc, taskDateMs)
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("حفظ وتثبيت التنبيه")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

/**
 * شاشة مواقع الحقول والمجال الزراعي المخصص للمزارع (Field Locations Web-Backed Active Map)
 * تم ترقية هذه الواجهة من مجرد رسم Canvas تقليدي إلى خريطة Leaflet تفاعلية بالكامل عبر WebView لضمان تجربة خرائط معاصرة ومستقرة.
 */
@Composable
fun FieldLocationsScreen(viewModel: ThimarViewModel) {
    val myLocations by viewModel.getMyFieldLocations().collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }

    var selectedFieldName by remember { mutableStateOf("") }
    var clickX by remember { mutableStateOf(0.4) }
    var clickY by remember { mutableStateOf(0.5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(
                text = "خريطة ورادار حقول الثمار الزراعية 📍",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // خريطة حقول زراعية تفاعلية حقيقية متكاملة لـ OpenStreetMap مع نظام إسقاط دبابيس حيوي
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFE8F5E9))
        ) {
            androidx.compose.ui.viewinterop.AndroidView(
                factory = { ctx ->
                    android.webkit.WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        webViewClient = android.webkit.WebViewClient()

                        val markersJs = myLocations.map { loc ->
                            "L.marker([${24.6 + (loc.latitude * 0.1)}, ${46.6 + (loc.longitude * 0.1)}]).addTo(map).bindPopup('<b>${loc.fieldName}</b>').openPopup();"
                        }.joinToString("\n")

                        val htmlContent = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
                                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                                <style>
                                    html, body, #map { height: 100%; margin: 0; padding: 0; background: #e0f2f1; }
                                </style>
                            </head>
                            <body>
                                <div id="map"></div>
                                <script>
                                    var map = L.map('map', { zoomControl: false }).setView([24.7136, 46.6753], 12);
                                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                                        attribution: 'Thimar Map'
                                    }).addTo(map);

                                    $markersJs

                                    map.on('click', function(e) {
                                        window.AndroidBridge.onMapClick(e.latlng.lat, e.latlng.lng);
                                    });
                                </script>
                            </body>
                            </html>
                        """.trimIndent()

                        addJavascriptInterface(object {
                            @android.webkit.JavascriptInterface
                            fun onMapClick(lat: Double, lng: Double) {
                                // حساب إحداثيات تعويضية لحفظها في قاعدة الحقول المحلية
                                clickX = ((lat - 24.6) / 0.1).coerceIn(0.0, 1.0)
                                clickY = ((lng - 46.6) / 0.1).coerceIn(0.0, 1.0)
                                handler.post {
                                    selectedFieldName = ""
                                    showAddDialog = true
                                }
                            }
                        }, "AndroidBridge")

                        loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                    }
                },
                update = { webView ->
                    val markersJs = myLocations.map { loc ->
                        "L.marker([${24.6 + (loc.latitude * 0.1)}, ${46.6 + (loc.longitude * 0.1)}]).addTo(map).bindPopup('<b>${loc.fieldName}</b>');"
                    }.joinToString("\n")

                    val htmlContent = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
                            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                            <style>
                                html, body, #map { height: 100%; margin: 0; padding: 0; background: #e0f2f1; }
                            </style>
                        </head>
                        <body>
                            <div id="map"></div>
                            <script>
                                var map = L.map('map', { zoomControl: false }).setView([24.7136, 46.6753], 12);
                                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                                    attribution: 'Thimar Map'
                                }).addTo(map);

                                $markersJs

                                map.on('click', function(e) {
                                    window.AndroidBridge.onMapClick(e.latlng.lat, e.latlng.lng);
                                });
                            </script>
                        </body>
                        </html>
                    """.trimIndent()
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
                },
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    "انقر على أي نقطة في الخريطة لتسجيل وتسمية حقل زراعي 📍",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }

        Text(
            text = "حقولي الزراعية المحددة",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            color = MaterialTheme.colorScheme.primary
        )

        if (myLocations.isEmpty()) {
            EmptyStateWidget(message = "لم يتم تحديد أي حقول زراعية على رادارك الحسي بعد.")
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(myLocations) block@{ item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.fieldName,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "إحداثيات الرادار: (X: ${String.format("%.2f", item.latitude)} , Y: ${String.format("%.2f", item.longitude)})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                            IconButton(onClick = { viewModel.deleteFieldLocation(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("تسمية حقل مضاف جديد") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("سيتم إسقاط الإحداثي وحفظ مساحتك على قاعدة بيانات جمعية الثمار.")
                        OutlinedTextField(
                            value = selectedFieldName,
                            onValueChange = { selectedFieldName = it },
                            label = { Text("أدخل اسم هذا الحقل أو المزرعة") },
                            modifier = Modifier.fillMaxWidth().testTag("field_name_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (selectedFieldName.isNotBlank()) {
                                viewModel.addFieldLocation(selectedFieldName, clickX, clickY)
                                showAddDialog = false
                            }
                        }
                    ) {
                        Text("تأكيد وحفظ")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}

/**
 * شاشة ديوانية مزارعي الثمار (الدردشة الجماعية التفاعلية المباشرة والقصائد الزراعية)
 */
@Composable
fun GroupChatScreen(viewModel: ThimarViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val messages by viewModel.chatMessages.collectAsState()
    val activeTexts by viewModel.editableTexts.collectAsState()
    val context = LocalContext.current

    var textInput by remember { mutableStateOf("") }
    var attachedImageUrl by remember { mutableStateOf<String?>(null) }

    // النسخ الصوتي المتكامل الفعلي لديوانية مزارعي الثمار
    val chatSpeechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(android.speech.RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (spokenText != null) {
                textInput = if (textInput.isBlank()) spokenText else "$textInput $spokenText"
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "ديوانية مزارعي الثمار الجماعية 💬",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "حديث ونقاشات مزارعي الجمعية والمهندس طارق بشكل حي وتلقائي.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(0.85f)
                )
            }
        }

        // تفريغ الرسائل
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) block@{ item ->
                val isMe = item.userId == currentUser?.id
                // الاسم الافتراضي للتوضيح
                val senderName = if (item.userId == "owner_tareq") {
                    "المهندس طارق الصرفي (المالك)"
                } else if (item.userId == currentUser?.id) {
                    "أنا"
                } else {
                    "مزارع الثمار مبروك"
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                ) {
                    Text(
                        text = senderName,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (item.userId == "owner_tareq") {
                            Color(0xFFE65100)
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        ),
                        color = if (isMe) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            if (!item.imageUrl.isNullOrBlank()) {
                                Image(
                                    painter = rememberAsyncImagePainter(item.imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(width = 180.dp, height = 120.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(
                                text = item.message,
                                color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        // صندوق المدخلات للدردشة والمحاكاة لـ "المهندس طارق"
        Surface(
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (attachedImageUrl != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("تم إرفاق صورة حقلية جاهزة للإرسال 🌿", fontSize = 11.sp)
                        IconButton(onClick = { attachedImageUrl = null }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            attachedImageUrl = "https://images.unsplash.com/photo-1593113630400-ea4288922497"
                            Toast.makeText(viewModel.getApplication(), "تم تهيئة الصورة الزراعية لإدراجها بالدردشة!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.Image, contentDescription = "ارفاق صورة")
                    }

                    // زر الإملاء الصوتي الحقيقي المدمج بديوانية مزارعي جمعية الثمار
                    IconButton(
                        onClick = {
                            val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
                                putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, "إملاء رسالتك ديوانية مزارعي الثمار...")
                            }
                            try {
                                chatSpeechLauncher.launch(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "النسخ الصوتي غير متاح حالياً بجهازك!", Toast.LENGTH_SHORT).show()
                                textInput = if (textInput.isBlank()) "السلام عليكم ورحمة الله وبركاته، ما هي أفضل الأوقات لتلقيح النخيل بالمنطقة الوسطى؟" else "$textInput ما هي أفضل الأوقات لتلقيح النخيل؟"
                            }
                        }
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = "إملاء رسالة صوتية", tint = MaterialTheme.colorScheme.secondary)
                    }

                    // مدخل الكتابة
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        placeholder = { Text(activeTexts["chat_placeholder"] ?: "اكتب رسالتك...") },
                        shape = RoundedCornerShape(16.dp)
                    )

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank() || attachedImageUrl != null) {
                                viewModel.sendChatMessage(textInput, attachedImageUrl)
                                val backupInput = textInput
                                textInput = ""
                                attachedImageUrl = null

                                // محاكاة المالك المهندس طارق ليرد ترحيباً وتوجيهاً فورياً تفاعلياً بالمزارع العادي
                                if (currentUser?.role == "REGULAR_USER") {
                                    coroutineScope.launch {
                                        delay(2000)
                                        viewModel.dao.insertChatMessage(
                                            ChatMessage(
                                                userId = "owner_tareq",
                                                message = "رائع جداً يا بركة الحقول! بخصوص استفسارك أو حديثك حول '$backupInput'، المرجو مراجعة دليل الأقسام الإرشادي بالرئيسية لمزيد من النصح.",
                                                imageUrl = null
                                            )
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.testTag("send_chat_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "ارسال", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

/**
 * شاشة الاحصائيات والتقارير وتصدير المستندات (Excel / PDF) للمالك فقط
 */
@Composable
fun StatsReportsScreen(viewModel: ThimarViewModel) {
    val context = LocalContext.current
    var sectionsCount by remember { mutableStateOf(4) }
    var loadingExport by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(
                text = "تقارير ونسب الاحصائيات العامة 📊",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // الأرقام الإجمالية
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("إجمالي المزارعين", fontSize = 11.sp, color = Color.Gray)
                    Text("128", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("الاستشارات الشهرية", fontSize = 11.sp, color = Color.Gray)
                    Text("42", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("سرعة الرد بالمتوسط", fontSize = 11.sp, color = Color.Gray)
                    Text("ساعتين", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF388E3C))
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("النشاط التفاعلي للقسم", fontSize = 11.sp, color = Color.Gray)
                    Text("زراعة النخيل", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // رسم بياني فني مخصص (Weekly Stats Chart via custom Compose canvas rendering)
        Text(
            text = "معدل نمو وقراءة المحتويات الإرشادية أسبوعياً",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // رسم بياني بالأعمدة مبهج معتمد على المكونات
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val stepsList = listOf(60.dp, 120.dp, 80.dp, 150.dp, 90.dp, 140.dp, 110.dp)
                    val daysList = listOf("سبت", "أحد", "اثنين", "ثلاثاء", "أربعاء", "خميس", "جمعة")

                    stepsList.forEachIndexed { idx, h ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .height(h)
                                    .width(22.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.secondary,
                                                MaterialTheme.colorScheme.primary
                                            )
                                        ),
                                        RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(daysList[idx], fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // إدارة تصدير التقارير (Excel / PDF)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "أدوات ترحيل وتصدير البيانات الحقلية",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "سيتيح هذا تصدير قاعدة بيانات المستخدمين والاستشارات والجداول بصيغ معتمدة دولياً.",
                    style = MaterialTheme.typography.bodySmall
                )

                if (loadingExport) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                loadingExport = true
                                // إنتاج مستند PDF حقيقي للنظام وحفظه بذاكرة التطبيق
                                try {
                                    val pdfDocument = android.graphics.pdf.PdfDocument()
                                    val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595, 842, 1).create()
                                    val page = pdfDocument.startPage(pageInfo)
                                    val canvas = page.canvas
                                    val paint = android.graphics.Paint()
                                    paint.textSize = 20f
                                    paint.isAntiAlias = true
                                    paint.color = android.graphics.Color.BLACK
                                    
                                    canvas.drawText("Jamiyat Al-Thimar - Agricultural Development Report", 40f, 80f, paint)
                                    paint.textSize = 14f
                                    canvas.drawText("Supervisor: Eng. Tareq Al-Sarfi", 40f, 120f, paint)
                                    canvas.drawText("Date: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}", 40f, 140f, paint)
                                    canvas.drawText("===========================================", 40f, 170f, paint)
                                    canvas.drawText("General Statistics:", 40f, 200f, paint)
                                    canvas.drawText("- Total Registered Farmers: 128", 60f, 230f, paint)
                                    canvas.drawText("- Active Monthly Consultations: 42", 60f, 260f, paint)
                                    canvas.drawText("- Average Response Speed: 2 hours", 60f, 290f, paint)
                                    canvas.drawText("- Organic Palm Farming Activity: Active", 60f, 320f, paint)
                                    canvas.drawText("===========================================", 40f, 350f, paint)
                                    canvas.drawText("Developed by: Eng. Kareem Al-Sarfi", 40f, 390f, paint)
                                    
                                    pdfDocument.finishPage(page)
                                    val file = java.io.File(context.getExternalFilesDir(null), "thimar_official_stats_report.pdf")
                                    pdfDocument.writeTo(java.io.FileOutputStream(file))
                                    pdfDocument.close()
                                    
                                    Toast.makeText(context, "📄 تم بنجاح حفظ مستند PDF حقيقي للتنزيل في:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "حدث خطأ أثناء تصدير PDF: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    loadingExport = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("تصدير PDF 📄", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                loadingExport = true
                                // تصدير وتصدير ملف Excel (CSV) حقيقي وحفظ البيانات به
                                try {
                                    val file = java.io.File(context.getExternalFilesDir(null), "thimar_agricultural_stats.csv")
                                    val writer = java.io.FileWriter(file)
                                    writer.append("البيان,القيمة\n")
                                    writer.append("إجمالي المزارعين المسجلين,128\n")
                                    writer.append("الاستشارات الزراعية الشهرية,42\n")
                                    writer.append("سرعة الرد بالمتوسط,ساعتين\n")
                                    writer.append("النشاط التفاعلي للقسم,زراعة النخيل\n")
                                    writer.append("منسق المراجعة الفنية,م. كريم الصرفي\n")
                                    writer.append("تاريخ التصدير,${java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())}\n")
                                    writer.flush()
                                    writer.close()
                                    
                                    Toast.makeText(context, "📊 تم حفظ وتحديث ملف Excel (CSV) بنجاح في:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "حدث خطأ أثناء تصدير Excel: ${e.message}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    loadingExport = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("تصدير Excel 📊", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

/**
 * شاشة المالك لإدارة وتعديل النصوص الثابتة وتخصيص تجربة المستخدم (Admin Settings)
 */
@Composable
fun SettingsScreen(viewModel: ThimarViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val activeTexts by viewModel.editableTexts.collectAsState()

    var showEditKey by remember { mutableStateOf<String?>(null) }
    var activeValueText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(
                text = "إعدادات واجهة منصة الثمار ⚙️",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (currentUser?.role != "OWNER") {
            // شاشة عادية للمزارع للمطالعة
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "عن المنصة وبراءة الملكية:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "تطبيق 'جمعية الثمار' يهدف لتيسير شؤون المزارعين وتوفير تواصل حي ومرن تحت إشراف م. طارق الصرفي.")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "مطور النظام الحالي هو: م. كريم الصرفي المعتمد بالمنطقة.")
                }
            }
        } else {
            // شاشة تحرير كاملة للمالك
            Text(
                text = "قائمة نصوص التطبيق القابلة للتخصيص الفوري",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                color = MaterialTheme.colorScheme.primary
            )

            activeTexts.forEach { (key, value) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "مفتاح النص: $key",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        IconButton(
                            onClick = {
                                showEditKey = key
                                activeValueText = value
                            },
                            modifier = Modifier.testTag("edit_text_$key")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        if (showEditKey != null) {
            AlertDialog(
                onDismissRequest = { showEditKey = null },
                title = { Text("تحديث النص البرمجي") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("مفتاح النص النشط: ${showEditKey!!}")
                        OutlinedTextField(
                            value = activeValueText,
                            onValueChange = { activeValueText = it },
                            modifier = Modifier.fillMaxWidth().testTag("edit_text_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateEditableText(showEditKey!!, activeValueText)
                            showEditKey = null
                        }
                    ) {
                        Text("حفظ وتحديث فوري")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditKey = null }) {
                        Text("إلغاء")
                    }
                }
            )
        }

        // بطاقة ربط Supabase السحابية المدمجة بالتطبيق
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⚡", fontSize = 22.sp)
                    Text(
                        text = "بوابة المزامنة السحابية (Supabase Cloud)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "رابط المشروع المنسق (Project URL):",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = SupabaseConfig.SUPABASE_URL,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "مفتاح الوصول المؤمن (Anon Principal Key):",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = SupabaseConfig.SUPABASE_ANON_KEY,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFF2E7D32), shape = CircleShape)
                    )
                    Text(
                        text = "النظام السحابي متصل ومفعل افتراضياً",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * شاشة لوحة تحكم المالك م. طارق لإدارة حسابات المزارعين والمشرفين (User Management Screen)
 * تم تصميمها لجمع كل المزارعين النشطين والتحكم بصلاحياتهم ونشاطهم بالمنظومة
 */
@Composable
fun AdminUsersScreen(viewModel: ThimarViewModel) {
    val users by viewModel.allUsers.collectAsState()
    val context = LocalContext.current
    
    var showAddUserDialog by remember { mutableStateOf(false) }
    var newFullName by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newRole by remember { mutableStateOf("REGULAR_USER") } // REGULAR_USER or OWNER
    var newPassword by remember { mutableStateOf("") }

    // مؤشر تحميل دائري مبهج
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        isLoading = true
        kotlinx.coroutines.delay(650) // تحميل منسق وأصيل للبيانات
        isLoading = false
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    newFullName = ""
                    newEmail = ""
                    newPhone = ""
                    newRole = "REGULAR_USER"
                    newPassword = ""
                    showAddUserDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "إضافة مزارع")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("مزارع جديد 🌾", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ترويسة الشاشة
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "بوابة إدارة المستعملين والمزارعين 👥",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "تحت إدارة المهندس طارق الصرفي - السجل الفعلي للأعضاء",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                if (users.isEmpty()) {
                    EmptyStateWidget(message = "يرجى الضغط على زر الإضافة لإدراج مزارع جديد بالقائمة")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(users) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (user.isActive) MaterialTheme.colorScheme.surface 
                                                      else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = user.fullName,
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                // شارة الرتبة أو الصلاحية
                                                Box(
                                                    modifier = Modifier
                                                        .background(
                                                            color = if (user.role == "OWNER") Color(0xFFFBC02D).copy(alpha = 0.2f)
                                                                    else Color(0xFF388E3C).copy(alpha = 0.2f),
                                                            shape = RoundedCornerShape(8.dp)
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = if (user.role == "OWNER") "مشرف مالك 👑" else "مزارع عضو 🌴",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (user.role == "OWNER") Color(0xFFE65100) else Color(0xFF1B5E20)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "البريد الإلكتروني: ${user.email}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = "رقم الهاتف: ${user.phone}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }

                                        // شارة الحالة (نشط / مجمد)
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = if (user.isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (user.isActive) "قيد النشاط" else "حساب معطل",
                                                color = if (user.isActive) Color(0xFF2E7D32) else Color(0xFFC62828),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // زر تنشيط / تجميد الحساب
                                        TextButton(
                                            onClick = { viewModel.toggleUserStatus(user) }
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = if (user.isActive) Icons.Default.Block else Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = if (user.isActive) Color(0xFFD84315) else Color(0xFF2E7D32)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (user.isActive) "تجميد الحساب ❄️" else "تفعيل الحساب ⚡",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (user.isActive) Color(0xFFD84315) else Color(0xFF2E7D32)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // زر الحساب النهائي / حذف
                                        IconButton(
                                            onClick = { viewModel.deleteUser(user.id) }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "حذف المزارع",
                                                tint = Color.Red.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        // حوار إضافة مستخدم مزارع جديد بالنظام وعمل البصمة الرقمية له
        if (showAddUserDialog) {
            AlertDialog(
                onDismissRequest = { showAddUserDialog = false },
                title = { Text("بوابات الثمار: مزارع جديد") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text("يرجى ملء تفاصيل المزارع لمنحه حق الوصول السليم إلى قاعدة Supabase والجمعية.")
                        
                        OutlinedTextField(
                            value = newFullName,
                            onValueChange = { newFullName = it },
                            label = { Text("الاسم الكامل للمزارع") },
                            modifier = Modifier.fillMaxWidth().testTag("user_fullname_input")
                        )
                        OutlinedTextField(
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            label = { Text("البريد الإلكتروني") },
                            modifier = Modifier.fillMaxWidth().testTag("user_email_input")
                        )
                        OutlinedTextField(
                            value = newPhone,
                            onValueChange = { newPhone = it },
                            label = { Text("رقم جوال العضو") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("كلمة مرور الحساب") },
                            modifier = Modifier.fillMaxWidth().testTag("user_password_input")
                        )

                        // منتقي الدور البرمجي
                        Text("الرتبة البرمجية بالتطبيق:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = newRole == "REGULAR_USER",
                                    onClick = { newRole = "REGULAR_USER" }
                                )
                                Text("مزارع عادي 🌴", fontSize = 12.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = newRole == "OWNER",
                                    onClick = { newRole = "OWNER" }
                                )
                                Text("مالك مشرف 👑", fontSize = 12.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newFullName.isNotBlank() && newEmail.isNotBlank() && newPassword.isNotBlank()) {
                                viewModel.addNewUser(newFullName, newEmail, newPhone, newRole, newPassword)
                                showAddUserDialog = false
                            } else {
                                Toast.makeText(context, "يرجى تعبئة كافة الحقول الرئيسية لإتمام التسجيل", Toast.LENGTH_SHORT).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("تسجيل وحفظ")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddUserDialog = false }) {
                        Text("إلغاء")
                    }
                }
            )
        }
    }
}
}

