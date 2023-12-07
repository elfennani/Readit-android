package com.elfennani.readit.presentation.auth

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elfennani.readit.common.ResourceDataLess

@Composable
fun ExchangeTokenScreen(authViewModel: AuthViewModel = hiltViewModel(), code: String) {
  val state = authViewModel.state
  val context = LocalContext.current

  LaunchedEffect(key1 = state.value){
    if(state.value is ResourceDataLess.Success){
      val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
      context.startActivity(Intent.makeRestartActivityTask(intent?.component))
      Runtime.getRuntime().exit(0)
    }
  }

  Scaffold {
    Column(
      Modifier.padding(it).fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      CircularProgressIndicator()
      Spacer(modifier = Modifier.height(24.dp))
      Text(text = "Logging you in...")
    }
  }
}
