package com.elfennani.readit.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.elfennani.readit.utilities.AUTH_REDIRECT
import com.elfennani.readit.utilities.AUTH_STATE
import com.elfennani.readit.utilities.AUTH_URL
import com.elfennani.readit.utilities.CLIENT_ID
import com.elfennani.readit.utilities.AUTH_SCOPES

//https://www.reddit.com/api/v1/authorize?client_id=jZxbCi8nroDGjr_X7ss5AQ&response_type=code&state=READIT1&redirect_uri=readit%3A%2F%2Fredirect&duration=permanent&scope=identity%2Cedit%2Cflair%2Chistory%2Cmodconfig%2Cmodflair%2Cmodlog%2Cmodposts%2Cmodwiki%2Cmysubreddits%2Cprivatemessages%2Cread%2Creport%2Csave%2Csubmit%2Csubscribe%2Cvote%2Cwikiedit%2Cwikiread

class LoginViewModel : ViewModel() {
    fun initiateLogin(context: Context){
        val uri = Uri.parse(AUTH_URL).buildUpon()
        val queryParams = mapOf<String, String>(
            "client_id" to CLIENT_ID,
            "response_type" to "code",
            "state" to AUTH_STATE,
            "redirect_uri" to AUTH_REDIRECT,
            "duration" to "permanent",
            "scope" to AUTH_SCOPES.joinToString(separator = ",")
        )


        queryParams.forEach{
            uri.appendQueryParameter(it.key, it.value)
        }

        val browserIntent = Intent(Intent.ACTION_VIEW, uri.build())
        (context as Activity).startActivity(browserIntent)
    }

    suspend fun exchangeCode(){}
}