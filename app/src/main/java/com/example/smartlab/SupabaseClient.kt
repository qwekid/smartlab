package com.example.smartlab

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth

val supabase = createSupabaseClient(
    supabaseUrl = "https://your-project.supabase.co", // Укажите ваш Supabase URL
    supabaseKey = "your-anon-key" // Укажите ваш API ключ
) {
    install(Auth) // Подключение модуля аутентификации
}