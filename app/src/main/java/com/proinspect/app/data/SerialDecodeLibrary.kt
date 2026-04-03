package com.proinspect.app.data

object SerialDecodeLibrary {
    fun getDefaultPatterns(): List<SerialDecodePattern> {
        return listOf(
            // ========== RHEEM / RUUD ==========
            SerialDecodePattern(
                manufacturer = "Rheem",
                pattern = "SUBSTRING:4-6",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 5-6 are year",
                priority = 10
            ),
            
SerialDecodePattern(
    manufacturer = "Carrier",
    pattern = "^(\\d{2})(\\d{2}).*",
    yearGroup = 1,
    monthGroup = 2,
    priority = 1
)

            
            // ========== CARRIER / BRYANT / PAYNE ==========
            SerialDecodePattern(
                manufacturer = "Carrier",
                pattern = "POSITION:3",
                yearCalculation = "2010+DIGIT_CYCLE",
                description = "4th char is year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Bryant",
                pattern = "POSITION:3",
                yearCalculation = "2010+DIGIT_CYCLE",
                description = "4th char is year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Payne",
                pattern = "POSITION:3",
                yearCalculation = "2010+DIGIT_CYCLE",
                description = "4th char is year",
                priority = 10
            ),
            
            // ========== TRANE / AMERICAN STANDARD ==========
            SerialDecodePattern(
                manufacturer = "Trane",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "American Standard",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            
            // ========== LENNOX ==========
            SerialDecodePattern(
                manufacturer = "Lennox",
                pattern = "SUBSTRING:1-3",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 2-3 are year",
                priority = 10
            ),
            
            // ========== GOODMAN / AMANA ==========
            SerialDecodePattern(
                manufacturer = "Goodman",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Amana",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            
            // ========== YORK ==========
            SerialDecodePattern(
                manufacturer = "York",
                pattern = "POSITION:0",
                yearCalculation = "2004+LETTER",
                description = "1st letter (A=2004, B=2005...)",
                priority = 10
            ),
            
            // ========== COLEMAN / HEIL ==========
            SerialDecodePattern(
                manufacturer = "Coleman",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Heil",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            
            // ========== WATER HEATERS ==========
            SerialDecodePattern(
                manufacturer = "Bradford White",
                pattern = "REGEX:(19|20)\\d{2}",
                yearCalculation = "DIRECT",
                description = "4-digit year in serial",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "A.O. Smith",
                pattern = "REGEX:(19|20)\\d{2}",
                yearCalculation = "DIRECT",
                description = "4-digit year in serial",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "State",
                pattern = "REGEX:(19|20)\\d{2}",
                yearCalculation = "DIRECT",
                description = "4-digit year in serial",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Whirlpool",
                pattern = "REGEX:(19|20)\\d{2}",
                yearCalculation = "DIRECT",
                description = "4-digit year in serial",
                priority = 10
            ),
            
            // ========== ASIAN MANUFACTURERS ==========
            SerialDecodePattern(
                manufacturer = "Mitsubishi",
                pattern = "SUBSTRING:4-6",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 5-6 are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Daikin",
                pattern = "SUBSTRING:0-2",
                yearCalculation = "SMART_CENTURY",
                description = "First 2 chars are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Fujitsu",
                pattern = "SUBSTRING:0-2",
                yearCalculation = "SMART_CENTURY",
                description = "First 2 chars are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "LG",
                pattern = "POSITION:0",
                yearCalculation = "2010+DIGIT",
                description = "1st digit (0=2010, 1=2011...)",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Samsung",
                pattern = "POSITION:0",
                yearCalculation = "2010+DIGIT",
                description = "1st digit (0=2010, 1=2011...)",
                priority = 10
            ),
            
            // ========== OTHER BRANDS ==========
            SerialDecodePattern(
                manufacturer = "GE",
                pattern = "SUBSTRING:2-4",
                yearCalculation = "SMART_CENTURY",
                description = "Chars 3-4 are year",
                priority = 10
            ),
            SerialDecodePattern(
                manufacturer = "Frigidaire",
                pattern = "SUBSTRING:0-2",
                yearCalculation = "SMART_CENTURY",
                description = "First 2 chars are year",
                priority = 10
            ),
            
            // ========== GENERIC FALLBACK ==========
            SerialDecodePattern(
                manufacturer = "GENERIC",
                pattern = "REGEX:(19|20)\\d{2}",
                yearCalculation = "DIRECT",
                description = "Generic: find 4-digit year",
                priority = 1
            )
        )
    }
}
