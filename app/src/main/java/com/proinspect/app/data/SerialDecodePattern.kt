package com.proinspect.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serial_decode_patterns")
data class SerialDecodePattern(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val manufacturer: String,
    val pattern: String,
    val yearGroup: Int,
    val monthGroup: Int,
    val priority: Int = 0
)
