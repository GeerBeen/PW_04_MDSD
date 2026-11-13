package com.example.pw_04_15.data.repository

import com.example.pw_04_15.data.local.InstallationLocalDataSource
import com.example.pw_04_15.data.model.PowerInstallation

/**
 * Репозиторій для доступу до списку електроустановок.
 */
class InstallationRepository {

    private val localDataSource = InstallationLocalDataSource()

    fun getInstallations(): List<PowerInstallation> {
        return localDataSource.getInstallations()
    }
}
