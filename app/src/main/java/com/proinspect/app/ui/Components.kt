// Pattern matching function for local OCR
fun parseSerialPlateText(text: String, equipmentName: String): String {
    val lines = text.lines().map { it.trim() }
    
    var manufacturer = ""
    var model = ""
    var serial = ""
    var year = ""
    var capacity = ""
    
    // Common manufacturer patterns
    val mfgPatterns = listOf(
        "rheem", "ruud", "carrier", "trane", "lennox", "goodman", "amana", 
        "york", "american standard", "bryant", "payne", "bradford white",
        "a.o. smith", "ao smith", "state", "whirlpool", "ge", "frigidaire",
        "mitsubishi", "daikin", "fujitsu", "lg", "samsung", "coleman", "heil"
    )
    
    for (line in lines) {
        val lower = line.lowercase()
        
        // Find manufacturer
        if (manufacturer.isEmpty()) {
            for (mfg in mfgPatterns) {
                if (lower.contains(mfg)) {
                    manufacturer = mfg.split(" ").joinToString(" ") { 
                        it.replaceFirstChar { c -> c.uppercase() } 
                    }
                    break
                }
            }
        }
        
        // Find model (usually starts with MODEL, MOD, or M/N)
        if (model.isEmpty() && (lower.contains("model") || lower.contains("mod") || lower.contains("m/n") || lower.contains("m.n"))) {
            model = line.replace(Regex("(?i)(model|mod|m/n|m\\.n)[:\\s]*"), "").trim()
        }
        
        // Find serial (usually starts with SERIAL, SER, or S/N)
        if (serial.isEmpty() && (lower.contains("serial") || lower.contains("ser") || lower.contains("s/n") || lower.contains("s.n"))) {
            serial = line.replace(Regex("(?i)(serial|ser|s/n|s\\.n)[:\\s]*"), "").trim()
        }
        
        // Find year - multiple strategies
        if (year.isEmpty()) {
            // Strategy 1: Look for explicit 4-digit year
            val yearMatch = Regex("\\b(19|20)\\d{2}\\b").find(line)
            if (yearMatch != null) {
                year = yearMatch.value
            } 
            // Strategy 2: Look for MFG DATE or DATE fields
            else if (lower.contains("mfg") || lower.contains("date") || lower.contains("manufactured")) {
                val dateText = line.replace(Regex("(?i)(mfg|date|manufactured)[:\\s]*"), "").trim()
                // Try to extract year from date formats like "05/2015" or "2015-05"
                val yearInDate = Regex("(19|20)\\d{2}").find(dateText)
                if (yearInDate != null) {
                    year = yearInDate.value
                } else {
                    year = dateText
                }
            }
            // Strategy 3: Look for "YEAR" field
            else if (lower.contains("year")) {
                year = line.replace(Regex("(?i)year[:\\s]*"), "").trim()
            }
        }
        
        // Find capacity (BTU, gallons, tons)
        if (capacity.isEmpty()) {
            val btuMatch = Regex("\\d+,?\\d*\\s*(btu|btuh)", RegexOption.IGNORE_CASE).find(line)
            val galMatch = Regex("\\d+\\s*(gal|gallon)", RegexOption.IGNORE_CASE).find(line)
            val tonMatch = Regex("\\d+\\.?\\d*\\s*ton", RegexOption.IGNORE_CASE).find(line)
            
            capacity = btuMatch?.value ?: galMatch?.value ?: tonMatch?.value ?: ""
        }
    }
    
    // Strategy 4: Try to decode year from serial number if still empty
    if (year.isEmpty() && serial.isNotEmpty()) {
        year = decodeYearFromSerial(serial, manufacturer)
    }
    
    // Only return if we found at least 3 fields
    val foundCount = listOf(manufacturer, model, serial, year, capacity).count { it.isNotEmpty() }
    
    if (foundCount < 3) {
        return "" // Not enough data, will trigger API fallback
    }
    
    return buildString {
        appendLine("Manufacturer: $manufacturer")
        appendLine("Model: $model")
        appendLine("Serial: $serial")
        appendLine("Year/Age: $year")
        appendLine("Capacity: $capacity")
    }.trim()
}

// Helper function to decode year from serial number based on manufacturer
fun decodeYearFromSerial(serial: String, manufacturer: String): String {
    if (serial.length < 4) return ""
    
    return when (manufacturer.lowercase()) {
        "rheem", "ruud" -> {
            // Rheem/Ruud: 3rd & 4th digits are month, 5th & 6th are year
            // Example: M051234567 = May 2012
            if (serial.length >= 6) {
                val yearDigits = serial.substring(4, 6)
                val year = yearDigits.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year > 50) 1900 + year else 2000 + year
                    fullYear.toString()
                } else ""
            } else ""
        }
        "carrier", "bryant", "payne" -> {
            // Carrier/Bryant: 4th digit is year (0-9 for 2010-2019, then continues)
            // Example: 1234A56789 where 4 = 2014
            if (serial.length >= 4) {
                val yearDigit = serial[3].toString().toIntOrNull()
                if (yearDigit != null) {
                    val year = 2010 + yearDigit
                    if (year > 2025) (year - 10).toString() else year.toString()
                } else ""
            } else ""
        }
        "trane", "american standard" -> {
            // Trane: 3rd & 4th characters are year
            if (serial.length >= 4) {
                val yearChars = serial.substring(2, 4)
                val year = yearChars.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year > 50) 1900 + year else 2000 + year
                    fullYear.toString()
                } else ""
            } else ""
        }
        "lennox" -> {
            // Lennox: 2nd & 3rd digits are year
            if (serial.length >= 3) {
                val yearDigits = serial.substring(1, 3)
                val year = yearDigits.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year > 50) 1900 + year else 2000 + year
                    fullYear.toString()
                } else ""
            } else ""
        }
        "goodman", "amana" -> {
            // Goodman/Amana: 3rd & 4th digits are year
            if (serial.length >= 4) {
                val yearDigits = serial.substring(2, 4)
                val year = yearDigits.toIntOrNull()
                if (year != null) {
                    val fullYear = if (year > 50) 1900 + year else 2000 + year
                    fullYear.toString()
                } else ""
            } else ""
        }
        "york" -> {
            // York: 1st letter is year (A=2004, B=2005, etc.)
            if (serial.isNotEmpty() && serial[0].isLetter()) {
                val letter = serial[0].uppercaseChar()
                val year = 2004 + (letter - 'A')
                if (year in 2004..2030) year.toString() else ""
            } else ""
        }
        "bradford white", "a.o. smith", "ao smith", "state" -> {
            // Water heaters often have year in first 4 digits or embedded
            val yearMatch = Regex("(19|20)\\d{2}").find(serial)
            yearMatch?.value ?: ""
        }
        else -> {
            // Generic: try to find any 4-digit year in the serial
            val yearMatch = Regex("(19|20)\\d{2}").find(serial)
            yearMatch?.value ?: ""
        }
    }
}
