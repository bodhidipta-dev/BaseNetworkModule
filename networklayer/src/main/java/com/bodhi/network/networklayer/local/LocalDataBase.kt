package com.bodhi.network.networklayer.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
@Database(entities = [NetworkCallCache::class,NetworkSyncData::class], version = 1)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun getLocalPreferenceDAO(): PersistenceDao

    companion object {
        fun getInstance(context: Context): LocalDataBase? {
            return Room.databaseBuilder(
                context.applicationContext,
                LocalDataBase::class.java,
                "persistance_call.db"
            ).allowMainThreadQueries().build()
        }
    }

}