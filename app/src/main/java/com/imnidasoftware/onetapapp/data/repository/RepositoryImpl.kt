package com.imnidasoftware.onetapapp.data.repository

import com.imnidasoftware.onetapapp.data.remote.KtorApi
import com.imnidasoftware.onetapapp.domain.model.ApiRequest
import com.imnidasoftware.onetapapp.domain.model.ApiResponse
import com.imnidasoftware.onetapapp.domain.model.UserUpdate
import com.imnidasoftware.onetapapp.domain.repository.DataStoreOperations
import com.imnidasoftware.onetapapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataStoreOperations: DataStoreOperations,
    private val ktorApi: KtorApi
): Repository {

    override suspend fun saveSignedInState(signedIn: Boolean) {
        dataStoreOperations.saveSignedInState(signedIn)
    }

    override fun readSignedInState(): Flow<Boolean> {
        return dataStoreOperations.readSignedInState()
    }

    override suspend fun verifyTokenOnBackend(request: ApiRequest): ApiResponse {
        return try {
            ktorApi.verifyTokenOnBackend(request = request)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun getUserInfo(): ApiResponse {
        return try {
            ktorApi.getUserInfo()
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun updateUser(userUpdate: UserUpdate): ApiResponse {
        return try {
            ktorApi.updateUser(userUpdate = userUpdate)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun deleteUser(): ApiResponse {
        return try {
            ktorApi.deleteUser()
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

    override suspend fun clearSession(): ApiResponse {
        return try {
            ktorApi.clearSession()
        } catch (e: Exception) {
            ApiResponse(success = false, error = e)
        }
    }

}