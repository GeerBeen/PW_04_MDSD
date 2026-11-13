package com.example.pw_04_16.data.local

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.pw_04_16.data.model.UserLocation
import com.example.pw_04_16.ui.MapConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Джерело даних для отримання поточних координат пристрою за допомогою системного LocationManager.
 *
 * ПОЯСНЕННЯ РОБОТИ:
 * 1. LocationManager: системний сервіс Android для отримання геоданих без використання сторонніх бібліотек.
 * 2. Перевірка дозволів: ContextCompat.checkSelfPermission перевіряє наявність ACCESS_FINE_LOCATION або
 *    ACCESS_COARSE_LOCATION. Якщо дозволу немає, потік закривається за допомогою close(), запобігаючи SecurityException.
 * 3. Очищення в awaitClose: під час завершення збору корутиною слухач знімається через removeUpdates(listener).
 *    Це критично для запобігання витокам пам'яті та марному витрачанню ресурсу акумулятора пристрою.
 */
class LocationDataSource(private val context: Context) {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /**
     * Повертає холодний Flow із поточними координатами користувача.
     */
    fun getLocationFlow(): Flow<UserLocation> = callbackFlow {
        val hasFinePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarsePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFinePermission && !hasCoarsePermission) {
            close()
            return@callbackFlow
        }

        // Отримання останніх відомих координат
        val gpsLocation = try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else null
        } catch (_: SecurityException) { null }

        val networkLocation = try {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } else null
        } catch (_: SecurityException) { null }

        val bestLastLocation = getFresherLocation(gpsLocation, networkLocation)
        bestLastLocation?.let {
            trySend(UserLocation(it.latitude, it.longitude))
        }

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                trySend(UserLocation(location.latitude, location.longitude))
            }

            @Deprecated("Deprecated in API 29")
            override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MapConfig.LOCATION_UPDATE_INTERVAL_MS,
                    MapConfig.LOCATION_MIN_DISTANCE_METERS,
                    locationListener,
                    Looper.getMainLooper()
                )
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MapConfig.LOCATION_UPDATE_INTERVAL_MS,
                    MapConfig.LOCATION_MIN_DISTANCE_METERS,
                    locationListener,
                    Looper.getMainLooper()
                )
            }
        } catch (e: SecurityException) {
            close(e)
            return@callbackFlow
        }

        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }

    private fun getFresherLocation(loc1: Location?, loc2: Location?): Location? {
        if (loc1 == null) return loc2
        if (loc2 == null) return loc1
        return if (loc1.time > loc2.time) loc1 else loc2
    }
}
