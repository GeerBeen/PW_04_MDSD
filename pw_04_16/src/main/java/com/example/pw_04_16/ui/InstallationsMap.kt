package com.example.pw_04_16.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.pw_04_16.data.model.PowerInstallation
import com.example.pw_04_16.data.model.UserLocation
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

private val OPENTOPOMAP_TILE_SOURCE = XYTileSource(
    MapConfig.TILE_SOURCE_NAME,
    0, 17, 256, MapConfig.TILE_SOURCE_EXTENSION,
    arrayOf(MapConfig.TILE_SOURCE_BASE_URL),
    "© OpenStreetMap contributors, SRTM"
)

@Composable
fun InstallationsMap(
    installations: List<PowerInstallation>,
    selectedId: Int?,
    userLocation: UserLocation?,
    onInstallationSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(OPENTOPOMAP_TILE_SOURCE)
            setMultiTouchControls(true)
            controller.setZoom(MapConfig.INITIAL_ZOOM)
            controller.setCenter(GeoPoint(MapConfig.INITIAL_LATITUDE, MapConfig.INITIAL_LONGITUDE))
        }
    }

    var lastFocusedId by remember { mutableStateOf<Int?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { map ->
            map.overlays.clear()

            // 1. Маркери електроустановок
            val installationMarkers = mutableMapOf<Int, Marker>()

            installations.forEach { installation ->
                val marker = Marker(map).apply {
                    position = GeoPoint(installation.latitude, installation.longitude)
                    title = installation.name
                    snippet = "${installation.type.displayName} • ${installation.status.displayName}\n" +
                            String.format(Locale.US, "Потужність: %.1f кВт\n", installation.powerKw) +
                            installation.description
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    val isSelected = installation.id == selectedId
                    val currentIcon = icon?.mutate()
                    if (currentIcon != null) {
                        if (isSelected) {
                            DrawableCompat.setTint(currentIcon, android.graphics.Color.RED)
                        } else {
                            currentIcon.clearColorFilter()
                        }
                        icon = currentIcon
                    }

                    setOnMarkerClickListener { m, _ ->
                        onInstallationSelected(installation.id)
                        m.showInfoWindow()
                        true
                    }
                }
                map.overlays.add(marker)
                installationMarkers[installation.id] = marker
            }

            // 2. Маркер геолокації користувача
            if (userLocation != null) {
                val userMarker = Marker(map).apply {
                    position = GeoPoint(userLocation.latitude, userLocation.longitude)
                    title = "Ви тут"
                    snippet = String.format(Locale.US, "Широта: %.4f, Довгота: %.4f", userLocation.latitude, userLocation.longitude)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    val userIcon = icon?.mutate()
                    if (userIcon != null) {
                        DrawableCompat.setTint(userIcon, android.graphics.Color.BLUE)
                        icon = userIcon
                    }
                }
                map.overlays.add(userMarker)
            }

            // 3. Фокусування камери та відкриття InfoWindow для обраного маркера
            if (selectedId != null && selectedId != lastFocusedId) {
                val selectedObj = installations.find { it.id == selectedId }
                if (selectedObj != null) {
                    val point = GeoPoint(selectedObj.latitude, selectedObj.longitude)
                    map.controller.animateTo(point)
                    map.controller.setZoom(MapConfig.FOCUS_ZOOM)
                    installationMarkers[selectedId]?.showInfoWindow()
                    lastFocusedId = selectedId
                }
            }

            map.invalidate()
        }
    )
}
