package com.imnidasoftware.onetapapp.data.repository

import com.imnidasoftware.onetapapp.domain.repository.DataStoreOperations
import com.imnidasoftware.onetapapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataStore: DataStoreOperations
): Repository {

    override suspend fun saveSignedInState(signedIn: Boolean) {
        dataStore.saveSignedInState(signedIn)
    }

    override fun readSignedInState(): Flow<Boolean> {
        return dataStore.readSignedInState()
    }

}