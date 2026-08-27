package com.example.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CyanTactical
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PowerPurple

enum class CostumeRarity(val label: String, val color: Color, val borderGlow: Color) {
    COMMON("Common", Color(0xFF9E9E9E), Color(0xFF616161)),
    RARE("Rare", CyanTactical, Color(0xFF00ACC1)),
    EPIC("Epic", PowerPurple, Color(0xFFAB47BC)),
    LEGENDARY("Legendary", GoldPrimary, GoldDark),
    MYTHIC("Mythic", Color(0xFFFF3D00), Color(0xFFFF6E40))
}

enum class CostumeUnlockType {
    DEFAULT,
    CASH,
    RESPECT,
    PRESTIGE_TIER,
    PRESTIGE_TOKENS,
    REPUTATION,
    DUEL_WINS
}

data class BossCostume(
    val id: String,
    val name: String,
    val title: String,
    val rarity: CostumeRarity,
    val description: String,
    val lore: String,
    val icon: ImageVector,
    val cashMultiplier: Float = 1.0f,
    val powerMultiplier: Float = 1.0f,
    val respectMultiplier: Float = 1.0f,
    val duelAttackBonus: Int = 0,
    val raidDefenseBonus: Int = 0,
    val unlockType: CostumeUnlockType = CostumeUnlockType.DEFAULT,
    val costCash: Double = 0.0,
    val costRespect: Long = 0L,
    val costTokens: Int = 0,
    val requiredPrestigeLevel: Int = 0,
    val requiredDuelsWon: Int = 0
)

object CostumeCatalog {
    val allCostumes = listOf(
        BossCostume(
            id = "costume_classic_capo",
            name = "Hero Cape & Sneakers",
            title = "Junior Explorer",
            rarity = CostumeRarity.COMMON,
            description = "A bright red superhero cape paired with high-top light-up sneakers!",
            lore = "The classic starter gear for aspiring park heroes. Lightweight, stylish, and full of boundless energy!",
            icon = Icons.Default.Person,
            cashMultiplier = 1.05f,
            powerMultiplier = 1.00f,
            respectMultiplier = 1.05f,
            duelAttackBonus = 5,
            raidDefenseBonus = 10,
            unlockType = CostumeUnlockType.DEFAULT
        ),
        BossCostume(
            id = "costume_cyber_enforcer",
            name = "Neon Robot Mascot Armor",
            title = "Tech Champion",
            rarity = CostumeRarity.RARE,
            description = "Glowing sky-blue robot armor with friendly digital visor and sound effects!",
            lore = "Custom-built for disco roller skating and friendly balloon tag championships!",
            icon = Icons.Default.Security,
            cashMultiplier = 1.10f,
            powerMultiplier = 1.25f,
            respectMultiplier = 1.05f,
            duelAttackBonus = 20,
            raidDefenseBonus = 35,
            unlockType = CostumeUnlockType.CASH,
            costCash = 15000.0
        ),
        BossCostume(
            id = "costume_velvet_tuxedo",
            name = "Sparkle Magician Tuxedo",
            title = "Wonder Magician",
            rarity = CostumeRarity.RARE,
            description = "A sparkly purple velvet suit with a top hat that pulls out plush rabbits!",
            lore = "Performs magical illusion tricks and shower crowds with chocolate gold coins!",
            icon = Icons.Default.Casino,
            cashMultiplier = 1.30f,
            powerMultiplier = 1.05f,
            respectMultiplier = 1.20f,
            duelAttackBonus = 10,
            raidDefenseBonus = 15,
            unlockType = CostumeUnlockType.CASH,
            costCash = 45000.0,
            costRespect = 1500L
        ),
        BossCostume(
            id = "costume_shadow_assassin",
            name = "Cosmic Astronaut Suit",
            title = "Space Explorer",
            rarity = CostumeRarity.EPIC,
            description = "A lightweight astronaut suit with zero-gravity jetpack boots and starlight goggles.",
            lore = "Soars across planetary rollercoasters and discovers secret constellation treasure boxes!",
            icon = Icons.Default.FlashOn,
            cashMultiplier = 1.20f,
            powerMultiplier = 1.45f,
            respectMultiplier = 1.15f,
            duelAttackBonus = 35,
            raidDefenseBonus = 25,
            unlockType = CostumeUnlockType.CASH,
            costCash = 120000.0,
            costRespect = 5000L
        ),
        BossCostume(
            id = "costume_dragon_haori",
            name = "Golden Dragon Champion Kimono",
            title = "Joy Martial Artist",
            rarity = CostumeRarity.EPIC,
            description = "A ceremonial golden silk robe embroidered with a cheerful dancing celestial dragon.",
            lore = "Honored by the Great Panda Dojo for showing unmatched kindness and sportsmanship!",
            icon = Icons.Default.Shield,
            cashMultiplier = 1.35f,
            powerMultiplier = 1.35f,
            respectMultiplier = 1.40f,
            duelAttackBonus = 30,
            raidDefenseBonus = 40,
            unlockType = CostumeUnlockType.RESPECT,
            costRespect = 12000L,
            costCash = 250000.0
        ),
        BossCostume(
            id = "costume_sovereign_robe",
            name = "Wonderland Monarch Robe",
            title = "Kingdom Ruler",
            rarity = CostumeRarity.LEGENDARY,
            description = "A royal ruby velvet cape with golden stars and an enchanted crown of sparkles.",
            lore = "Worn by the beloved leader who turns the entire city into a giant festival of fun!",
            icon = Icons.Default.WorkspacePremium,
            cashMultiplier = 1.60f,
            powerMultiplier = 1.40f,
            respectMultiplier = 1.50f,
            duelAttackBonus = 50,
            raidDefenseBonus = 60,
            unlockType = CostumeUnlockType.PRESTIGE_TIER,
            requiredPrestigeLevel = 1,
            costTokens = 2
        ),
        BossCostume(
            id = "costume_exosuit_overlord",
            name = "Rainbow Mecha Hero Suit",
            title = "Mecha Guardian",
            rarity = CostumeRarity.LEGENDARY,
            description = "High-tech mecha suit with confetti blasters and giant rainbow wings!",
            lore = "Equipped to launch park-wide fireworks displays and shield friends from rainy days.",
            icon = Icons.Default.MilitaryTech,
            cashMultiplier = 1.85f,
            powerMultiplier = 1.75f,
            respectMultiplier = 1.60f,
            duelAttackBonus = 75,
            raidDefenseBonus = 80,
            unlockType = CostumeUnlockType.PRESTIGE_TIER,
            requiredPrestigeLevel = 2,
            costTokens = 5
        ),
        BossCostume(
            id = "costume_immortal_godfather",
            name = "Grand Celestial Star Master Regalia",
            title = "Legendary Hero Deity",
            rarity = CostumeRarity.MYTHIC,
            description = "Radiant golden starlight armor crowned with the mythical Sparkle Scepter!",
            lore = "The highest summit of joyful wonder. A grand master whose park brings smiles across the entire galaxy!",
            icon = Icons.Default.AutoAwesome,
            cashMultiplier = 2.25f,
            powerMultiplier = 2.00f,
            respectMultiplier = 2.00f,
            duelAttackBonus = 120,
            raidDefenseBonus = 120,
            unlockType = CostumeUnlockType.PRESTIGE_TIER,
            requiredPrestigeLevel = 3,
            costTokens = 10
        )
    )

    fun getCostumeById(id: String): BossCostume {
        return allCostumes.find { it.id == id } ?: allCostumes.first()
    }
}
