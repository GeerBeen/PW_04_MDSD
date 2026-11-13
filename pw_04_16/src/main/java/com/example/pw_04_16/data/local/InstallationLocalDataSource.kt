package com.example.pw_04_16.data.local

import com.example.pw_04_16.data.model.InstallationStatus
import com.example.pw_04_16.data.model.InstallationType
import com.example.pw_04_16.data.model.PowerInstallation

/**
 * Локальне джерело даних, що містить зумовлений перелік промислових електроустановок у пам'яті.
 */
class InstallationLocalDataSource {

    fun getInstallations(): List<PowerInstallation> {
        return listOf(
            PowerInstallation(
                id = 1,
                name = "ГПП-1",
                type = InstallationType.SUBSTATION,
                status = InstallationStatus.ACTIVE,
                powerKw = 6300.0,
                description = "Головна понижувальна підстанція заводу",
                latitude = 48.4720,
                longitude = 35.0150
            ),
            PowerInstallation(
                id = 2,
                name = "ДГ-2",
                type = InstallationType.GENERATOR,
                status = InstallationStatus.RESERVE,
                powerKw = 800.0,
                description = "Дизель-генератор аварійного живлення",
                latitude = 48.4650,
                longitude = 35.0420
            ),
            PowerInstallation(
                id = 3,
                name = "Насосна станція НС-3",
                type = InstallationType.ELECTRIC_DRIVE,
                status = InstallationStatus.ACTIVE,
                powerKw = 250.0,
                description = "Електропривод насосів охолодження",
                latitude = 48.4580,
                longitude = 35.0550
            ),
            PowerInstallation(
                id = 4,
                name = "Т-4",
                type = InstallationType.SUBSTATION,
                status = InstallationStatus.MAINTENANCE,
                powerKw = 1600.0,
                description = "Силовий трансформатор цехової підстанції",
                latitude = 48.4800,
                longitude = 35.0700
            ),
            PowerInstallation(
                id = 5,
                name = "Електропіч ЕП-5",
                type = InstallationType.TECHNOLOGICAL_UNIT,
                status = InstallationStatus.ACTIVE,
                powerKw = 1200.0,
                description = "Дугова електропіч плавильної ділянки",
                latitude = 48.4500,
                longitude = 35.0300
            ),
            PowerInstallation(
                id = 6,
                name = "Компресорна КС-6",
                type = InstallationType.ELECTRIC_DRIVE,
                status = InstallationStatus.MAINTENANCE,
                powerKw = 450.0,
                description = "Привід компресорної станції",
                latitude = 48.4400,
                longitude = 35.0800
            )
        )
    }
}
