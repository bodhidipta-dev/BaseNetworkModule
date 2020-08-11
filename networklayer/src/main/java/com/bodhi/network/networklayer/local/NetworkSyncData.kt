package com.bodhi.network.networklayer.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
@Entity(tableName = "SyncTable")
data class NetworkSyncData(
    @PrimaryKey
    val identifier: String,
    val type: String,
    val responseSaved: String,
    @NonNull
    @ColumnInfo(name = "date")
    @TypeConverters(TimestampConverter::class)
    val syncedTime: Long
)