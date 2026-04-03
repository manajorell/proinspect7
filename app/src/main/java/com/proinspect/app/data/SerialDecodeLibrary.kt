package com.proinspect.app.data

object SerialDecodeLibrary {
    
    fun getPatterns(): List<SerialDecodePattern> = listOf(
        // Rheem / Ruud - Positions 5-6 are year (YY format)
        SerialDecodePattern(
            manufacturer = "Rheem",
            pattern = "^.{4}(\\d{2})(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 2,
            priority = 10
        ),
        SerialDecodePattern(
            manufacturer = "Ruud",
            pattern = "^.{4}(\\d{2})(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 2,
            priority = 10
        ),
        
        // Carrier - 4th character is year digit (0-9 for 2010-2019, then repeats)
        SerialDecodePattern(
            manufacturer = "Carrier",
            pattern = "^.{3}(\\d).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        SerialDecodePattern(
            manufacturer = "Bryant",
            pattern = "^.{3}(\\d).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        SerialDecodePattern(
            manufacturer = "Payne",
            pattern = "^.{3}(\\d).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        
        // Trane / American Standard - Positions 3-4 are year
        SerialDecodePattern(
            manufacturer = "Trane",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        SerialDecodePattern(
            manufacturer = "American Standard",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        
        // Lennox - Position 2-3 are year
        SerialDecodePattern(
            manufacturer = "Lennox",
            pattern = "^.(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        
        // Goodman / Amana - Positions 3-4 are year
        SerialDecodePattern(
            manufacturer = "Goodman",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        SerialDecodePattern(
            manufacturer = "Amana",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 9
        ),
        
        // York - First letter indicates year (A=2004, B=2005, etc.)
        SerialDecodePattern(
            manufacturer = "York",
            pattern = "^([A-Z]).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 8
        ),
        
        // Bradford White - Year in serial (YYWW format at various positions)
        SerialDecodePattern(
            manufacturer = "Bradford White",
            pattern = ".*(\\d{2})(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 2,
            priority = 7
        ),
        
        // A.O. Smith - Year often embedded in serial
        SerialDecodePattern(
            manufacturer = "A.O. Smith",
            pattern = ".*(\\d{4}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 7
        ),
        SerialDecodePattern(
            manufacturer = "AO Smith",
            pattern = ".*(\\d{4}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 7
        ),
        
        // State Water Heaters
        SerialDecodePattern(
            manufacturer = "State",
            pattern = ".*(\\d{4}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 7
        ),
        
        // Coleman
        SerialDecodePattern(
            manufacturer = "Coleman",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 8
        ),
        
        // Heil
        SerialDecodePattern(
            manufacturer = "Heil",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 8
        ),
        
        // Mitsubishi
        SerialDecodePattern(
            manufacturer = "Mitsubishi",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 8
        ),
        
        // Daikin
        SerialDecodePattern(
            manufacturer = "Daikin",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 8
        ),
        
        // Fujitsu
        SerialDecodePattern(
            manufacturer = "Fujitsu",
            pattern = "^.{2}(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 8
        ),
        
        // Generic fallback - look for 4-digit year anywhere
        SerialDecodePattern(
            manufacturer = "Generic",
            pattern = ".*(19|20)(\\d{2}).*",
            yearGroup = 1,
            monthGroup = 0,
            priority = 1
        )
    )
    
    /**
     * Decode a serial number using manufacturer-specific patterns
     */
    fun decodeSerial(serial: String, manufacturer: String): String? {
        val cleanSerial = serial.replace(Regex("[\\s-]"), "").uppercase()
        val patterns = getPatterns()
            .filter { it.manufacturer.equals(manufacturer, ignoreCase = true) || it.manufacturer == "Generic" }
            .sortedByDescending { it.priority }
        
        for (pattern in patterns) {
            val regex = Regex(pattern.pattern)
            val match = regex.find(cleanSerial) ?: continue
            
            if (match.groupValues.size <= pattern.yearGroup) continue
            
            val yearStr = match.groupValues[pattern.yearGroup]
            val year = when {
                // York uses letters (A=2004, B=2005, etc.)
                manufacturer.equals("York", ignoreCase = true) && yearStr.length == 1 && yearStr[0].isLetter() -> {
                    2004 + (yearStr[0] - 'A')
                }
                // 4-digit year
                yearStr.length == 4 -> yearStr.toIntOrNull()
                // 2-digit year
                yearStr.length == 2 -> {
                    val twoDigit = yearStr.toIntOrNull() ?: continue
                    if (twoDigit <= 50) 2000 + twoDigit else 1900 + twoDigit
                }
                // Single digit year (Carrier style - decade indicator)
                yearStr.length == 1 -> {
                    val digit = yearStr.toIntOrNull() ?: continue
                    2010 + digit // Assumes 2010s, adjust as needed
                }
                else -> null
            }
            
            if (year != null && year in 1980..2030) {
                val monthStr = if (pattern.monthGroup > 0 && match.groupValues.size > pattern.monthGroup) {
                    match.groupValues[pattern.monthGroup]
                } else null
                
                return if (monthStr != null) {
                    "Year: $year, Month/Week: $monthStr"
                } else {
                    "Year: $year"
                }
            }
        }
        
        return null
    }
}
