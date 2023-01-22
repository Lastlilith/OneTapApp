package com.imnidasoftware.onetapapp.presentation.screen.login

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.imnidasoftware.onetapapp.domain.model.MessageBarState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            LoginTopBar()
        },
        content = {
            LoginContent(
                signedInState = false,
                messageBarState = MessageBarState(),
                onButtonClicked = {}
            )
        }
    )
}