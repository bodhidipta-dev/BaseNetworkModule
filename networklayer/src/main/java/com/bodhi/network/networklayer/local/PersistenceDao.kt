package com.bodhi.network.networklayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
@Dao
interface PersistenceDao {
    @Query("Select * from LocalCache where identifier = :identifierKey")
    suspend fun getResponseByIdentifierAsync(identifierKey: String): NetworkCallCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateResponse(string: NetworkCallCache)

    @Query("Delete from LocalCache")
    suspend fun clearResponse()

    @Query("select * from SyncTable where type =:type ORDER BY date ASC")
    fun getAllSyncedDataFlow(type: String): Flow<List<NetworkSyncData>>

    @Query("select * from SyncTable where type =:type ORDER BY date ASC")
    fun getAllSyncedDataObservable(type: String): Flow<List<NetworkSyncData>>

    @Query("select * from SyncTable where identifier=:orderID")
    fun getSyncedDataSingle(orderID: String): Flow<NetworkSyncData>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSyncData(list: List<NetworkSyncData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleSync(syncData: NetworkSyncData)

    @Query("delete from SyncTable where type=:type")
    suspend fun deleteAllSyncData(type: String)
}