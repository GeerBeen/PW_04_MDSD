package com.example.pw_04_16

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pw_04_16.ui.InstallationsScreen
import com.example.pw_04_16.ui.theme.PW_04Theme
import org.osmdroid.config.Configuration
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Налаштування конфігурації osmdroid відповідно до політики використання тайлів
        val config = Configuration.getInstance()
        config.userAgentValue = packageName
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
