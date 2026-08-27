package com.example.data.playgames

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.vector.ImageVector

data class PlayGamesAchievement(
    val id: String,
    val title: String,
    val description: String,
    val xpValue: Int,
    val icon: ImageVector,
    val isUnlocked: Boolean = false,
    val unlockedAtTimestamp: Long? = null
)

data class PlayGamesLeaderboardInfo(
    val id: String,
    val name: String,
    val description: String,
    val unit: String,
    val playerScore: Long,
    val playerFormattedScore: String,
    val topRank: Int
)

data class PlayGamesState(
    val isSignedIn: Boolean = true,
    val gamerTag: String = "SovereignBoss#7729",
    val playGamesLevel: Int = 14,
    val totalXp: Int = 12500,
    val isCloudSaveSynced: Boolean = true,
    val lastCloudSyncTime: Long = System.currentTimeMillis(),
    val achievements: List<PlayGamesAchievement> = defaultAchievements(),
    val leaderboards: List<PlayGamesLeaderboardInfo> = emptyList(),
    val recentUnlockedAchievement: PlayGamesAchievement? = null
)

fun defaultAchievements(): List<PlayGamesAchievement> = listOf(
    PlayGamesAchievement(
        id = "ach_first_hustle",
        title = "First Street Hustle",
        description = "Accumulate your first $1,000 dirty cash.",
        xpValue = 500,
        icon = Icons.Default.AccountBalance
    ),
    PlayGamesAchievement(
        id = "ach_syndicate_founder",
        title = "Syndicate Founder",
        description = "Acquire and level up 3 front businesses.",
        xpValue = 1000,
        icon = Icons.Default.LocationCity
    ),
    PlayGamesAchievement(
        id = "ach_turf_conqueror",
        title = "Turf Conqueror",
        description = "Raid and conquer a rival syndicate district.",
        xpValue = 1500,
        icon = Icons.Default.MilitaryTech
    ),
    PlayGamesAchievement(
        id = "ach_high_roller",
        title = "High Roller Penthouse",
        description = "Purchase the Sovereign Grand Casino or Velvet Noir Club.",
        xpValue = 2000,
        icon = Icons.Default.Casino
    ),
    PlayGamesAchievement(
        id = "ach_diplomat_alliance",
        title = "Underworld Coalition",
        description = "Forge an alliance pact with Silverline or Ironclad.",
        xpValue = 2000,
        icon = Icons.Default.Handshake
    ),
    PlayGamesAchievement(
        id = "ach_duel_gladiator",
        title = "Duel Arena Champion",
        description = "Win a tactical 2-Player Pass-and-Play sector card match.",
        xpValue = 2500,
        icon = Icons.Default.SportsKabaddi
    ),
    PlayGamesAchievement(
        id = "ach_costume_icon",
        title = "Fashion Godfather",
        description = "Unlock and equip a custom Boss Outfit from the Wardrobe.",
        xpValue = 2000,
        icon = Icons.Default.Shield
    ),
    PlayGamesAchievement(
        id = "ach_first_rebirth",
        title = "Ascended Sovereign",
        description = "Perform your first Syndicate Rebirth and earn Rebirth Tokens.",
        xpValue = 4000,
        icon = Icons.Default.AutoAwesome
    ),
    PlayGamesAchievement(
        id = "ach_billionaire_oligarch",
        title = "Billionaire Oligarch",
        description = "Amass an all-time net worth exceeding $100,000,000.",
        xpValue = 5000,
        icon = Icons.Default.WorkspacePremium
    )
)
