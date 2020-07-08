package com.bodhi.network.networklayer.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
@Entity(tableName = "LocalCache")
data class NetworkCallCache(
    @PrimaryKey
    val identifier: String,
    val responseSaved: String,
    val lastSynchronised: String
)