package com.example.pw_04_14

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pw_04_14.ui.InstallationsScreen
import com.example.pw_04_14.ui.theme.PW_04Theme
import org.osmdroid.config.Configuration
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Налаштування унікального User-Agent та шляхів кешу у внутрішній директорії додатка (для сумісності з Android 10+)
        val config = Configuration.getInstance()
        config.userAgentValue = "PowerPlantMonitor/1.0 (+https://example.org; contact: admin@powerplant.org)"
        config.osmdroidBasePath = File(cacheDir, "osmdroid")
        config.osmdroidTileCache = File(cacheDir, "osmdroid/tiles")
        config.load(applicationContext, getSharedPreferences("osmdroid_config", MODE_PRIVATE))

        enableEdgeToEdge()
        setContent {
            PW_04Theme {
                InstallationsScreen()
            }
        }
    }
}
