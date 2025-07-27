package com.example.rocketbrowser.ui.theme

@Composable
fun RocketBrowserTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = Purple40, secondary = Teal40
    )
    MaterialTheme(colorScheme = colors, typography = Typography, content = content)
}
