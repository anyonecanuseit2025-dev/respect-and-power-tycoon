package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_profile")
data class PlayerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Don Valentino",
    val title: String = "Street Capo",
    val cash: Double = 500.0,
    val respect: Long = 100,
    val power: Long = 50,
    val influence: Int = 10,
    val reputation: Int = 10, // -100 (Ruthless) to +100 (Honorable)
    val reputationTitle: String = "Pragmatic Capo",
    val clanName: String = "Apex Syndicate",
    val allianceId: String? = null,
    val allianceName: String = "Independent Syndicate",
    val prestigeLevel: Int = 0,
    val totalRaidWins: Int = 0,
    val totalRaidLosses: Int = 0,
    val allTimeCashEarned: Double = 500.0,
    val rankScore: Long = 250,
    val globalRank: Int = 88,
    val rankTier: String = "Bronze Capo",
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val equippedCostumeId: String = "costume_classic_capo",
    val unlockedCostumes: String = "costume_classic_capo",
    val prestigeTokens: Int = 0,
    val rebirthPerkBankLevel: Int = 0,
    val rebirthPerkPowerLevel: Int = 0,
    val rebirthPerkRespectLevel: Int = 0,
    val rebirthPerkCostReductionLevel: Int = 0,
    val rebirthPerkHeritageLevel: Int = 0,
    val rebirthPerkAutoManagerLevel: Int = 0,
    val totalRebirthCount: Int = 0,
    val totalDuelsWon: Int = 0,
    val playGamesGamerTag: String = "SovereignBoss#7729"
)

@Entity(tableName = "businesses")
data class BusinessEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val level: Int = 0,
    val baseCost: Double,
    val costMultiplier: Double = 1.15,
    val baseRevenuePerSec: Double,
    val baseRespectPerSec: Double,
    val basePowerPerSec: Double,
    val upgradeLevel: Int = 0,
    val isAutomated: Boolean = false,
    val managerName: String,
    val managerCost: Double,
    val iconName: String,
    val description: String
)

@Entity(tableName = "operatives")
data class OperativeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val alias: String,
    val role: String,
    val powerBonus: Int,
    val respectMultiplier: Float,
    val cashMultiplier: Float,
    val costCash: Double,
    val costRespect: Long,
    val isHired: Boolean = false,
    val level: Int = 1,
    val bio: String
)

@Entity(tableName = "turf_districts")
data class TurfDistrictEntity(
    @PrimaryKey val id: String,
    val name: String,
    val controlledBy: String,
    val isPlayerControlled: Boolean,
    val defensePower: Long,
    val revenueMultiplier: Float,
    val respectBonus: Long,
    val difficulty: String,
    val description: String
)

@Entity(tableName = "rival_syndicates")
data class RivalSyndicateEntity(
    @PrimaryKey val id: String,
    val name: String,
    val leader: String,
    val power: Long,
    val respect: Long,
    val bounty: Double,
    val rank: Int,
    val status: String,
    val avatarCrest: String,
    val territory: String,
    val isDeclaredRival: Boolean = false,
    val sabotageVulnerability: String = "Supply Lines",
    val sabotageDisruptedUntil: Long = 0L
)

@Entity(tableName = "alliances")
data class AllianceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val leader: String,
    val pactType: String, // "Mutual Defense", "Trade Conglomerate", "Cartel Pact", "Cyber Network"
    val trustScore: Int = 50, // 0 to 100
    val dividendPerSec: Double = 15.0,
    val defenseBonus: Int = 100,
    val isAllied: Boolean = false,
    val avatar: String = "handshake",
    val description: String,
    val totalMembers: Int = 12
)

@Entity(tableName = "joint_ventures")
data class JointVentureEntity(
    @PrimaryKey val id: String,
    val allianceId: String,
    val title: String,
    val description: String,
    val costCash: Double,
    val payoutPerSec: Double,
    val respectBonus: Long,
    val powerBonus: Long,
    val isFunded: Boolean = false,
    val fundedAmount: Double = 0.0
)

@Entity(tableName = "reputation_contracts")
data class ReputationContractEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "Honorable Deal", "Ruthless Takeover", "Diplomatic Accord", "Shadow Extortion"
    val requiredRepMin: Int = -100,
    val requiredRepMax: Int = 100,
    val description: String,
    val rewardCash: Double,
    val rewardRespect: Long,
    val rewardPower: Long,
    val reputationShift: Int, // e.g. +15 or -20
    val isCompleted: Boolean = false,
    val cooldownUntil: Long = 0L
)

@Entity(tableName = "leaderboard_entries")
data class LeaderboardEntryEntity(
    @PrimaryKey val id: String,
    val rank: Int,
    val syndicateName: String,
    val leaderName: String,
    val power: Long,
    val respect: Long,
    val netWorth: Double,
    val reputationScore: Int,
    val reputationAlignment: String,
    val rankTier: String,
    val clan: String,
    val territoriesCount: Int,
    val alliesCount: Int,
    val isPlayer: Boolean = false,
    val avatar: String = "military_tech"
)

@Entity(tableName = "war_logs")
data class SyndicateWarLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val rewardCash: Double,
    val rewardRespect: Long,
    val rewardPower: Long,
    val isVictory: Boolean
)

