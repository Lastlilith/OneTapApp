package com.imnidasoftware.onetapapp.presentation.screen.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imnidasoftware.onetapapp.domain.model.ApiResponse
import com.imnidasoftware.onetapapp.domain.model.MessageBarState
import com.imnidasoftware.onetapapp.domain.model.User
import com.imnidasoftware.onetapapp.domain.model.UserUpdate
import com.imnidasoftware.onetapapp.domain.repository.Repository
import com.imnidasoftware.onetapapp.util.Constants.MAX_LENGTH
import com.imnidasoftware.onetapapp.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _user: MutableState<User?> = mutableStateOf(null)
    val user: State<User?> = _user

    private val _firstName: MutableState<String> = mutableStateOf("")
    val firstName: State<String> = _firstName

    private val _lastName: MutableState<String> = mutableStateOf("")
    val lastName: State<String> = _lastName

    private val _apiResponse: MutableState<RequestState<ApiResponse>> =
        mutableStateOf(RequestState.Idle)
    val apiResponse: State<RequestState<ApiResponse>> = _apiResponse

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _apiResponse.value = RequestState.Loading
            try {
                val response = repository.getUserInfo()
                _apiResponse.value = RequestState.Success(response)
                _messageBarState.value = MessageBarState(
                    message = response.message,
                    error = response.error
                )
                if (response.user != null) {
                    _user.value = response.user
                    _firstName.value = response.user.name.split(" ").first()
                    _lastName.value = response.user.name.split(" ").last()
                }
            } catch (e: Exception) {
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }
    }

    fun updateUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _apiResponse.value = RequestState.Loading
            try {
                if (user.value != null) {
                    val response = repository.getUserInfo()
                    verifyAndUpdate(currentUser = response)
                    _messageBarState.value = MessageBarState(
                        message = response.message,
                        error = response.error
                    )
                }
            } catch (e: Exception) {
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }
    }

    private fun verifyAndUpdate(currentUser: ApiResponse) {
        val (verified, exception) = if (firstName.value.isEmpty() || lastName.value.isEmpty()) {
            Pair(false, EmptyFieldException())
        } else {
            if (currentUser.user?.name?.split(" ")?.first() == firstName.value &&
                currentUser.user.name.split(" ").last() == lastName.value
            ) {
                Pair(false, NothingToUpdateException())
            } else {
                Pair(true, null)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (verified) {
                val response = repository.updateUser(
                    userUpdate = UserUpdate(
                        firstName = firstName.value,
                        lastName = lastName.value
                    )
                )
                _apiResponse.value = RequestState.Success(response)
                _messageBarState.value = MessageBarState(
                    message = response.message,
                    error = response.error
                )
            } else {
                _apiResponse.value =
                    RequestState.Success(ApiResponse(success = false, error = exception))
                _messageBarState.value = MessageBarState(error = exception)
            }
        }
    }

    fun updateFirstName(newName: String) {
        if (newName.length < MAX_LENGTH) {
            _firstName.value = newName
        }
    }

    fun updateLastName(newName: String) {
        if (newName.length < MAX_LENGTH) {
            _lastName.value = newName
        }
    }

}

class EmptyFieldException(
    override val message: String = "Empty Input Field."
) : Exception()

class NothingToUpdateException(
    override val message: String = "Nothing to Update."
) : Exception()