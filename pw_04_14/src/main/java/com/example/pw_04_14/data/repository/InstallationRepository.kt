package com.example.pw_04_14.data.repository

import com.example.pw_04_14.data.local.InstallationLocalDataSource
import com.example.pw_04_14.data.model.PowerInstallation

/**
 * Репозиторій для роботи з даними про електроустановки.
 */
class InstallationRepository {

    private val localDataSource = InstallationLocalDataSource()

    fun getInstallations(): List<PowerInstallation> {
        return localDataSource.getInstallations()
    }
}
