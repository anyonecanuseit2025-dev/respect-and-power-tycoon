package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AllianceEntity
import com.example.data.local.AppDatabase
import com.example.data.local.BusinessEntity
import com.example.data.local.GameRepository
import com.example.data.local.InitialData
import com.example.data.local.JointVentureEntity
import com.example.data.local.LeaderboardEntryEntity
import com.example.data.local.OperativeEntity
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.ReputationContractEntity
import com.example.data.local.RivalSyndicateEntity
import com.example.data.local.SyndicateWarLogEntity
import com.example.data.local.TurfDistrictEntity
import com.example.data.models.CostumeCatalog
import com.example.data.models.CostumeUnlockType
import com.example.data.models.RebirthConfig
import com.example.data.models.RebirthPerksCatalog
import com.example.data.playgames.PlayGamesAchievement
import com.example.data.playgames.PlayGamesState
import com.example.data.playgames.defaultAchievements
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.random.Random

data class OfflineGains(
    val secondsElapsed: Long,
    val cashGained: Double,
    val respectGained: Long,
    val powerGained: Long
)

data class CityEvent(
    val id: String,
    val title: String,
    val description: String,
    val choiceA: EventChoice,
    val choiceB: EventChoice
)

data class EventChoice(
    val label: String,
    val costInfluence: Int = 0,
    val costCash: Double = 0.0,
    val outcomeMessage: String,
    val rewardCash: Double = 0.0,
    val rewardRespect: Long = 0,
    val rewardPower: Long = 0,
    val reputationShift: Int = 0
)

data class TapEffect(
    val id: Long,
    val x: Float,
    val y: Float,
    val text: String
)

class TycoonViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = GameRepository(database.gameDao())

    val playerProfile: StateFlow<PlayerProfileEntity?> = repository.playerProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val businesses: StateFlow<List<BusinessEntity>> = repository.businesses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val operatives: StateFlow<List<OperativeEntity>> = repository.operatives
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val districts: StateFlow<List<TurfDistrictEntity>> = repository.districts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rivals: StateFlow<List<RivalSyndicateEntity>> = repository.rivals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val warLogs: StateFlow<List<SyndicateWarLogEntity>> = repository.warLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alliances: StateFlow<List<AllianceEntity>> = repository.alliances
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val jointVentures: StateFlow<List<JointVentureEntity>> = repository.jointVentures
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reputationContracts: StateFlow<List<ReputationContractEntity>> = repository.reputationContracts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val leaderboardEntries: StateFlow<List<LeaderboardEntryEntity>> = repository.leaderboardEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Google Play Games State
    private val _playGamesState = MutableStateFlow(
        PlayGamesState(
            isSignedIn = true,
            gamerTag = "ShadowDon_77",
            playGamesLevel = 4,
            totalXp = 1250,
            achievements = defaultAchievements()
        )
    )
    val playGamesState: StateFlow<PlayGamesState> = _playGamesState.asStateFlow()

    // Leaderboard Inspection & Filter
    private val _selectedLeaderboardEntry = MutableStateFlow<LeaderboardEntryEntity?>(null)
    val selectedLeaderboardEntry: StateFlow<LeaderboardEntryEntity?> = _selectedLeaderboardEntry.asStateFlow()

    private val _leaderboardFilter = MutableStateFlow("OVERALL")
    val leaderboardFilter: StateFlow<String> = _leaderboardFilter.asStateFlow()

    // UI Rates
    private val _cashPerSec = MutableStateFlow(0.0)
    val cashPerSec: StateFlow<Double> = _cashPerSec.asStateFlow()

    private val _respectPerSec = MutableStateFlow(0.0)
    val respectPerSec: StateFlow<Double> = _respectPerSec.asStateFlow()

    private val _powerPerSec = MutableStateFlow(0.0)
    val powerPerSec: StateFlow<Double> = _powerPerSec.asStateFlow()

    // Offline dialog
    private val _offlineGains = MutableStateFlow<OfflineGains?>(null)
    val offlineGains: StateFlow<OfflineGains?> = _offlineGains.asStateFlow()

    // Active City Random Encounter / Event
    private val _activeEvent = MutableStateFlow<CityEvent?>(null)
    val activeEvent: StateFlow<CityEvent?> = _activeEvent.asStateFlow()

    // Active Raid Result Dialog
    private val _lastRaidResult = MutableStateFlow<SyndicateWarLogEntity?>(null)
    val lastRaidResult: StateFlow<SyndicateWarLogEntity?> = _lastRaidResult.asStateFlow()

    // Floating Tap FX
    private val _tapEffects = MutableStateFlow<List<TapEffect>>(emptyList())
    val tapEffects: StateFlow<List<TapEffect>> = _tapEffects.asStateFlow()

    // 2-Player Pass-and-Play Multiplayer State
    private val _duelState = MutableStateFlow(MultiplayerDuelState())
    val duelState: StateFlow<MultiplayerDuelState> = _duelState.asStateFlow()

    // Message Toast / SnackBar state
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Intro Story & Lore Dialog state
    private val _showIntroStory = MutableStateFlow(false)
    val showIntroStory: StateFlow<Boolean> = _showIntroStory.asStateFlow()

    private var hasCheckedOffline = false

    init {
        ensureInitialDataSeeded()
        startGameLoop()
        startEventScheduler()
    }

    private fun ensureInitialDataSeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            val prof = repository.getProfileSync()
            if (prof == null) {
                repository.updateProfile(PlayerProfileEntity())
            }
            if (repository.getBusinessesSync().isEmpty()) {
                repository.insertBusinesses(InitialData.defaultBusinesses())
            }
            if (repository.getAlliancesSync().isEmpty()) {
                database.gameDao().insertAlliances(InitialData.defaultAlliances())
            }
            if (repository.getJointVenturesSync().isEmpty()) {
                database.gameDao().insertJointVentures(InitialData.defaultJointVentures())
            }
            if (repository.getReputationContractsSync().isEmpty()) {
                database.gameDao().insertReputationContracts(InitialData.defaultReputationContracts())
            }
            if (repository.getLeaderboardEntriesSync().isEmpty()) {
                database.gameDao().insertLeaderboardEntries(InitialData.defaultLeaderboardEntries())
            }
        }
    }

    private fun startGameLoop() {
        viewModelScope.launch(Dispatchers.Default) {
            var lastTickTime = System.currentTimeMillis()
            var rankRecalcCounter = 0

            while (isActive) {
                delay(250) // 4 ticks per second
                val now = System.currentTimeMillis()
                val deltaSec = (now - lastTickTime) / 1000.0
                lastTickTime = now

                val profile = playerProfile.value
                val bizList = businesses.value
                val opsList = operatives.value
                val distList = districts.value
                val allList = alliances.value
                val jvList = jointVentures.value

                if (profile == null) continue

                // Check offline earnings once profile loads
                if (!hasCheckedOffline && profile.lastActiveTimestamp > 0) {
                    hasCheckedOffline = true
                    checkOfflineEarnings(profile, bizList, opsList, distList)
                }

                // Costume Multipliers
                val equippedCostume = CostumeCatalog.getCostumeById(profile.equippedCostumeId)
                val costumeCashMul = equippedCostume.cashMultiplier.toDouble()
                val costumeRespectMul = equippedCostume.respectMultiplier.toDouble()
                val costumePowerMul = equippedCostume.powerMultiplier.toDouble()

                // Rebirth Perk Multipliers
                val rebirthBankMul = 1.0 + (profile.rebirthPerkBankLevel * 0.15)
                val rebirthPowerMul = 1.0 + (profile.rebirthPerkPowerLevel * 0.20)
                val rebirthRespectMul = 1.0 + (profile.rebirthPerkRespectLevel * 0.20)

                // Operatives
                var opCashMul = 1.0
                var opRespectMul = 1.0
                var totalOpPower = 0L
                for (op in opsList) {
                    if (op.isHired) {
                        opCashMul += (op.cashMultiplier - 1.0) * op.level
                        opRespectMul += (op.respectMultiplier - 1.0) * op.level
                        totalOpPower += (op.powerBonus * op.level).toLong()
                    }
                }

                // Controlled Districts
                var districtCashMul = 1.0
                var districtRespectBonus = 0L
                for (dist in distList) {
                    if (dist.isPlayerControlled) {
                        districtCashMul += (dist.revenueMultiplier - 1.0)
                        districtRespectBonus += dist.respectBonus
                    }
                }

                // Prestige Multiplier
                val prestigeMul = 1.0 + (profile.prestigeLevel * 0.5)

                // Reputation Alignment
                val isHonorable = profile.reputation >= 25
                val isRuthless = profile.reputation <= -25
                val repRespectMul = if (isHonorable) 1.25 else 1.0
                val repCashMul = if (isRuthless) 1.15 else 1.0

                // Alliances
                var allianceCashRate = 0.0
                val activeAlliance = allList.find { it.id == profile.allianceId && it.isAllied }
                if (activeAlliance != null) {
                    val trustMultiplier = 1.0 + (activeAlliance.trustScore / 100.0)
                    val honorBonus = if (isHonorable) 1.30 else 1.0
                    allianceCashRate = activeAlliance.dividendPerSec * trustMultiplier * honorBonus
                }

                // Joint Ventures
                var jvCashRate = 0.0
                var jvRespectRate = 0.0
                var jvPowerRate = 0.0
                for (jv in jvList) {
                    if (jv.isFunded) {
                        jvCashRate += jv.payoutPerSec
                        jvRespectRate += jv.respectBonus * 0.02
                        jvPowerRate += jv.powerBonus * 0.01
                    }
                }

                // Base Businesses
                var currentCashRate = 0.0
                var currentRespectRate = 0.0
                var currentPowerRate = 0.0

                for (biz in bizList) {
                    if (biz.level > 0) {
                        val upgradeMul = (1 + biz.upgradeLevel).toDouble()
                        val autoEfficiency = if (biz.isAutomated) 1.0 else 0.4
                        currentCashRate += (biz.level * biz.baseRevenuePerSec * upgradeMul * autoEfficiency)
                        currentRespectRate += (biz.level * biz.baseRespectPerSec * upgradeMul * autoEfficiency)
                        currentPowerRate += (biz.level * biz.basePowerPerSec * upgradeMul * autoEfficiency)
                    }
                }

                val finalCashRate = (currentCashRate * opCashMul * districtCashMul * repCashMul * costumeCashMul * rebirthBankMul + allianceCashRate + jvCashRate) * prestigeMul
                val finalRespectRate = (currentRespectRate * opRespectMul * repRespectMul * costumeRespectMul * rebirthRespectMul + (districtRespectBonus * 0.05) + jvRespectRate) * prestigeMul
                val finalPowerRate = ((currentPowerRate + jvPowerRate + totalOpPower) * costumePowerMul * rebirthPowerMul) * prestigeMul

                _cashPerSec.value = finalCashRate
                _respectPerSec.value = finalRespectRate
                _powerPerSec.value = finalPowerRate

                val cashIncrement = finalCashRate * deltaSec
                val respectIncrement = (finalRespectRate * deltaSec).toLong()
                val powerIncrement = (finalPowerRate * deltaSec).toLong()

                if (cashIncrement > 0 || respectIncrement > 0 || powerIncrement > 0) {
                    val updatedProfile = profile.copy(
                        cash = profile.cash + cashIncrement,
                        respect = profile.respect + respectIncrement,
                        power = profile.power + powerIncrement,
                        allTimeCashEarned = profile.allTimeCashEarned + cashIncrement,
                        lastActiveTimestamp = now
                    )
                    withContext(Dispatchers.IO) {
                        repository.updateProfile(updatedProfile)
                    }
                }

                rankRecalcCounter++
                if (rankRecalcCounter >= 8) {
                    rankRecalcCounter = 0
                    updateLeaderboardRankings(profile, distList, allList, jvList)
                    checkPlayGamesAchievements(profile)
                }
            }
        }
    }

    private fun checkOfflineEarnings(
        profile: PlayerProfileEntity,
        bizList: List<BusinessEntity>,
        opsList: List<OperativeEntity>,
        distList: List<TurfDistrictEntity>
    ) {
        val now = System.currentTimeMillis()
        val elapsedSec = (now - profile.lastActiveTimestamp) / 1000
        if (elapsedSec > 30) {
            val cappedSec = elapsedSec.coerceAtMost(28800)

            var baseRate = 0.0
            var respectRate = 0.0
            for (b in bizList) {
                if (b.level > 0 && b.isAutomated) {
                    baseRate += b.level * b.baseRevenuePerSec * (1 + b.upgradeLevel)
                    respectRate += b.level * b.baseRespectPerSec * (1 + b.upgradeLevel)
                }
            }

            val equippedCostume = CostumeCatalog.getCostumeById(profile.equippedCostumeId)
            val costumeMul = equippedCostume.cashMultiplier.toDouble()
            val perkBankMul = 1.0 + (profile.rebirthPerkBankLevel * 0.15)

            val earnedCash = baseRate * cappedSec * 0.8 * costumeMul * perkBankMul
            val earnedRespect = (respectRate * cappedSec * 0.8).toLong()
            val earnedPower = (cappedSec * 0.1).toLong()

            if (earnedCash > 50) {
                _offlineGains.value = OfflineGains(
                    secondsElapsed = elapsedSec,
                    cashGained = earnedCash,
                    respectGained = earnedRespect,
                    powerGained = earnedPower
                )
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateProfile(
                        profile.copy(
                            cash = profile.cash + earnedCash,
                            respect = profile.respect + earnedRespect,
                            power = profile.power + earnedPower,
                            allTimeCashEarned = profile.allTimeCashEarned + earnedCash,
                            lastActiveTimestamp = now
                        )
                    )
                }
            }
        }
    }

    private fun checkPlayGamesAchievements(profile: PlayerProfileEntity) {
        val currentPlayState = _playGamesState.value
        var newlyUnlocked: PlayGamesAchievement? = null
        val updatedAch = currentPlayState.achievements.map { ach ->
            if (ach.isUnlocked) ach
            else {
                val shouldUnlock = when (ach.id) {
                    "ach_first_hustle" -> profile.allTimeCashEarned >= 1000.0
                    "ach_syndicate_founder" -> businesses.value.count { it.level > 0 } >= 3
                    "ach_turf_conqueror" -> districts.value.any { it.isPlayerControlled && it.id != "district_slums" }
                    "ach_high_roller" -> businesses.value.any { (it.id == "penthouse_casino" || it.id == "neon_lounge") && it.level > 0 }
                    "ach_diplomat_alliance" -> alliances.value.any { it.isAllied }
                    "ach_duel_gladiator" -> profile.totalDuelsWon >= 1
                    "ach_costume_icon" -> profile.equippedCostumeId != "costume_classic_capo"
                    "ach_first_rebirth" -> profile.prestigeLevel >= 1 || profile.totalRebirthCount >= 1
                    "ach_billionaire_oligarch" -> profile.allTimeCashEarned >= 100_000_000.0
                    else -> false
                }
                if (shouldUnlock) {
                    val unlocked = ach.copy(isUnlocked = true, unlockedAtTimestamp = System.currentTimeMillis())
                    newlyUnlocked = unlocked
                    unlocked
                } else {
                    ach
                }
            }
        }

        if (newlyUnlocked != null) {
            _playGamesState.value = currentPlayState.copy(
                achievements = updatedAch,
                totalXp = currentPlayState.totalXp + newlyUnlocked.xpValue,
                playGamesLevel = ((currentPlayState.totalXp + newlyUnlocked.xpValue) / 1000) + 1,
                recentUnlockedAchievement = newlyUnlocked
            )
            _statusMessage.value = "Google Play Games: Unlocked '${newlyUnlocked.title}' (+${newlyUnlocked.xpValue} XP)!"
        }
    }

    fun dismissOfflineGains() {
        _offlineGains.value = null
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun dismissRaidResult() {
        _lastRaidResult.value = null
    }

    fun openIntroStory() {
        _showIntroStory.value = true
    }

    fun dismissIntroStory() {
        _showIntroStory.value = false
    }

    fun setLeaderboardFilter(filter: String) {
        _leaderboardFilter.value = filter
    }

    fun selectLeaderboardEntry(entry: LeaderboardEntryEntity?) {
        _selectedLeaderboardEntry.value = entry
    }

    private suspend fun updateLeaderboardRankings(
        profile: PlayerProfileEntity,
        distList: List<TurfDistrictEntity>,
        allList: List<AllianceEntity>,
        jvList: List<JointVentureEntity>
    ) {
        val controlledDistricts = distList.count { it.isPlayerControlled }
        val activeAlliesCount = allList.count { it.isAllied }
        val fundedJvsCount = jvList.count { it.isFunded }

        val calculatedRankScore = (profile.respect * 1.5).toLong() +
                (profile.power * 2.0).toLong() +
                (profile.allTimeCashEarned / 2000.0).toLong() +
                (profile.totalRaidWins * 300L) +
                (controlledDistricts * 1500L) +
                (fundedJvsCount * 2500L) +
                (activeAlliesCount * 1500L)

        val rankTier = when {
            calculatedRankScore >= 500000 -> "Apex Sovereign"
            calculatedRankScore >= 200000 -> "Diamond Overlord"
            calculatedRankScore >= 75000 -> "Platinum Don"
            calculatedRankScore >= 25000 -> "Gold Kingpin"
            calculatedRankScore >= 5000 -> "Silver Underboss"
            else -> "Bronze Capo"
        }

        val repTitle = getReputationTitle(profile.reputation)
        val currentEntries = repository.getLeaderboardEntriesSync().toMutableList()

        val playerEntry = LeaderboardEntryEntity(
            id = "player_entry",
            rank = 7,
            syndicateName = profile.clanName,
            leaderName = profile.name,
            power = profile.power,
            respect = profile.respect,
            netWorth = profile.allTimeCashEarned,
            reputationScore = profile.reputation,
            reputationAlignment = repTitle,
            rankTier = rankTier,
            clan = profile.clanName,
            territoriesCount = controlledDistricts,
            alliesCount = activeAlliesCount,
            isPlayer = true,
            avatar = "military_tech"
        )

        val updatedList = currentEntries.filter { !it.isPlayer }.toMutableList()
        updatedList.add(playerEntry)

        val sortedList = updatedList.sortedByDescending {
            (it.respect * 1.5) + (it.power * 2.0) + (it.netWorth / 2000.0) + (it.territoriesCount * 1500)
        }.mapIndexed { index, item ->
            item.copy(rank = index + 1)
        }

        val myRank = sortedList.find { it.isPlayer }?.rank ?: 7

        repository.insertLeaderboardEntries(sortedList)

        if (profile.rankScore != calculatedRankScore || profile.globalRank != myRank || profile.rankTier != rankTier || profile.reputationTitle != repTitle) {
            repository.updateProfile(
                profile.copy(
                    rankScore = calculatedRankScore,
                    globalRank = myRank,
                    rankTier = rankTier,
                    reputationTitle = repTitle
                )
            )
        }
    }

    private fun getReputationTitle(rep: Int): String = when {
        rep >= 60 -> "Honorable Syndicate Don"
        rep in 25..59 -> "Civic Underworld Protector"
        rep in -24..24 -> "Pragmatic Dealmaker"
        rep in -59..-25 -> "Ruthless Crime Kingpin"
        else -> "Shadow Lord of Extortion"
    }

    // Tap to Hustle
    fun onHustleTap(x: Float = 0f, y: Float = 0f) {
        val profile = playerProfile.value ?: return
        val prestigeMul = 1.0 + (profile.prestigeLevel * 0.5)
        val equippedCostume = CostumeCatalog.getCostumeById(profile.equippedCostumeId)
        val costumeMul = equippedCostume.cashMultiplier.toDouble()
        val perkBankMul = 1.0 + (profile.rebirthPerkBankLevel * 0.15)

        val isCrit = Random.nextFloat() < 0.20f
        val critMul = if (isCrit) 3.0 else 1.0

        val baseHustle = (15.0 + (profile.power * 0.2)) * prestigeMul * critMul * costumeMul * perkBankMul
        val respectGain = if (isCrit) 5L else 2L
        val powerGain = if (isCrit) 2L else 1L

        val text = if (isCrit) "+$${baseHustle.toLong()} CRIT!" else "+$${baseHustle.toLong()}"
        val effect = TapEffect(System.currentTimeMillis(), x, y, text)
        _tapEffects.value = (_tapEffects.value + effect).takeLast(6)

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(
                profile.copy(
                    cash = profile.cash + baseHustle,
                    respect = profile.respect + respectGain,
                    power = profile.power + powerGain,
                    allTimeCashEarned = profile.allTimeCashEarned + baseHustle,
                    lastActiveTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun buyBusiness(businessId: String) {
        val profile = playerProfile.value ?: return
        val bizList = businesses.value
        val target = bizList.find { it.id == businessId } ?: return

        val costDiscount = (1.0 - (profile.rebirthPerkCostReductionLevel * 0.08)).coerceAtLeast(0.5)
        val cost = target.baseCost * (target.costMultiplier.pow(target.level)) * costDiscount
        if (profile.cash < cost) {
            _statusMessage.value = "Insufficient Cash! Need $${cost.toLong()} to upgrade ${target.name}"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(
                cash = profile.cash - cost,
                power = profile.power + (target.basePowerPerSec * 2).toLong().coerceAtLeast(1),
                respect = profile.respect + (target.baseRespectPerSec * 2).toLong().coerceAtLeast(1)
            )
            val updatedBiz = target.copy(level = target.level + 1)

            repository.updateProfile(updatedProfile)
            repository.updateBusiness(updatedBiz)
        }
    }

    fun upgradeBusiness(businessId: String) {
        val profile = playerProfile.value ?: return
        val target = businesses.value.find { it.id == businessId } ?: return
        val costDiscount = (1.0 - (profile.rebirthPerkCostReductionLevel * 0.08)).coerceAtLeast(0.5)
        val upgradeCost = target.baseCost * 5.0 * (target.upgradeLevel + 1) * costDiscount

        if (profile.cash < upgradeCost) {
            _statusMessage.value = "Need $${upgradeCost.toLong()} for Facility Upgrade!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(cash = profile.cash - upgradeCost)
            val updatedBiz = target.copy(upgradeLevel = target.upgradeLevel + 1)

            repository.updateProfile(updatedProfile)
            repository.updateBusiness(updatedBiz)
            _statusMessage.value = "${target.name} upgraded! Output permanently doubled!"
        }
    }

    fun hireManager(businessId: String) {
        val profile = playerProfile.value ?: return
        val target = businesses.value.find { it.id == businessId } ?: return

        if (target.isAutomated) return
        if (profile.cash < target.managerCost) {
            _statusMessage.value = "Need $${target.managerCost.toLong()} to hire ${target.managerName}!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(cash = profile.cash - target.managerCost)
            val updatedBiz = target.copy(isAutomated = true)

            repository.updateProfile(updatedProfile)
            repository.updateBusiness(updatedBiz)
            _statusMessage.value = "${target.managerName} hired! ${target.name} is now 100% automated!"
        }
    }

    fun hireOperative(operativeId: String) {
        val profile = playerProfile.value ?: return
        val target = operatives.value.find { it.id == operativeId } ?: return

        val cost = if (target.isHired) target.costRespect * target.level else target.costRespect
        if (profile.respect < cost) {
            _statusMessage.value = "Insufficient Respect! Need $cost Respect to hire ${target.name}"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(respect = profile.respect - cost)
            val updatedOp = target.copy(
                isHired = true,
                level = if (target.isHired) target.level + 1 else 1
            )

            repository.updateProfile(updatedProfile)
            repository.updateOperative(updatedOp)
            _statusMessage.value = "${target.name} recruited! Syndicate power & revenue surged!"
        }
    }

    fun attackDistrict(districtId: String) {
        val profile = playerProfile.value ?: return
        val district = districts.value.find { it.id == districtId } ?: return

        val equippedCostume = CostumeCatalog.getCostumeById(profile.equippedCostumeId)
        val myPower = (profile.power * equippedCostume.powerMultiplier).toLong()
        val defPower = district.defensePower

        if (myPower < defPower * 0.5) {
            _statusMessage.value = "Your syndicate power is too low to breach ${district.name}!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val myRoll = (myPower * (0.85 + Random.nextDouble(0.3))).toLong()
            val defRoll = (defPower * (0.85 + Random.nextDouble(0.3))).toLong()

            if (myRoll >= defRoll) {
                val rewardCash = district.defensePower * 10.0
                val rewardRespect = district.respectBonus

                val updatedProfile = profile.copy(
                    cash = profile.cash + rewardCash,
                    respect = profile.respect + rewardRespect,
                    power = profile.power + (district.defensePower * 0.2).toLong(),
                    totalRaidWins = profile.totalRaidWins + 1
                )
                val updatedDist = district.copy(
                    isPlayerControlled = true,
                    controlledBy = "Player (${profile.clanName})"
                )

                repository.updateProfile(updatedProfile)
                repository.updateDistrict(updatedDist)

                val warLog = SyndicateWarLogEntity(
                    title = "District Conquered: ${district.name}",
                    description = "You seized control from rival forces! Gained permanent +${((district.revenueMultiplier - 1) * 100).toInt()}% cash bonus.",
                    rewardCash = rewardCash,
                    rewardRespect = rewardRespect,
                    rewardPower = (district.defensePower * 0.2).toLong(),
                    isVictory = true
                )
                repository.addWarLog(warLog)
                _lastRaidResult.value = warLog
            } else {
                val warLog = SyndicateWarLogEntity(
                    title = "Failed Takeover of ${district.name}",
                    description = "The defenders held the checkpoints ($myRoll vs $defRoll). Recruit more enforcers and try again!",
                    rewardCash = 0.0,
                    rewardRespect = 0,
                    rewardPower = 0,
                    isVictory = false
                )
                repository.addWarLog(warLog)
                _lastRaidResult.value = warLog
            }
        }
    }

    fun sabotageRival(rivalId: String) {
        val profile = playerProfile.value ?: return
        val rival = rivals.value.find { it.id == rivalId } ?: return

        if (profile.power < 50) {
            _statusMessage.value = "Need at least 50 Syndicate Power to launch a sabotage operation!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val success = Random.nextFloat() > 0.35f
            if (success) {
                val lootCash = rival.bounty.coerceAtLeast(5000.0)
                val lootRespect = (rival.respect * 0.1).toLong().coerceAtLeast(50L)
                val updatedProfile = profile.copy(
                    cash = profile.cash + lootCash,
                    respect = profile.respect + lootRespect,
                    totalRaidWins = profile.totalRaidWins + 1
                )
                val updatedRival = rival.copy(
                    power = (rival.power - 25).coerceAtLeast(10),
                    status = "Disrupted"
                )

                repository.updateProfile(updatedProfile)
                repository.updateRival(updatedRival)

                val log = SyndicateWarLogEntity(
                    title = "Sabotage Succeeded: ${rival.name}",
                    description = "Raided their distribution warehouses and seized black-market funds.",
                    rewardCash = lootCash,
                    rewardRespect = lootRespect,
                    rewardPower = 20,
                    isVictory = true
                )
                repository.addWarLog(log)
                _lastRaidResult.value = log
            } else {
                val log = SyndicateWarLogEntity(
                    title = "Sabotage Repelled by ${rival.name}",
                    description = "Rival sentries were fortified. Your operatives retreated safely.",
                    rewardCash = 0.0,
                    rewardRespect = 0,
                    rewardPower = 0,
                    isVictory = false
                )
                repository.addWarLog(log)
                _lastRaidResult.value = log
            }
        }
    }

    fun launchRaid(rivalId: String, strategy: String = "DIRECT_ASSAULT") {
        sabotageRival(rivalId)
    }

    fun sabotageRival(rivalId: String, operationType: String = "SABOTAGE") {
        sabotageRival(rivalId)
    }

    fun formAlliance(allianceId: String) {
        val profile = playerProfile.value ?: return
        val target = alliances.value.find { it.id == allianceId } ?: return

        if (profile.respect < 500) {
            _statusMessage.value = "Need at least 500 Respect to negotiate with ${target.name}!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedAlliance = target.copy(isAllied = true)
            val updatedProfile = profile.copy(allianceId = target.id)

            database.gameDao().updateAlliance(updatedAlliance)
            repository.updateProfile(updatedProfile)
            _statusMessage.value = "Pact sealed with ${target.name}! Dividends now generating every second."
        }
    }

    fun leaveAlliance(allianceId: String) {
        val profile = playerProfile.value ?: return
        val target = alliances.value.find { it.id == allianceId } ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val updatedAlliance = target.copy(isAllied = false)
            val updatedProfile = profile.copy(allianceId = null)
            database.gameDao().updateAlliance(updatedAlliance)
            repository.updateProfile(updatedProfile)
            _statusMessage.value = "Alliance with ${target.name} dissolved."
        }
    }

    fun sendAllianceAid(allianceId: String) {
        strengthenAlliance(allianceId)
    }

    fun betrayAlliance(allianceId: String) {
        val profile = playerProfile.value ?: return
        val target = alliances.value.find { it.id == allianceId } ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val loot = 15000.0 * (1.0 + (target.trustScore / 50.0))
            val updatedAlliance = target.copy(isAllied = false, trustScore = 0)
            val updatedProfile = profile.copy(
                allianceId = null,
                cash = profile.cash + loot,
                reputation = (profile.reputation - 25).coerceIn(-100, 100)
            )
            database.gameDao().updateAlliance(updatedAlliance)
            repository.updateProfile(updatedProfile)
            _statusMessage.value = "Betrayed ${target.name}! Plundered $${loot.toLong()} in syndicate assets."
        }
    }

    fun declareRivalry(rivalId: String) {
        val target = rivals.value.find { it.id == rivalId } ?: return
        _statusMessage.value = "Declared total syndicate war on ${target.name}!"
    }

    fun executeReputationContract(contractId: String) {
        signReputationContract(contractId)
    }

    fun performPrestige() {
        performRebirthAscension()
    }

    fun updateProfileDetails(name: String, clan: String) {
        updateProfileNameAndClan(name, clan)
    }

    fun strengthenAlliance(allianceId: String) {
        val profile = playerProfile.value ?: return
        val target = alliances.value.find { it.id == allianceId } ?: return
        val cost = 2500.0

        if (profile.cash < cost) {
            _statusMessage.value = "Need $2,500 to send gifts and bolster diplomatic trust!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedAlliance = target.copy(trustScore = (target.trustScore + 15).coerceAtMost(100))
            val updatedProfile = profile.copy(cash = profile.cash - cost)

            database.gameDao().updateAlliance(updatedAlliance)
            repository.updateProfile(updatedProfile)
            _statusMessage.value = "Trust with ${target.name} increased to ${updatedAlliance.trustScore}%!"
        }
    }

    fun fundJointVenture(ventureId: String) {
        val profile = playerProfile.value ?: return
        val target = jointVentures.value.find { it.id == ventureId } ?: return

        if (profile.cash < target.costCash) {
            _statusMessage.value = "Need $${target.costCash.toLong()} to finance ${target.title}!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedJv = target.copy(isFunded = true)
            val updatedProfile = profile.copy(
                cash = profile.cash - target.costCash,
                respect = profile.respect + target.respectBonus,
                power = profile.power + target.powerBonus
            )

            database.gameDao().updateJointVenture(updatedJv)
            repository.updateProfile(updatedProfile)
            _statusMessage.value = "Joint Venture '${target.title}' is funded! Payouts active."
        }
    }

    fun signReputationContract(contractId: String) {
        val profile = playerProfile.value ?: return
        val target = reputationContracts.value.find { it.id == contractId } ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(
                cash = profile.cash + target.rewardCash,
                respect = profile.respect + target.rewardRespect,
                power = profile.power + target.rewardPower,
                reputation = (profile.reputation + target.reputationShift).coerceIn(-100, 100)
            )
            val updatedContract = target.copy(isCompleted = true)

            database.gameDao().updateReputationContract(updatedContract)
            repository.updateProfile(updatedProfile)
            _statusMessage.value = "Contract completed! Reputation shifted by ${target.reputationShift}."
        }
    }

    // Costumes / Wardrobe actions
    fun equipCostume(costumeId: String) {
        val profile = playerProfile.value ?: return
        val unlocked = profile.unlockedCostumes.split(",").map { it.trim() }
        if (!unlocked.contains(costumeId)) {
            _statusMessage.value = "You have not unlocked this costume yet!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(equippedCostumeId = costumeId)
            repository.updateProfile(updatedProfile)
            val costume = CostumeCatalog.getCostumeById(costumeId)
            _statusMessage.value = "Equipped ${costume.name}! Active bonuses updated."
        }
    }

    fun unlockCostume(costumeId: String) {
        val profile = playerProfile.value ?: return
        val costume = CostumeCatalog.getCostumeById(costumeId)
        val unlocked = profile.unlockedCostumes.split(",").map { it.trim() }.toMutableList()

        if (unlocked.contains(costumeId)) return

        when (costume.unlockType) {
            CostumeUnlockType.CASH -> {
                if (profile.cash < costume.costCash) {
                    _statusMessage.value = "Need $${costume.costCash.toLong()} to unlock ${costume.name}!"
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    unlocked.add(costumeId)
                    val updated = profile.copy(
                        cash = profile.cash - costume.costCash,
                        unlockedCostumes = unlocked.joinToString(","),
                        equippedCostumeId = costumeId
                    )
                    repository.updateProfile(updated)
                    _statusMessage.value = "Unlocked & Equipped ${costume.name}!"
                }
            }
            CostumeUnlockType.RESPECT -> {
                if (profile.respect < costume.costRespect) {
                    _statusMessage.value = "Need ${costume.costRespect} Respect to unlock ${costume.name}!"
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    unlocked.add(costumeId)
                    val updated = profile.copy(
                        respect = profile.respect - costume.costRespect,
                        unlockedCostumes = unlocked.joinToString(","),
                        equippedCostumeId = costumeId
                    )
                    repository.updateProfile(updated)
                    _statusMessage.value = "Unlocked & Equipped ${costume.name}!"
                }
            }
            CostumeUnlockType.PRESTIGE_TOKENS -> {
                if (profile.prestigeTokens < costume.costTokens) {
                    _statusMessage.value = "Need ${costume.costTokens} Rebirth Tokens for ${costume.name}!"
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    unlocked.add(costumeId)
                    val updated = profile.copy(
                        prestigeTokens = profile.prestigeTokens - costume.costTokens,
                        unlockedCostumes = unlocked.joinToString(","),
                        equippedCostumeId = costumeId
                    )
                    repository.updateProfile(updated)
                    _statusMessage.value = "Unlocked & Equipped ${costume.name}!"
                }
            }
            CostumeUnlockType.PRESTIGE_TIER -> {
                if (profile.prestigeLevel < costume.requiredPrestigeLevel) {
                    _statusMessage.value = "Requires Rebirth Ascension Tier ${costume.requiredPrestigeLevel}!"
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    unlocked.add(costumeId)
                    val updated = profile.copy(
                        unlockedCostumes = unlocked.joinToString(","),
                        equippedCostumeId = costumeId
                    )
                    repository.updateProfile(updated)
                    _statusMessage.value = "Unlocked & Equipped ${costume.name}!"
                }
            }
            else -> {}
        }
    }

    // Rebirth / Ascension actions
    fun upgradeRebirthPerk(perkId: String) {
        val profile = playerProfile.value ?: return
        val perk = RebirthPerksCatalog.createPerkList(0, 0, 0, 0, 0, 0).find { it.id == perkId } ?: return

        val currentLvl = when (perkId) {
            "perk_swiss_vault" -> profile.rebirthPerkBankLevel
            "perk_iron_militia" -> profile.rebirthPerkPowerLevel
            "perk_diplomatic_magnate" -> profile.rebirthPerkRespectLevel
            "perk_master_laundering" -> profile.rebirthPerkCostReductionLevel
            "perk_sovereign_heritage" -> profile.rebirthPerkHeritageLevel
            "perk_autonomous_syndicate" -> profile.rebirthPerkAutoManagerLevel
            else -> 0
        }

        if (currentLvl >= perk.maxLevel) {
            _statusMessage.value = "${perk.name} is already maxed!"
            return
        }

        if (profile.prestigeTokens < perk.tokenCostPerLevel) {
            _statusMessage.value = "Need ${perk.tokenCostPerLevel} Rebirth Tokens to upgrade!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updated = when (perkId) {
                "perk_swiss_vault" -> profile.copy(
                    prestigeTokens = profile.prestigeTokens - perk.tokenCostPerLevel,
                    rebirthPerkBankLevel = profile.rebirthPerkBankLevel + 1
                )
                "perk_iron_militia" -> profile.copy(
                    prestigeTokens = profile.prestigeTokens - perk.tokenCostPerLevel,
                    rebirthPerkPowerLevel = profile.rebirthPerkPowerLevel + 1
                )
                "perk_diplomatic_magnate" -> profile.copy(
                    prestigeTokens = profile.prestigeTokens - perk.tokenCostPerLevel,
                    rebirthPerkRespectLevel = profile.rebirthPerkRespectLevel + 1
                )
                "perk_master_laundering" -> profile.copy(
                    prestigeTokens = profile.prestigeTokens - perk.tokenCostPerLevel,
                    rebirthPerkCostReductionLevel = profile.rebirthPerkCostReductionLevel + 1
                )
                "perk_sovereign_heritage" -> profile.copy(
                    prestigeTokens = profile.prestigeTokens - perk.tokenCostPerLevel,
                    rebirthPerkHeritageLevel = profile.rebirthPerkHeritageLevel + 1
                )
                "perk_autonomous_syndicate" -> profile.copy(
                    prestigeTokens = profile.prestigeTokens - perk.tokenCostPerLevel,
                    rebirthPerkAutoManagerLevel = profile.rebirthPerkAutoManagerLevel + 1
                )
                else -> profile
            }
            repository.updateProfile(updated)
            _statusMessage.value = "${perk.name} upgraded to Level ${currentLvl + 1}!"
        }
    }

    fun performRebirthAscension() {
        val profile = playerProfile.value ?: return
        val currentPrestige = profile.prestigeLevel
        val nextTier = RebirthConfig.getNextRebirthTier(currentPrestige)

        if (profile.cash < nextTier.requiredCash || profile.respect < nextTier.requiredRespect) {
            _statusMessage.value = "Ascension requirements not met!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val newPrestige = currentPrestige + 1
            repository.resetForPrestige(profile, newPrestige, nextTier.tokensAwarded)
            _statusMessage.value = "Ascension complete! Reborn as ${nextTier.name} (+${nextTier.tokensAwarded} Tokens)!"
        }
    }

    fun syncPlayGamesCloudSave() {
        val currentPlayState = _playGamesState.value
        _playGamesState.value = currentPlayState.copy(
            isCloudSaveSynced = true,
            lastCloudSyncTime = System.currentTimeMillis()
        )
        _statusMessage.value = "Google Play Games: Cloud Save synchronized successfully!"
    }

    fun togglePlayGamesSignIn() {
        val current = _playGamesState.value
        val newState = !current.isSignedIn
        _playGamesState.value = current.copy(isSignedIn = newState)
        _statusMessage.value = if (newState) "Signed in as ${current.gamerTag}" else "Signed out of Google Play Games"
    }

    fun updateProfileNameAndClan(name: String, clanName: String) {
        val profile = playerProfile.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(profile.copy(name = name, clanName = clanName))
            _statusMessage.value = "Syndicate credentials updated!"
        }
    }

    // Dynamic Events Scheduler
    private fun startEventScheduler() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                delay(45000)
                if (_activeEvent.value == null && playerProfile.value != null) {
                    val events = listOf(
                        CityEvent(
                            id = "police_sweep",
                            title = "High-Level Police Raid",
                            description = "Federal agents are raiding commercial hubs across the district demanding permits.",
                            choiceA = EventChoice(
                                label = "Bribe Inspector ($1,500)",
                                costCash = 1500.0,
                                outcomeMessage = "The inspector accepted your envelope. Operations remain untouched and respect grew!",
                                rewardRespect = 150,
                                reputationShift = -5
                            ),
                            choiceB = EventChoice(
                                label = "Flex Political Influence (3 INF)",
                                costInfluence = 3,
                                outcomeMessage = "City Hall called off the investigation immediately. Your reputation is untouchable!",
                                rewardRespect = 350,
                                rewardPower = 50,
                                reputationShift = +10
                            )
                        ),
                        CityEvent(
                            id = "rival_truce",
                            title = "Black Market Arms Shipment",
                            description = "An unregistered cargo container loaded with tactical military hardware arrived at the bay.",
                            choiceA = EventChoice(
                                label = "Seize Armament ($5,000)",
                                costCash = 5000.0,
                                outcomeMessage = "Your syndicates intercepted the cache! Massive power upgrade acquired.",
                                rewardPower = 200,
                                rewardRespect = 100,
                                reputationShift = -10
                            ),
                            choiceB = EventChoice(
                                label = "Auction to Underworld",
                                outcomeMessage = "You brokered the deal to foreign cartels for a quick, fat profit.",
                                rewardCash = 12000.0,
                                rewardRespect = 50,
                                reputationShift = +5
                            )
                        )
                    )
                    _activeEvent.value = events.random()
                }
            }
        }
    }

    fun handleEventChoice(choice: EventChoice) {
        val profile = playerProfile.value ?: return

        if (profile.cash < choice.costCash) {
            _statusMessage.value = "Not enough cash for this decision!"
            return
        }
        if (profile.influence < choice.costInfluence) {
            _statusMessage.value = "Not enough Political Influence for this action!"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val updatedProfile = profile.copy(
                cash = (profile.cash - choice.costCash) + choice.rewardCash,
                influence = profile.influence - choice.costInfluence,
                respect = profile.respect + choice.rewardRespect,
                power = profile.power + choice.rewardPower,
                reputation = (profile.reputation + choice.reputationShift).coerceIn(-100, 100)
            )
            repository.updateProfile(updatedProfile)
            _activeEvent.value = null
            _statusMessage.value = choice.outcomeMessage
        }
    }

    fun dismissEvent() {
        _activeEvent.value = null
    }

    // 2-Player Pass-and-Play Multiplayer Duel Logic
    fun startNewDuel() {
        _duelState.value = MultiplayerDuelState(
            isGameActive = true,
            currentRound = 1,
            maxRounds = 5,
            activePlayer = DuelPlayer.PLAYER_ONE,
            p1Cash = 500,
            p2Cash = 500,
            p1Respect = 100,
            p2Respect = 100,
            p1ActionPoints = 3,
            p2ActionPoints = 3,
            lastClashLog = "Match Started! Boss 1, choose your tactical moves for Round 1.",
            isPassingPhone = false,
            isMatchFinished = false,
            winner = null
        )
    }

    fun selectDuelSector(sectorId: Int) {
        _duelState.value = _duelState.value.copy(selectedSectorId = sectorId)
    }

    fun playDuelCard(card: DuelCombatCard) {
        val current = _duelState.value
        if (current.isMatchFinished || current.isPassingPhone) return

        val isP1 = current.activePlayer == DuelPlayer.PLAYER_ONE
        val currentAp = if (isP1) current.p1ActionPoints else current.p2ActionPoints

        if (currentAp < card.apCost) {
            _statusMessage.value = "Not enough Action Points! (Need ${card.apCost} AP)"
            return
        }

        val updatedSectors = current.sectors.map { sector ->
            if (sector.id == current.selectedSectorId) {
                var p1P = sector.p1Power
                var p2P = sector.p2Power

                when (card.effectType) {
                    CardEffect.DEPLOY_BRUISERS -> {
                        if (isP1) p1P += 40 else p2P += 40
                    }
                    CardEffect.TACTICAL_SNIPERS -> {
                        if (isP1) p1P += 80 else p2P += 80
                    }
                    CardEffect.BRIBE_INSPECTOR -> {
                        if (isP1) p2P = (p2P - 30).coerceAtLeast(0) else p1P = (p1P - 30).coerceAtLeast(0)
                    }
                    CardEffect.SABOTAGE_GRID -> {
                        if (isP1) p2P = (p2P - 50).coerceAtLeast(0) else p1P = (p1P - 50).coerceAtLeast(0)
                    }
                    CardEffect.HOSTILE_BUYOUT -> {
                        if (isP1) {
                            val stolen = 30.coerceAtMost(p2P)
                            p2P -= stolen
                            p1P += stolen
                        } else {
                            val stolen = 30.coerceAtMost(p1P)
                            p1P -= stolen
                            p2P += stolen
                        }
                    }
                    CardEffect.RESPECT_SURGE -> {}
                }
                sector.copy(p1Power = p1P, p2Power = p2P)
            } else sector
        }

        val p1RespectGain = if (isP1 && card.effectType == CardEffect.RESPECT_SURGE) 100 else 0
        val p2RespectGain = if (!isP1 && card.effectType == CardEffect.RESPECT_SURGE) 100 else 0

        _duelState.value = current.copy(
            sectors = updatedSectors,
            p1ActionPoints = if (isP1) current.p1ActionPoints - card.apCost else current.p1ActionPoints,
            p2ActionPoints = if (!isP1) current.p2ActionPoints - card.apCost else current.p2ActionPoints,
            p1Respect = current.p1Respect + p1RespectGain,
            p2Respect = current.p2Respect + p2RespectGain,
            lastClashLog = "${current.activePlayer.displayName} played '${card.title}' on sector ${current.selectedSectorId}!"
        )
    }

    fun endDuelTurn() {
        val current = _duelState.value
        if (current.isMatchFinished) return

        if (current.activePlayer == DuelPlayer.PLAYER_ONE) {
            _duelState.value = current.copy(
                activePlayer = DuelPlayer.PLAYER_TWO,
                isPassingPhone = true,
                lastClashLog = "Turn complete! Pass phone to ${DuelPlayer.PLAYER_TWO.displayName}."
            )
        } else {
            resolveDuelRoundClash()
        }
    }

    fun confirmPhonePassed() {
        _duelState.value = _duelState.value.copy(isPassingPhone = false)
    }

    private fun resolveDuelRoundClash() {
        val current = _duelState.value
        var p1GainedCash = 0
        var p2GainedCash = 0
        var p1GainedRespect = 0
        var p2GainedRespect = 0

        val resolvedSectors = current.sectors.map { sector ->
            val owner = when {
                sector.p1Power > sector.p2Power -> DuelPlayer.PLAYER_ONE
                sector.p2Power > sector.p1Power -> DuelPlayer.PLAYER_TWO
                else -> null
            }
            if (owner == DuelPlayer.PLAYER_ONE) {
                p1GainedCash += sector.cashReward
                p1GainedRespect += sector.respectReward
            } else if (owner == DuelPlayer.PLAYER_TWO) {
                p2GainedCash += sector.cashReward
                p2GainedRespect += sector.respectReward
            }
            sector.copy(controlledBy = owner)
        }

        val nextRound = current.currentRound + 1
        val isFinished = nextRound > current.maxRounds

        var winner: DuelPlayer? = null
        if (isFinished) {
            val p1Score = (current.p1Cash + p1GainedCash) + (current.p1Respect + p1GainedRespect) * 3 + (resolvedSectors.count { it.controlledBy == DuelPlayer.PLAYER_ONE } * 500)
            val p2Score = (current.p2Cash + p2GainedCash) + (current.p2Respect + p2GainedRespect) * 3 + (resolvedSectors.count { it.controlledBy == DuelPlayer.PLAYER_TWO } * 500)
            winner = if (p1Score >= p2Score) DuelPlayer.PLAYER_ONE else DuelPlayer.PLAYER_TWO

            if (winner == DuelPlayer.PLAYER_ONE) {
                playerProfile.value?.let { prof ->
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.updateProfile(prof.copy(totalDuelsWon = prof.totalDuelsWon + 1))
                    }
                }
            }
        }

        val p1Controlled = resolvedSectors.count { it.controlledBy == DuelPlayer.PLAYER_ONE }
        val p2Controlled = resolvedSectors.count { it.controlledBy == DuelPlayer.PLAYER_TWO }

        val roundSummary = "Round ${current.currentRound} Resolved: Boss 1 controls $p1Controlled sectors. Boss 2 controls $p2Controlled sectors."

        _duelState.value = current.copy(
            sectors = resolvedSectors,
            currentRound = nextRound.coerceAtMost(current.maxRounds),
            p1Cash = current.p1Cash + p1GainedCash + 200,
            p2Cash = current.p2Cash + p2GainedCash + 200,
            p1Respect = current.p1Respect + p1GainedRespect,
            p2Respect = current.p2Respect + p2GainedRespect,
            p1ActionPoints = 3,
            p2ActionPoints = 3,
            activePlayer = DuelPlayer.PLAYER_ONE,
            isPassingPhone = !isFinished,
            isMatchFinished = isFinished,
            winner = winner,
            lastClashLog = roundSummary
        )
    }
}
