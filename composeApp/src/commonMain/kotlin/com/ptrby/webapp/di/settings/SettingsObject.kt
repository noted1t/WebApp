package com.ptrby.webapp.di.settings

import com.russhwolf.settings.Settings
import org.koin.dsl.module
//предоставляет settings и settingsRepository
object SettingsObject {
    val settingsModule = module {
        single {
            Settings()
        }

        single {
            SettingsRepository(get<Settings>())
        }
    }
}