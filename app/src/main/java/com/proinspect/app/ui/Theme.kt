package com.proinspect.app.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Navy       = Color(0xFF1A2744)
val NavyLight  = Color(0xFF243358)
val Gold       = Color(0xFFC9973A)
val GoldLight  = Color(0xFFE8B85A)
val Cream      = Color(0xFFF8F5EF)
val RatingRed    = Color(0xFFDC2626)
val RatingOrange = Color(0xFFEA580C)
val RatingYellow = Color(0xFFCA8A04)
val RatingGreen  = Color(0xFF16A34A)
val RatingGray   = Color(0xFF6B7280)

private val ColorScheme = lightColorScheme(
    primary        = Navy,
    onPrimary      = Color.White,
    secondary      = Gold,
    onSecondary    = Navy,
    background     = Cream,
    surface        = Color.White,
    onBackground   = Color(0xFF1F2937),
    onSurface      = Color(0xFF1F2937),
    surfaceVariant = Color(0xFFF3F4F6),
    outline        = Color(0xFFE5E7EB)
)

@Composable
fun ProInspectTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColorScheme, content = content)
}

fun ratingColor(rating: com.proinspect.app.data.Rating) = when (rating) {
    com.proinspect.app.data.Rating.SAFETY    -> RatingRed
    com.proinspect.app.data.Rating.MAJOR     -> RatingOrange
    com.proinspect.app.data.Rating.MONITOR   -> RatingYellow
    com.proinspect.app.data.Rating.GOOD      -> RatingGreen
    com.proinspect.app.data.Rating.NOT_RATED -> RatingGray
}
