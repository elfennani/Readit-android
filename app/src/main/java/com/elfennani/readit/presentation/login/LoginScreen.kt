package com.elfennani.readit.presentation.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elfennani.readit.utilities.AUTH_REDIRECT
import com.elfennani.readit.utilities.AUTH_SCOPES
import com.elfennani.readit.utilities.AUTH_STATE
import com.elfennani.readit.utilities.AUTH_URL
import com.elfennani.readit.utilities.CLIENT_ID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val context = LocalContext.current

    Scaffold() { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Welcome to Readit",
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(modifier = Modifier.padding(24.dp)) {
                Button(onClick = { initiateLogin(context) }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Login using Reddit")
                }
            }
        }
    }
}

fun initiateLogin(context: Context) {
    val uri = Uri.parse(AUTH_URL).buildUpon()


     mapOf<String, String>(
            "client_id" to CLIENT_ID,
            "response_type" to "code",
            "state" to AUTH_STATE,
            "redirect_uri" to AUTH_REDIRECT,
            "duration" to "permanent",
            "scope" to AUTH_SCOPES.joinToString(separator = ",")
        ).forEach {
         uri.appendQueryParameter(it.key, it.value)
     }


    val browserIntent = Intent(Intent.ACTION_VIEW, uri.build())
    (context as Activity).startActivity(browserIntent)
}