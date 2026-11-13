package com.example.pw_04_15.data.local

import com.example.pw_04_15.data.model.InstallationStatus
import com.example.pw_04_15.data.model.InstallationType
import com.example.pw_04_15.data.model.PowerInstallation
import java.time.LocalDate

/**
 * Локальне джерело даних, що надає наперед визначений список промислових електроустановок у пам'яті.
 */
class InstallationLocalDataSource {

    fun getInstallations(): List<PowerInstallation> {
        return listOf(
            PowerInstallation(
                id = 1,
                name = "ГПП-1",
                type = InstallationType.SUBSTATION,
                powerKw = 6300.0,
                lastUpdate = LocalDate.of(2026, 10, 15),
                status = InstallationStatus.ACTIVE,
                location = "Цех 1"
            ),
            PowerInstallation(
                id = 2,
                name = "ДГ-2",
                type = InstallationType.GENERATOR,
                powerKw = 800.0,
                lastUpdate = LocalDate.of(2026, 10, 18),
                status = InstallationStatus.RESERVE,
                location = "Ділянка резервного живлення"
            ),
            PowerInstallation(
                id = 3,
                name = "Насосна станція НС-3",
                type = InstallationType.ELECTRIC_DRIVE,
                powerKw = 250.0,
                lastUpdate = LocalDate.of(2026, 10, 12),
                status = InstallationStatus.ACTIVE,
                location = "Цех 3"
            ),
            PowerInstallation(
                id = 4,
                name = "Т-4",
                type = InstallationType.SUBSTATION,
                powerKw = 1600.0,
                lastUpdate = LocalDate.of(2026, 10, 5),
                status = InstallationStatus.MAINTENANCE,
                location = "Цех 4"
            ),
            PowerInstallation(
                id = 5,
                name = "Електропіч ЕП-5",
                type = InstallationType.TECHNOLOGICAL_UNIT,
                powerKw = 1200.0,
                lastUpdate = LocalDate.of(2026, 10, 20),
                status = InstallationStatus.ACTIVE,
                location = "Цех 5"
            ),
            PowerInstallation(
                id = 6,
                name = "Компресорна КС-6",
                type = InstallationType.ELECTRIC_DRIVE,
                powerKw = 450.0,
                lastUpdate = LocalDate.of(2026, 10, 11),
                status = InstallationStatus.ACTIVE,
                location = "Цех 6"
            ),
            PowerInstallation(
                id = 7,
                name = "ТГ-7",
                type = InstallationType.GENERATOR,
                powerKw = 2500.0,
                lastUpdate = LocalDate.of(2026, 9, 28),
                status = InstallationStatus.MAINTENANCE,
                location = "Енергоблок 2"
            ),
            PowerInstallation(
                id = 8,
                name = "Індукційний нагрівач ІН-8",
                type = InstallationType.TECHNOLOGICAL_UNIT,
                powerKw = 600.0,
                lastUpdate = LocalDate.of(2026, 10, 19),
                status = InstallationStatus.RESERVE,
                location = "Цех 2"
            )
        )
    }
}
