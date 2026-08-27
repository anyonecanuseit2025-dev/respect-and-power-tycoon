package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PlayerProfileEntity::class,
        BusinessEntity::class,
        OperativeEntity::class,
        TurfDistrictEntity::class,
        RivalSyndicateEntity::class,
        SyndicateWarLogEntity::class,
        AllianceEntity::class,
        JointVentureEntity::class,
        ReputationContractEntity::class,
        LeaderboardEntryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "power_respect_tycoon_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial data in coroutine
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            val dao = database.gameDao()
                            dao.insertOrUpdateProfile(PlayerProfileEntity())
                            dao.insertBusinesses(InitialData.defaultBusinesses())
                            dao.insertOperatives(InitialData.defaultOperatives())
                            dao.insertDistricts(InitialData.defaultDistricts())
                            dao.insertRivals(InitialData.defaultRivals())
                            dao.insertAlliances(InitialData.defaultAlliances())
                            dao.insertJointVentures(InitialData.defaultJointVentures())
                            dao.insertReputationContracts(InitialData.defaultReputationContracts())
                            dao.insertLeaderboardEntries(InitialData.defaultLeaderboardEntries())
                            dao.insertWarLog(
                                SyndicateWarLogEntity(
                                    title = "Syndicate Founded",
                                    description = "You took control of the Neon Alleyways and declared your syndicate empire!",
                                    rewardCash = 500.0,
                                    rewardRespect = 100,
                                    rewardPower = 50,
                                    isVictory = true
                                )
                            )
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
