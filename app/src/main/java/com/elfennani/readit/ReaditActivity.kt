package com.elfennani.readit

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.elfennani.readit.compose.ReaditApp
import com.elfennani.readit.data.UserDao
import com.elfennani.readit.ui.ReaditTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

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