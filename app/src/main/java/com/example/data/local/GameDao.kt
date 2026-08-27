package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    // Player Profile
    @Query("SELECT * FROM player_profile WHERE id = 1")
    fun getPlayerProfile(): Flow<PlayerProfileEntity?>

    @Query("SELECT * FROM player_profile WHERE id = 1")
    suspend fun getPlayerProfileSync(): PlayerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: PlayerProfileEntity)

    @Update
    suspend fun updateProfile(profile: PlayerProfileEntity)

    // Businesses
    @Query("SELECT * FROM businesses")
    fun getAllBusinesses(): Flow<List<BusinessEntity>>

    @Query("SELECT * FROM businesses")
    suspend fun getAllBusinessesSync(): List<BusinessEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusinesses(businesses: List<BusinessEntity>)

    @Update
    suspend fun updateBusiness(business: BusinessEntity)

    // Operatives
    @Query("SELECT * FROM operatives")
    fun getAllOperatives(): Flow<List<OperativeEntity>>

    @Query("SELECT * FROM operatives")
    suspend fun getAllOperativesSync(): List<OperativeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperatives(operatives: List<OperativeEntity>)

    @Update
    suspend fun updateOperative(operative: OperativeEntity)

    // Turf Districts
    @Query("SELECT * FROM turf_districts")
    fun getAllDistricts(): Flow<List<TurfDistrictEntity>>

    @Query("SELECT * FROM turf_districts")
    suspend fun getAllDistrictsSync(): List<TurfDistrictEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<TurfDistrictEntity>)

    @Update
    suspend fun updateDistrict(district: TurfDistrictEntity)

    // Rival Syndicates
    @Query("SELECT * FROM rival_syndicates ORDER BY rank ASC")
    fun getAllRivals(): Flow<List<RivalSyndicateEntity>>

    @Query("SELECT * FROM rival_syndicates ORDER BY rank ASC")
    suspend fun getAllRivalsSync(): List<RivalSyndicateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRivals(rivals: List<RivalSyndicateEntity>)

    @Update
    suspend fun updateRival(rival: RivalSyndicateEntity)

    // War Logs
    @Query("SELECT * FROM war_logs ORDER BY timestamp DESC LIMIT 25")
    fun getRecentWarLogs(): Flow<List<SyndicateWarLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWarLog(log: SyndicateWarLogEntity)

    @Query("DELETE FROM war_logs")
    suspend fun clearWarLogs()

    // Alliances
    @Query("SELECT * FROM alliances")
    fun getAllAlliances(): Flow<List<AllianceEntity>>

    @Query("SELECT * FROM alliances")
    suspend fun getAllAlliancesSync(): List<AllianceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlliances(alliances: List<AllianceEntity>)

    @Update
    suspend fun updateAlliance(alliance: AllianceEntity)

    // Joint Ventures
    @Query("SELECT * FROM joint_ventures")
    fun getAllJointVentures(): Flow<List<JointVentureEntity>>

    @Query("SELECT * FROM joint_ventures")
    suspend fun getAllJointVenturesSync(): List<JointVentureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJointVentures(ventures: List<JointVentureEntity>)

    @Update
    suspend fun updateJointVenture(venture: JointVentureEntity)

    // Reputation Contracts
    @Query("SELECT * FROM reputation_contracts")
    fun getAllReputationContracts(): Flow<List<ReputationContractEntity>>

    @Query("SELECT * FROM reputation_contracts")
    suspend fun getAllReputationContractsSync(): List<ReputationContractEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReputationContracts(contracts: List<ReputationContractEntity>)

    @Update
    suspend fun updateReputationContract(contract: ReputationContractEntity)

    // Leaderboard Entries
    @Query("SELECT * FROM leaderboard_entries ORDER BY rank ASC")
    fun getAllLeaderboardEntries(): Flow<List<LeaderboardEntryEntity>>

    @Query("SELECT * FROM leaderboard_entries ORDER BY rank ASC")
    suspend fun getAllLeaderboardEntriesSync(): List<LeaderboardEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntries(entries: List<LeaderboardEntryEntity>)

    @Update
    suspend fun updateLeaderboardEntry(entry: LeaderboardEntryEntity)
}
