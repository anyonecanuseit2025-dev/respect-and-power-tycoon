package com.example.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CyanTactical
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PowerPurple

data class RebirthTierInfo(
    val tier: Int,
    val name: String,
    val titleBadge: String,
    val requiredCash: Double,
    val requiredRespect: Long,
    val tokensAwarded: Int,
    val globalMultiplierBonus: Int, // e.g. +50% per tier
    val startingBonusCash: Double,
    val startingBonusRespect: Long,
    val startingBonusPower: Long,
    val description: String,
    val icon: ImageVector,
    val bannerColor: Color
)

object RebirthConfig {
    val rebirthTiers = listOf(
        RebirthTierInfo(
            tier = 1,
            name = "Wonder Sparkle I: Junior Park Leader",
            titleBadge = "Park Leader",
            requiredCash = 1_000_000.0,
            requiredRespect = 25_000L,
            tokensAwarded = 3,
            globalMultiplierBonus = 50,
            startingBonusCash = 15_000.0,
            startingBonusRespect = 2_500L,
            startingBonusPower = 1_000L,
            description = "Expand your wonder empire! Unlock new magical rides, friendly mascots, and huge happiness bonuses.",
            icon = Icons.Default.Shield,
            bannerColor = CyanTactical
        ),
        RebirthTierInfo(
            tier = 2,
            name = "Wonder Sparkle II: Carnival Star Master",
            titleBadge = "Carnival Master",
            requiredCash = 5_000_000.0,
            requiredRespect = 100_000L,
            tokensAwarded = 6,
            globalMultiplierBonus = 100,
            startingBonusCash = 75_000.0,
            startingBonusRespect = 10_000L,
            startingBonusPower = 5_000L,
            description = "Bring joy to every corner of the kingdom! Unlock epic superhero outfits and festival clubs.",
            icon = Icons.Default.WorkspacePremium,
            bannerColor = GoldPrimary
        ),
        RebirthTierInfo(
            tier = 3,
            name = "Wonder Sparkle III: Rainbow Galaxy Explorer",
            titleBadge = "Galaxy Explorer",
            requiredCash = 25_000_000.0,
            requiredRespect = 450_000L,
            tokensAwarded = 10,
            globalMultiplierBonus = 175,
            startingBonusCash = 350_000.0,
            startingBonusRespect = 40_000L,
            startingBonusPower = 20_000L,
            description = "Build cosmic observation towers and hot air balloon fleets across the starry sky.",
            icon = Icons.Default.MilitaryTech,
            bannerColor = PowerPurple
        ),
        RebirthTierInfo(
            tier = 4,
            name = "Wonder Sparkle IV: Fairytale Monarch",
            titleBadge = "Fairytale King",
            requiredCash = 100_000_000.0,
            requiredRespect = 1_800_000L,
            tokensAwarded = 15,
            globalMultiplierBonus = 275,
            startingBonusCash = 1_500_000.0,
            startingBonusRespect = 150_000L,
            startingBonusPower = 80_000L,
            description = "The highest tier of celebration! Host nightly firework celebrations and amusement parades.",
            icon = Icons.Default.AutoAwesome,
            bannerColor = CashGreen
        ),
        RebirthTierInfo(
            tier = 5,
            name = "Wonder Sparkle V: Galactic Joy Deity Prime",
            titleBadge = "Cosmic Star Deity",
            requiredCash = 500_000_000.0,
            requiredRespect = 8_000_000L,
            tokensAwarded = 25,
            globalMultiplierBonus = 400,
            startingBonusCash = 10_000_000.0,
            startingBonusRespect = 800_000L,
            startingBonusPower = 400_000L,
            description = "Your magical theme park becomes legendary across every universe, shining with endless smiles!",
            icon = Icons.Default.Star,
            bannerColor = Color(0xFFFF9E00)
        )
    )

    fun getNextRebirthTier(currentPrestige: Int): RebirthTierInfo {
        return rebirthTiers.find { it.tier == currentPrestige + 1 } ?: RebirthTierInfo(
            tier = currentPrestige + 1,
            name = "Wonder Sparkle ${currentPrestige + 1}: Galaxy Star Prime",
            titleBadge = "Galaxy Prime",
            requiredCash = 1_000_000_000.0 * (currentPrestige - 3),
            requiredRespect = 15_000_000L * (currentPrestige - 3),
            tokensAwarded = 30 + (currentPrestige * 5),
            globalMultiplierBonus = (currentPrestige + 1) * 75,
            startingBonusCash = 25_000_000.0 * currentPrestige,
            startingBonusRespect = 2_000_000L * currentPrestige,
            startingBonusPower = 1_000_000L * currentPrestige,
            description = "Supreme galactic joy ascension tier!",
            icon = Icons.Default.Star,
            bannerColor = GoldPrimary
        )
    }
}

data class RebirthPerk(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val iconColor: Color,
    val maxLevel: Int,
    val tokenCostPerLevel: Int,
    val currentLevel: Int = 0
)

object RebirthPerksCatalog {
    fun createPerkList(
        bankLevel: Int,
        powerLevel: Int,
        respectLevel: Int,
        costReductionLevel: Int,
        heritageCashLevel: Int,
        autoManagerLevel: Int
    ): List<RebirthPerk> = listOf(
        RebirthPerk(
            id = "perk_swiss_vault",
            name = "Rainbow Piggy Bank Boost",
            description = "+15% extra bonus coins earned from all park attractions per level.",
            icon = Icons.Default.AccountBalance,
            iconColor = CashGreen,
            maxLevel = 10,
            tokenCostPerLevel = 1,
            currentLevel = bankLevel
        ),
        RebirthPerk(
            id = "perk_iron_militia",
            name = "Superhero Mascot League",
            description = "+20% hero team power in friendly zone challenges and party duels per level.",
            icon = Icons.Default.Shield,
            iconColor = CyanTactical,
            maxLevel = 10,
            tokenCostPerLevel = 1,
            currentLevel = powerLevel
        ),
        RebirthPerk(
            id = "perk_diplomatic_magnate",
            name = "Golden Smile Star Magnate",
            description = "+20% smile stars gained across all businesses and festivals per level.",
            icon = Icons.Default.Star,
            iconColor = GoldPrimary,
            maxLevel = 10,
            tokenCostPerLevel = 1,
            currentLevel = respectLevel
        ),
        RebirthPerk(
            id = "perk_master_laundering",
            name = "Toy Builder Discount Club",
            description = "Reduces business purchase and ride upgrade costs by 8% per level.",
            icon = Icons.Default.LocalAtm,
            iconColor = PowerPurple,
            maxLevel = 5,
            tokenCostPerLevel = 2,
            currentLevel = costReductionLevel
        ),
        RebirthPerk(
            id = "perk_sovereign_heritage",
            name = "Treasure Chest Starter Fund",
            description = "Start every rebirth with +$50,000 coins, +3,000 smile stars, +1,500 hero power per level.",
            icon = Icons.Default.AutoAwesome,
            iconColor = GoldPrimary,
            maxLevel = 5,
            tokenCostPerLevel = 2,
            currentLevel = heritageCashLevel
        ),
        RebirthPerk(
            id = "perk_autonomous_syndicate",
            name = "Friendly Auto-Managers",
            description = "Automatically recruits lovable ride managers upon starting every new Rebirth.",
            icon = Icons.Default.Bolt,
            iconColor = CyanTactical,
            maxLevel = 1,
            tokenCostPerLevel = 3,
            currentLevel = autoManagerLevel
        )
    )
}
