package com.bodhi.network.networklayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
@Dao
interface PersistenceDao {
    @Query("Select * from LocalCache where identifier = :identifierKey")
    fun getResponseByIdentifier(identifierKey: String): Single<NetworkCallCache?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateResponse(string: NetworkCallCache): Completable

    @Query("Delete from LocalCache")
    fun clearResponse(): Completable
}