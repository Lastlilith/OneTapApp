package com.imnidasoftware.onetapapp.presentation.screen.profile

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            ProfileTopBar(
                onSave = {},
                onDeleteAllConfirmed = {}
            )
        },
        content = {}
    )
}