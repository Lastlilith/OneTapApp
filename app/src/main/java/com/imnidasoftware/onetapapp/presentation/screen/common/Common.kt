package com.imnidasoftware.onetapapp.presentation.screen.common

import android.app.Activity
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.imnidasoftware.onetapapp.util.Constants.CLIENT_ID

fun signIn(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    accountNotFound: () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(CLIENT_ID)
                .setFilterByAuthorizedAccounts(true)
                .build()
        ).setAutoSelectEnabled(true)
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                Log.d("SignIn", "Couldn't start One Tap UI: ${e.message}")
            }
        }
        .addOnFailureListener {
            Log.d("SignIn", "Signing Up....")
            signUp(
                activity,
                launchActivityResult,
                accountNotFound
            )
        }
}

fun signUp(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    accountNotFound: () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        ).build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                Log.d("SignIn", "Couldn't start One Tap UI: ${e.message}")
            }
        }
        .addOnFailureListener {
            Log.d("SignIn", "Signing Up....")
            accountNotFound()
        }
}