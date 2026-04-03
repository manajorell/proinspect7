package com.proinspect.app.data

enum class Rating(val short: String) {
    SAFETY("Safety"),
    MAJOR("Major"),
    MONITOR("Monitor"),
    GOOD("Good"),
    NOT_RATED("N/R"),
    NOT_PRESENT("N/A")
}
