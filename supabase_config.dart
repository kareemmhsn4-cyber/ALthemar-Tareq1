// supabase_config.dart
// ملف إعدادات Supabase الخاص بتطبيق جمعية الثمار
// يحتوي على بيانات الاتصال بقاعدة البيانات السحابية Supabase والمفاتيح الأمنية للاتصال الآمن.

class SupabaseConfig {
  /// الرابط الخاص بمشروع Supabase الخاص بك
  /// قم بتبديل القيمة بالرابط الفعلي لمشروعك إذا تغير مستقبلاً
  static const String SUPABASE_URL = 'https://ymmtmoxcvqzlnbeorpms.supabase.co';

  /// مفتاح الوصول المجهول (Anonymous Key) لتفويض الطلبات بطريقة آمنة
  /// قم بتبديل القيمة بالمفتاح الفعلي لمشروعك إذا تغير مستقبلاً
  static const String SUPABASE_ANON_KEY = 'sb_secret_UhkRFpM6yV3nZIUBJCj05g_yXniO1YR';
}
