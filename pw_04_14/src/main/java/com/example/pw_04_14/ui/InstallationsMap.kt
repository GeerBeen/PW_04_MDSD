package com.example.pw_04_14.ui

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
import com.example.pw_04_14.data.model.PowerInstallation
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

// Джерело тайлів OpenTopoMap на основі даних OpenStreetMap зі стандартною сіткою координат {z}/{x}/{y}
private val OPENTOPOMAP_TILE_SOURCE = XYTileSource(
    "OpenTopoMap",
    0, 17, 256, ".png",
    arrayOf(
        "https://a.tile.opentopomap.org/",
        "https://b.tile.opentopomap.org/",
        "https://c.tile.opentopomap.org/"
    ),
    "© OpenStreetMap contributors, SRTM"
)

@Composable
fun InstallationsMap(
    installations: List<PowerInstallation>,
    selectedId: Int?,
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
            installations.forEach { installation ->
                val marker = Marker(map).apply {
                    position = GeoPoint(installation.latitude, installation.longitude)
                    title = installation.name
                    infoWindow = null
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
                    setOnMarkerClickListener { _, _ ->
                        onInstallationSelected(installation.id)
                        true
                    }
                }
                map.overlays.add(marker)
            }

            if (selectedId != null && selectedId != lastFocusedId) {
                val selectedObj = installations.find { it.id == selectedId }
                if (selectedObj != null) {
                    val point = GeoPoint(selectedObj.latitude, selectedObj.longitude)
                    map.controller.animateTo(point)
                    map.controller.setZoom(MapConfig.FOCUS_ZOOM)
                    lastFocusedId = selectedId
                }
            }

            map.invalidate()
        }
    )
}
