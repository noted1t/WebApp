package com.ptrby.webapp.di.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.get
import com.russhwolf.settings.set
//Предоставляет методы для добавления, удаления, получения и обновления значений настроек
class SettingsRepository(private val settings: Settings) {
    //Добавляет новое значение в настройки по указанному ключу
    fun addValue(key: String, value: String) {
        settings[key] = value
    }
//Удаляет значение из настроек по указанному ключу
    fun removeValue(key: String) {
        settings.remove(key)
    }
//Возвращает значение настройки по указанному ключу или возвращает null
    fun getValue(key: String): String? {
        return settings[key]
    }
//Проверяет, существует ли значение для указанного ключа в настройках
    fun contains(key: String) : Boolean {
        return settings.contains(key)
    }
//Обновляет значение настройки по указанному ключу
    fun updateValue(key: String, value: String) {
        settings[key] = value
    }
}