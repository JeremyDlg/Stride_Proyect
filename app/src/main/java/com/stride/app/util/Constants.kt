package com.stride.app.util

/** Default colors offered when creating a habit. */
object DefaultColors {
    const val BLUE = "#007AFF"
    const val CYAN = "#00C7FF"
    const val GOLD = "#FBBF24"
    const val GREEN = "#30D158"
    val ALL = listOf(BLUE, CYAN, GOLD, GREEN)
}

/** Keys of the built-in icons. The UI maps each key to a drawable. */
object DefaultIcons {
    const val DROP = "drop"
    val ALL = listOf("drop", "activity", "book", "sun", "leaf", "moon")
}

/** Initial map position, San Jose, Costa Rica. */
object MapDefaults {
    const val LATITUDE = 9.9281
    const val LONGITUDE = -84.0907
    const val ZOOM = 14f
}
