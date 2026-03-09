package com.proinspect.app.data

object HVACDecoder {
    
    data class DecodedInfo(
        val manufacturer: String,
        val year: Int?,
        val model: String?,
        val confidence: String // "High", "Medium", "Low"
    )
    
    fun decode(serialNumber: String): DecodedInfo? {
        val serial = serialNumber.trim().uppercase()
        
        // Carrier / Bryant / Payne
        if (serial.length >= 10 && serial.matches(Regex("^[0-9]{4}[A-Z]{1}.*"))) {
            val year = decodeCarrierYear(serial.substring(3, 4))
            return DecodedInfo("Carrier/Bryant", year, null, "High")
        }
        
        // Trane / American Standard
        if (serial.matches(Regex("^[0-9]{9}.*"))) {
            val year = decodeTraneYear(serial.substring(3, 4))
            return DecodedInfo("Trane/American Standard", year, null, "High")
        }
        
        // Lennox
        if (serial.length >= 10 && serial.matches(Regex("^[0-9]{4}[A-Z].*"))) {
            val year = decodeLennoxYear(serial.substring(4, 5))
            return DecodedInfo("Lennox", year, null, "Medium")
        }
        
        // Rheem / Ruud
        if (serial.matches(Regex("^[A-Z]{4}[0-9]{8}"))) {
            val yearCode = serial.substring(1, 2)
            val year = decodeRheemYear(yearCode)
            return DecodedInfo("Rheem/Ruud", year, null, "High")
        }
        
        // Goodman / Amana
        if (serial.length >= 9 && serial.matches(Regex("^[0-9]{2}[0-9]{2}.*"))) {
            val yearStr = serial.substring(0, 2)
            val year = yearStr.toIntOrNull()?.let { 2000 + it }
            return DecodedInfo("Goodman/Amana", year, null, "High")
        }
        
        // York / Luxaire / Coleman
        if (serial.matches(Regex("^[A-Z]{1}[0-9]{3}.*"))) {
            val year = decodeYorkYear(serial.substring(0, 1))
            return DecodedInfo("York", year, null, "Medium")
        }
        
        return null
    }
    
    private fun decodeCarrierYear(code: String): Int? {
        val yearMap = mapOf(
            "A" to 2010, "B" to 2011, "C" to 2012, "D" to 2013, "E" to 2014,
            "F" to 2015, "G" to 2016, "H" to 2017, "J" to 2018, "K" to 2019,
            "L" to 2020, "M" to 2021, "N" to 2022, "P" to 2023, "R" to 2024,
            "S" to 2025, "T" to 2026, "U" to 2027, "V" to 2028, "W" to 2029,
            "X" to 2030, "Y" to 2031
        )
        return yearMap[code]
    }
    
    private fun decodeTraneYear(code: String): Int? {
        val digit = code.toIntOrNull() ?: return null
        return when {
            digit in 0..9 -> 2010 + digit
            else -> null
        }
    }
    
    private fun decodeLennoxYear(code: String): Int? {
        val yearMap = mapOf(
            "A" to 2010, "B" to 2011, "C" to 2012, "D" to 2013, "E" to 2014,
            "F" to 2015, "G" to 2016, "H" to 2017, "J" to 2018, "K" to 2019,
            "L" to 2020, "M" to 2021, "N" to 2022, "P" to 2023, "R" to 2024
        )
        return yearMap[code]
    }
    
    private fun decodeRheemYear(code: String): Int? {
        val yearMap = mapOf(
            "A" to 2010, "B" to 2011, "C" to 2012, "D" to 2013, "E" to 2014,
            "F" to 2015, "G" to 2016, "H" to 2017, "J" to 2018, "K" to 2019,
            "L" to 2020, "M" to 2021, "N" to 2022, "P" to 2023, "R" to 2024
        )
        return yearMap[code]
    }
    
    private fun decodeYorkYear(code: String): Int? {
        val yearMap = mapOf(
            "A" to 2010, "B" to 2011, "C" to 2012, "D" to 2013, "E" to 2014,
            "F" to 2015, "G" to 2016, "H" to 2017, "J" to 2018, "K" to 2019,
            "L" to 2020, "M" to 2021, "N" to 2022, "P" to 2023, "R" to 2024
        )
        return yearMap[code]
    }
}
