package com.imnidasoftware.onetapapp.presentation.screen.profile

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.imnidasoftware.onetapapp.domain.model.ApiResponse
import com.imnidasoftware.onetapapp.navigation.Screen
import com.imnidasoftware.onetapapp.util.RequestState
import com.google.android.gms.auth.api.identity.Identity
import com.imnidasoftware.onetapapp.domain.model.ApiRequest
import com.imnidasoftware.onetapapp.presentation.screen.common.StartActivityForResult
import com.imnidasoftware.onetapapp.presentation.screen.common.signIn
import retrofit2.HttpException

@ExperimentalCoilApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val apiResponse by profileViewModel.apiResponse
    val clearSessionResponse by profileViewModel.clearSessionResponse
    val messageBarState by profileViewModel.messageBarState

    val user by profileViewModel.user
    val firstName by profileViewModel.firstName
    val lastName by profileViewModel.lastName

    Scaffold(
        topBar = {
            ProfileTopBar(
                onSave = { profileViewModel.updateUserInfo() },
                onDeleteAllConfirmed = { profileViewModel.deleteUser() }
            )
        },
        content = {
            ProfileContent(
                apiResponse = apiResponse,
                messageBarState = messageBarState,
                firstName = firstName,
                onFirstNameChanged = { profileViewModel.updateFirstName(it) },
                lastName = lastName,
                onLastNameChanged = { profileViewModel.updateLastName(it) },
                emailAddress = user?.emailAddress,
                profilePhoto = user?.profilePhoto,
                onSignOutClicked = {
                    profileViewModel.clearSession()
                }
            )
        }
    )

    val activity = LocalContext.current as Activity

    StartActivityForResult(
        key = apiResponse,
        onResultReceived = { tokenId ->
            profileViewModel.verifyTokenOnBackend(request = ApiRequest(tokenId = tokenId))
        },
        onDialogDismissed = {
            profileViewModel.saveSignedInState(signedIn = false)
            navigateToLoginScreen(navController = navController)
        }
    ) { activityLauncher ->
        if (apiResponse is RequestState.Success) {
            val response = (apiResponse as RequestState.Success<ApiResponse>).data
            if (response.error is HttpException && response.error.code() == 401) {
                signIn(
                    activity = activity,
                    accountNotFound = {
                        profileViewModel.saveSignedInState(signedIn = false)
                        navigateToLoginScreen(navController = navController)
                    },
                    launchActivityResult = {
                        activityLauncher.launch(it)
                    }
                )
            }
        } else if (apiResponse is RequestState.Error) {
            profileViewModel.saveSignedInState(signedIn = false)
            navigateToLoginScreen(navController = navController)
        }
    }

    LaunchedEffect(key1 = clearSessionResponse) {
        if (clearSessionResponse is RequestState.Success &&
            (clearSessionResponse as RequestState.Success<ApiResponse>).data.success
        ) {
            val oneTapClient = Identity.getSignInClient(activity)
            oneTapClient.signOut()
            profileViewModel.saveSignedInState(signedIn = false)
            navigateToLoginScreen(navController = navController)
        }
    }
}

private fun navigateToLoginScreen(
    navController: NavHostController
) {
    navController.navigate(route = Screen.Login.route) {
        popUpTo(route = Screen.Login.route) {
            inclusive = true
        }
    }
}