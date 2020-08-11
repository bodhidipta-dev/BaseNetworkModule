package com.bodhi.network.networklayer.local

import androidx.room.TypeConverter
import java.util.*

internal class TimestampConverter {
    companion object {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return if (value == null) null else Date(value)
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time
        }
    }
}