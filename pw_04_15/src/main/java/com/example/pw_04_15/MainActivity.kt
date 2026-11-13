package com.example.pw_04_15

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pw_04_15.ui.InstallationsScreen
import com.example.pw_04_15.ui.theme.PW_04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PW_04Theme {
                InstallationsScreen()
            }
        }
    }
}
