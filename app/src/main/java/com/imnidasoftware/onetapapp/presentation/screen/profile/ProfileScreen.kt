package com.imnidasoftware.onetapapp.presentation.screen.profile

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.imnidasoftware.onetapapp.domain.model.ApiResponse
import com.imnidasoftware.onetapapp.domain.model.MessageBarState
import com.imnidasoftware.onetapapp.util.RequestState

@ExperimentalCoilApi
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
        content = {
            ProfileContent(
                apiResponse = RequestState.Success(ApiResponse(success = true)),
                messageBarState = MessageBarState(),
                firstName = "",
                onFirstNameChanged = {},
                lastName = "",
                onLastNameChanged = {},
                emailAddress = "",
                profilePhoto = "",
                onSignOutClicked = {}
            )
        }
    )
}