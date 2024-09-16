package com.example.secgamecallenge_wordsearch

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

// Test player instance for demonstration purposes (Todo: Generate based on the employee card tap)
val testPlayer = Player("MatNif", "mathias.niffeler@sec.ethz.ch", 0, 0)

// List of prizes that can be awarded to top players (Todo: Modify depending on the prizes that are available)
val listOfPrizes = listOf("a gift card", "a t-shirt", "a mug", "a keychain")

// Map of words for each day of the month (Todo: Can be updated monthly with new words generated by https://chatgpt.com/g/g-8ZGfMYmgb-sec-game-challenge-word-search)
val wordsForTheMonth = mapOf(
    1 to listOf("CITIES", "ENERGY", "HEALTH", "RESILIENT", "SYSTEMS"),
    2 to listOf("MANGROVE", "CARBON", "STOCKS", "GLOBAL", "IMPACT"),
    3 to listOf("URBAN", "HEAT", "ISLAND", "COOLING", "SINGAPORE"),
    4 to listOf("DIGITAL", "TWIN", "UNDERGROUND", "MAPPING", "UTILITIES"),
    5 to listOf("RESILIENCE", "FUTURE", "SYSTEMS", "MARKET", "BLACKOUT"),
    6 to listOf("MICROGRID", "COMMUNITY", "ELECTRIC", "SUPPLY", "SURVEY"),
    7 to listOf("SUSTAIN", "NATURE", "CITIES", "GREENERY", "CLIMATE"),
    8 to listOf("DATA", "MODELS", "CLIMATE", "DUCT", "SIMULATE"),
    9 to listOf("BLACK", "FLY", "RESEARCH", "INSECTS", "SOLDIER"),
    10 to listOf("CLEAN", "ENERGY", "VEHICLE", "ALTERNATIVE", "FUEL"),
    11 to listOf("SMART", "NATION", "CYCLE", "URBAN", "WALKING"),
    12 to listOf("COOL", "SINGAPORE", "PROJECT", "HEAT", "MITIGATE"),
    13 to listOf("RESILIENT", "CONFERENCE", "INTERNATIONAL", "RESEARCH", "SYSTEMS"),
    14 to listOf("ECO", "FRIENDLY", "FOOD", "SUSTAIN", "PLANET"),
    15 to listOf("MOU", "CAPABILITY", "KNOWLEDGE", "CITIES", "URBAN"),
    16 to listOf("AIR", "VEGETATION", "COOL", "CLIMATE", "GREEN"),
    17 to listOf("LIFESTYLE", "HEALTH", "PREVENT", "COACHING", "HOLISTIC"),
    18 to listOf("MAPPING", "DIGITAL", "UNDERGROUND", "3D", "TWIN"),
    19 to listOf("AGENCIES", "TRAINING", "PROGRAM", "WORKSHOP", "CLC"),
    20 to listOf("FOOD", "MICROALGAE", "STUDY", "DIET", "SUSTAIN"),
    21 to listOf("THERMAL", "COMFORT", "HEAT", "URBAN", "IMPACT"),
    22 to listOf("NUTRITION", "SECURITY", "PROTEIN", "ALGAE", "FOOD"),
    23 to listOf("SUPPLY", "GRID", "BLACKOUT", "MARKET", "STUDY"),
    24 to listOf("SOLAR", "ENERGY", "RENEWABLE", "GRID", "MANAGEMENT"),
    25 to listOf("COOLING", "STRATEGY", "URBAN", "TWIN", "CLIMATE"),
    26 to listOf("VEHICLE", "ELECTRIC", "GREEN", "FUTURE", "ENERGY"),
    27 to listOf("RESILIENCE", "STUDY", "HEALTH", "POLICY", "DATA"),
    28 to listOf("SUBSURFACE", "UTILITIES", "DIGITAL", "TWIN", "MAP"),
    29 to listOf("RESILIENT", "SYSTEMS", "RESEARCH", "CONFERENCE", "GLOBAL"),
    30 to listOf("GREEN", "URBAN", "HEAT", "IMPACT", "COOL"),
    31 to listOf("SOCIAL", "MEDIA", "SENTIMENT", "ANALYSIS", "AI")
)

// Constants for email server configuration (Todo: Update with the game master's email server details)
const val hostServer = "smtp.office365.com"
const val hostPort = "587"
const val gameMasterEmail = "your_email@example.com"
const val gameMasterPassword = "your_password"

// Define time limit for the game in seconds
const val gameDuration = 120

// Constants for the game grid layout
val gridOrigin = Offset(0f, 0f)
val cellDimensions = 70.dp

