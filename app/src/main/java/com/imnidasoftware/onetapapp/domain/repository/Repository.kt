package com.imnidasoftware.onetapapp.domain.repository

import com.imnidasoftware.onetapapp.domain.model.ApiRequest
import com.imnidasoftware.onetapapp.domain.model.ApiResponse
import com.imnidasoftware.onetapapp.domain.model.UserUpdate
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun saveSignedInState(signedIn: Boolean)
    fun readSignedInState(): Flow<Boolean>
    suspend fun verifyTokenOnBackend(request: ApiRequest): ApiResponse
    suspend fun getUserInfo(): ApiResponse
    suspend fun updateUser(userUpdate: UserUpdate): ApiResponse
    suspend fun deleteUser(): ApiResponse
    suspend fun clearSession(): ApiResponse
}