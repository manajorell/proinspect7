package com.proinspect.app.data

enum class Rating {
    SAFETY,      // Red - Safety issues
    MAJOR,       // Orange - Major defects
    MONITOR,     // Yellow - Monitor/minor issues
    GOOD,        // Green - Good condition
    NOT_RATED,   // Gray - Not yet rated
    NOT_PRESENT  // Gray - Item not present
}
