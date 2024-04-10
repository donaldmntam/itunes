package com.example.itunes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.itunes.R

val fontFamily = FontFamily(
        Font(R.font.light, FontWeight.Light, FontStyle.Normal),
        Font(R.font.light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.medium, FontWeight.Medium, FontStyle.Normal),
        Font(R.font.medium_italic, FontWeight.Medium, FontStyle.Italic),
        Font(R.font.semibold, FontWeight.SemiBold, FontStyle.Normal),
        Font(R.font.semibold_italic, FontWeight.SemiBold, FontStyle.Italic),
        Font(R.font.bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.bold, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.black, FontWeight.Black, FontStyle.Normal),
        Font(R.font.black_italic, FontWeight.Black, FontStyle.Italic),
)

// Set of Material typography styles to start with
val Typography = Typography(
        bodyLarge = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
        )
        /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)