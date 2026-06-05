package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ThimarViewModel
import com.example.ui.screens.AppNavigationShell
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ThemeProvider

/**
 * المدخل البرمجي الرئيسي لتطبيق جمعية الثمار التعاونية
 * يربط بين التهيئة الأساسية، والمظهر العضوي المتغير، ومسارات الشاشات الكلية.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // إتاحة التمدد الكامل وتطبيق معايير ملء الشاشة المتطورة (Edge to Edge)
        enableEdgeToEdge()
        
        setContent {
            // الوصول السريع للنموذج الموحد لحالات وبيانات التطبيق
            val thimarViewModel: ThimarViewModel = viewModel()
            
            // قراءة تفضيل المظهر المفعل حالياً
            val isDark = ThemeProvider.isDarkMode

            MyApplicationTheme(darkTheme = isDark) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var currentScreen by remember { mutableStateOf("splash") }

                    when (currentScreen) {
                        "splash" -> {
                            SplashScreen(
                                onSplashFinished = {
                                    currentScreen = "login"
                                }
                            )
                        }
                        "login" -> {
                            LoginScreen(
                                viewModel = thimarViewModel,
                                onLoginSuccess = {
                                    currentScreen = "main_shell"
                                }
                            )
                        }
                        "main_shell" -> {
                            AppNavigationShell(
                                viewModel = thimarViewModel,
                                onLogout = {
                                    currentScreen = "login"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
