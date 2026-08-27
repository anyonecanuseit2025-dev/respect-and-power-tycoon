package com.example.data.local

import kotlinx.coroutines.flow.Flow

class GameRepository(private val dao: GameDao) {
    val playerProfile: Flow<PlayerProfileEntity?> = dao.getPlayerProfile()
    val businesses: Flow<List<BusinessEntity>> = dao.getAllBusinesses()
    val operatives: Flow<List<OperativeEntity>> = dao.getAllOperatives()
    val districts: Flow<List<TurfDistrictEntity>> = dao.getAllDistricts()
    val rivals: Flow<List<RivalSyndicateEntity>> = dao.getAllRivals()
    val warLogs: Flow<List<SyndicateWarLogEntity>> = dao.getRecentWarLogs()
    val alliances: Flow<List<AllianceEntity>> = dao.getAllAlliances()
    val jointVentures: Flow<List<JointVentureEntity>> = dao.getAllJointVentures()
    val reputationContracts: Flow<List<ReputationContractEntity>> = dao.getAllReputationContracts()
    val leaderboardEntries: Flow<List<LeaderboardEntryEntity>> = dao.getAllLeaderboardEntries()

    suspend fun getProfileSync(): PlayerProfileEntity? = dao.getPlayerProfileSync()
    suspend fun updateProfile(profile: PlayerProfileEntity) = dao.insertOrUpdateProfile(profile)

    suspend fun getBusinessesSync(): List<BusinessEntity> = dao.getAllBusinessesSync()
    suspend fun updateBusiness(business: BusinessEntity) = dao.updateBusiness(business)
    suspend fun insertBusinesses(businesses: List<BusinessEntity>) = dao.insertBusinesses(businesses)

    suspend fun getOperativesSync(): List<OperativeEntity> = dao.getAllOperativesSync()
    suspend fun updateOperative(operative: OperativeEntity) = dao.updateOperative(operative)

    suspend fun getDistrictsSync(): List<TurfDistrictEntity> = dao.getAllDistrictsSync()
    suspend fun updateDistrict(district: TurfDistrictEntity) = dao.updateDistrict(district)

    suspend fun getRivalsSync(): List<RivalSyndicateEntity> = dao.getAllRivalsSync()
    suspend fun updateRival(rival: RivalSyndicateEntity) = dao.updateRival(rival)

    suspend fun getAlliancesSync(): List<AllianceEntity> = dao.getAllAlliancesSync()
    suspend fun updateAlliance(alliance: AllianceEntity) = dao.updateAlliance(alliance)

    suspend fun getJointVenturesSync(): List<JointVentureEntity> = dao.getAllJointVenturesSync()
    suspend fun updateJointVenture(venture: JointVentureEntity) = dao.updateJointVenture(venture)

    suspend fun getReputationContractsSync(): List<ReputationContractEntity> = dao.getAllReputationContractsSync()
    suspend fun updateReputationContract(contract: ReputationContractEntity) = dao.updateReputationContract(contract)

    suspend fun getLeaderboardEntriesSync(): List<LeaderboardEntryEntity> = dao.getAllLeaderboardEntriesSync()
    suspend fun updateLeaderboardEntry(entry: LeaderboardEntryEntity) = dao.updateLeaderboardEntry(entry)
    suspend fun insertLeaderboardEntries(entries: List<LeaderboardEntryEntity>) = dao.insertLeaderboardEntries(entries)

    suspend fun addWarLog(log: SyndicateWarLogEntity) = dao.insertWarLog(log)
    suspend fun clearWarLogs() = dao.clearWarLogs()

    suspend fun resetForPrestige(
        currentProfile: PlayerProfileEntity,
        newPrestigeLevel: Int,
        tokensAwarded: Int
    ) {
        val baseBusinesses = InitialData.defaultBusinesses().map { b ->
            if (currentProfile.rebirthPerkAutoManagerLevel >= 1) {
                b.copy(isAutomated = true)
            } else {
                b
            }
        }
        dao.insertBusinesses(baseBusinesses)

        val baseOperatives = InitialData.defaultOperatives()
        dao.insertOperatives(baseOperatives)

        val baseDistricts = InitialData.defaultDistricts()
        dao.insertDistricts(baseDistricts)

        val heritageCash = currentProfile.rebirthPerkHeritageLevel * 50000.0
        val heritageRespect = currentProfile.rebirthPerkHeritageLevel * 3000L
        val heritagePower = currentProfile.rebirthPerkHeritageLevel * 1500L

        val prestigeBonusCash = (5000.0 * (newPrestigeLevel + 1)) + heritageCash
        val prestigeBonusRespect = (1500L * (newPrestigeLevel + 1)) + heritageRespect
        val prestigeBonusPower = (500L * (newPrestigeLevel + 1)) + heritagePower

        val updatedProfile = currentProfile.copy(
            title = when (newPrestigeLevel) {
                1 -> "Syndicate Underboss"
                2 -> "Syndicate Don"
                3 -> "Global Shadow Master"
                4 -> "Apex Sovereign Lord"
                else -> "Immortal Godfather Prime"
            },
            cash = prestigeBonusCash,
            respect = prestigeBonusRespect,
            power = prestigeBonusPower,
            influence = 20 + (newPrestigeLevel * 10),
            prestigeLevel = newPrestigeLevel,
            prestigeTokens = currentProfile.prestigeTokens + tokensAwarded,
            totalRebirthCount = currentProfile.totalRebirthCount + 1,
            lastActiveTimestamp = System.currentTimeMillis()
        )
        dao.insertOrUpdateProfile(updatedProfile)

        dao.insertWarLog(
            SyndicateWarLogEntity(
                title = "Syndicate Rebirth (Ascension $newPrestigeLevel)",
                description = "Ascended to higher underworld tier! Earned +$tokensAwarded Rebirth Tokens and permanent +${newPrestigeLevel * 50}% multiplier bonus.",
                rewardCash = prestigeBonusCash,
                rewardRespect = prestigeBonusRespect,
                rewardPower = prestigeBonusPower,
                isVictory = true
            )
        )
    }
}
