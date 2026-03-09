package com.proinspect.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "serial_decode_patterns")
data class SerialDecodePattern(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val manufacturer: String,              // "Rheem", "Carrier", etc.
    val pattern: String,                   // "SUBSTRING:4-6" or "REGEX:..." or "POSITION:3"
    val yearCalculation: String,           // "SMART_CENTURY" or "2010+DIGIT_CYCLE" or "2004+LETTER"
    val description: String = "",          // Human-readable explanation
    val priority: Int = 0                  // Higher priority = try first
)
