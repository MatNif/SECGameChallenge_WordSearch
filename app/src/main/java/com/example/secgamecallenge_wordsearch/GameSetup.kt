package com.example.secgamecallenge_wordsearch

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

// Test player instance for demonstration purposes (Todo: Generate based on the employee card tap)
val testPlayer = Player("Tze Jan Sim", "mathias.niffeler@sec.ethz.ch", 0, 0)

// List of prizes that can be awarded to top players (Todo: Modify depending on the prizes that are available)
val listOfPrizes = listOf("a gift card", "a t-shirt", "a mug", "a keychain")

// Map of words for each day of the month (Todo: Can be updated monthly with new words generated by https://chatgpt.com/g/g-8ZGfMYmgb-sec-game-challenge-word-search)
val wordsForTheMonth = mapOf(
    1 to listOf("HEALTH", "CITIES", "RESILIENT", "ENERGY", "SYSTEMS"), // SEC research programs: Future Health Technologies, Future Cities, Future Resilient Systems
    2 to listOf("ALGAE", "FOOD", "SECURE", "URBAN", "SUSTAIN"), // Urban Microalgae-Based Protein Production for food security
    3 to listOf("DIGITAL", "TWIN", "MODEL", "COOL", "URBAN"), // Digital Urban Climate Twin project for cooling Singapore
    4 to listOf("MOBILE", "HEALTH", "TRACK", "LIFE", "DISEASE"), // Future Health Technologies program using mobile apps to monitor health
    5 to listOf("CLIMATE", "HEAT", "STRESS", "LOSSES", "IMPACT"), // Study on the economic impact of heat stress in Singapore
    6 to listOf("FLY", "BLACK", "INSECTS", "FEED", "WORLD"), // Black Soldier Fly project for sustainable food systems
    7 to listOf("SOIL", "MAP", "DATA", "SUBSURFACE", "LAND"), // Digital Underground project mapping subsurface utilities
    8 to listOf("CITY", "PATHS", "FUTURE", "DESIGN", "BIKE"), // Improving urban design with bicycle paths in Singapore
    9 to listOf("REHAB", "HANDYBOT", "WRIST", "ROBOT", "THERAPY"), // ReHandyBot project for wrist rehabilitation
    10 to listOf("GREEN", "SPACE", "TREES", "CITY", "PLAN"), // Greening urban spaces to mitigate heat in Singapore
    11 to listOf("SMART", "GRID", "POWER", "COMMUNE", "QUOTA"), // Study on smart grids and community energy sharing
    12 to listOf("PLASTIC", "WASTE", "RECYCLE", "CIRCULAR", "GREEN"), // Research on plastic waste recycling and circular economy
    13 to listOf("AGING", "CITIES", "FUTURE", "RESILIENCE", "HEALTH"), // Future Cities Laboratory research on aging populations in cities
    14 to listOf("TECH", "COACH", "MUMS", "WELLNESS", "DIABETES"), // LvL UP wellness app targeting diabetes prevention in mothers
    15 to listOf("COOL", "SING", "HEAT", "DUCT", "MODEL"), // Cooling Singapore's work on urban heat management
    16 to listOf("PRINT", "FOOD", "SEAFOOD", "MUNG", "PROTEIN"), // 3D printed seafood using microalgae and mung bean protein
    17 to listOf("RESILIENT", "INFRA", "SYSTEM", "SHOCK", "DESIGN"), // Future Resilient Systems project on infrastructure resilience
    18 to listOf("RISK", "FIRE", "CYBER", "NETWORK", "SAFETY"), // Future Internet Resilience Economics (FIRE) project
    19 to listOf("BIKE", "LANES", "WALK", "SAFE", "CITY"), // Expanding pedestrian and bicycle networks in urban spaces
    20 to listOf("HACK", "CODE", "SMART", "CYBER", "SAFETY"), // Cybersecurity in smart cities research at SEC
    21 to listOf("AIR", "POLLUTE", "FILTER", "CLEAN", "CITY"), // Air pollution mitigation efforts in Singapore
    22 to listOf("VIRUS", "SPREAD", "TRACE", "TRACK", "DATA"), // Digital tools for tracking virus spread in urban environments
    23 to listOf("YOUTH", "CITIES", "LEAD", "PROGRAM", "TECH"), // Leadership programs for youth in smart city development
    24 to listOf("SEEDS", "PLANTS", "CLIMATE", "GROW", "GREEN"), // Research on climate-resilient urban agriculture
    25 to listOf("BUILD", "SMART", "COOL", "HEAT", "TECH"), // Building design technologies to reduce urban heat
    26 to listOf("FLOOD", "WATER", "COAST", "RISKS", "RESILIENT"), // Coastal resilience and flood risk management projects
    27 to listOf("FARM", "VERTICAL", "FOOD", "URBAN", "GREEN"), // Urban vertical farming projects in Singapore
    28 to listOf("SENSOR", "HEALTH", "WEAR", "MONITOR", "DATA"), // Wearable health monitoring devices
    29 to listOf("CITY", "MOVE", "TRANSIT", "GREEN", "SMART"), // Smart transit systems in urban settings
    30 to listOf("WASTE", "RECYCLE", "GREEN", "FUTURE", "PLAN"), // Sustainable waste management projects
    31 to listOf("AIR", "QUALITY", "MONITOR", "CITY", "LUNG") // Air quality monitoring systems in smart cities
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

