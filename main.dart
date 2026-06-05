// main.dart
// المدخل البرمجي الرئيسي لتطبيق Flutter الخاص بجمعية الثمار

import 'package:flutter/material.dart';
import 'package:supabase_flutter/supabase_flutter.dart';
import 'supabase_config.dart'; // استيراد ملف الإعدادات الخاص بقاعدة البيانات

void main() async {
  // التأكد من تهيئة بيئة تشغيل Flutter بشكل صحيح وقبل أي عملية تهيئة خارجية
  WidgetsFlutterBinding.ensureInitialized();

  // تهيئة اتصال Supabase بقاعدة البيانات باستخدام الرابط ومفتاح Anon الكامنين في ملف الإعدادات
  // هنا نقوم بتأمين الاتصال قبل تشغيل أي واجهة رسومية للمستخدم
  await Supabase.initialize(
    url: SupabaseConfig.SUPABASE_URL,
    anonKey: SupabaseConfig.SUPABASE_ANON_KEY,
  );

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'جمعية الثمار التعاونية',
      theme: ThemeData(
        primarySwatch: Colors.green,
        useMaterial3: true,
      ),
      home: const HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('جمعية الثمار - تم الاتصال بنجاح'),
        backgroundColor: Colors.green,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.cloud_done,
              color: Colors.green,
              size: 80,
            ),
            const SizedBox(height: 16),
            const Text(
              'تم تهيئة اتصال Supabase بنجاح!',
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              'رابط الاتصال: ${SupabaseConfig.SUPABASE_URL}',
              style: const TextStyle(color: Colors.grey),
            ),
          ],
        ),
      ),
    );
  }
}
