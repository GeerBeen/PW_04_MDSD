package com.example.pw_04_14.data.local

import com.example.pw_04_14.data.model.PowerInstallation

/**
 * Локальне джерело даних, що містить зумовлений перелік промислових електроустановок у пам'яті.
 */
class InstallationLocalDataSource {

    fun getInstallations(): List<PowerInstallation> {
        return listOf(
            PowerInstallation(
                id = 1,
                name = "ГПП-1",
                type = "Підстанція",
                location = "Цех 1",
                description = "Головна понижувальна підстанція заводу",
                latitude = 48.4720,
                longitude = 35.0150,
                powerKw = 6300.0
            ),
            PowerInstallation(
                id = 2,
                name = "ДГ-2",
                type = "Генератор",
                location = "Ділянка резервного живлення",
                description = "Дизель-генератор для аварійного живлення",
                latitude = 48.4650,
                longitude = 35.0420,
                powerKw = 800.0
            ),
            PowerInstallation(
                id = 3,
                name = "Насосна станція НС-3",
                type = "Електропривод",
                location = "Цех 3",
                description = "Електропривод насосів охолодження",
                latitude = 48.4580,
                longitude = 35.0550,
                powerKw = 250.0
            ),
            PowerInstallation(
                id = 4,
                name = "Т-4",
                type = "Підстанція",
                location = "Цех 4",
                description = "Силовий трансформатор цехової підстанції",
                latitude = 48.4800,
                longitude = 35.0700,
                powerKw = 1600.0
            ),
            PowerInstallation(
                id = 5,
                name = "Електропіч ЕП-5",
                type = "Технологічна установка",
                location = "Цех 5",
                description = "Дугова електропіч плавильної ділянки",
                latitude = 48.4500,
                longitude = 35.0300,
                powerKw = 1200.0
            ),
            PowerInstallation(
                id = 6,
                name = "Компресорна КС-6",
                type = "Електропривод",
                location = "Цех 6",
                description = "Привід компресорної станції",
                latitude = 48.4400,
                longitude = 35.0800,
                powerKw = 450.0
            )
        )
    }
}
