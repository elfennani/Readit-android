package com.elfennani.readit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.elfennani.readit.presentation.ReaditApp
import com.elfennani.readit.presentation.ui.ReaditTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        val code = data?.getQueryParameter("code")
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        Log.d("USERID", userId.toString())

        enableEdgeToEdge()

        setContent {
            ReaditTheme {
                ReaditApp(code = code, user = userId)
            }
        }
    }
}