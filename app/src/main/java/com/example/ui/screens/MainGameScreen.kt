package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.HeaderStatusBar
import com.example.ui.theme.GeoBackground
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoTextSecondary
import com.example.ui.theme.GeoTextTertiary
import com.example.ui.theme.GoldPrimary
import com.example.viewmodel.TycoonViewModel

enum class MainNavDestination(val label: String) {
    EMPIRE("Attractions"),
    TURF_WARS("Zones"),
    DIPLOMACY("Clubs"),
    LEADERBOARD("Rankings"),
    SYNDICATE_HQ("Hero HQ")
}

@Composable
fun MainGameScreen(
    viewModel: TycoonViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    val profile by viewModel.playerProfile.collectAsStateWithLifecycle()
    val businesses by viewModel.businesses.collectAsStateWithLifecycle()
    val operatives by viewModel.operatives.collectAsStateWithLifecycle()
    val districts by viewModel.districts.collectAsStateWithLifecycle()
    val rivals by viewModel.rivals.collectAsStateWithLifecycle()
    val warLogs by viewModel.warLogs.collectAsStateWithLifecycle()
    val alliances by viewModel.alliances.collectAsStateWithLifecycle()
    val jointVentures by viewModel.jointVentures.collectAsStateWithLifecycle()
    val reputationContracts by viewModel.reputationContracts.collectAsStateWithLifecycle()
    val leaderboardEntries by viewModel.leaderboardEntries.collectAsStateWithLifecycle()

    val cashRate by viewModel.cashPerSec.collectAsStateWithLifecycle()
    val respectRate by viewModel.respectPerSec.collectAsStateWithLifecycle()
    val powerRate by viewModel.powerPerSec.collectAsStateWithLifecycle()

    val tapEffects by viewModel.tapEffects.collectAsStateWithLifecycle()
    val offlineGains by viewModel.offlineGains.collectAsStateWithLifecycle()
    val activeEvent by viewModel.activeEvent.collectAsStateWithLifecycle()
    val lastRaidResult by viewModel.lastRaidResult.collectAsStateWithLifecycle()
    val duelState by viewModel.duelState.collectAsStateWithLifecycle()
    val playGamesState by viewModel.playGamesState.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()
    val showIntroStory by viewModel.showIntroStory.collectAsStateWithLifecycle()

    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = GeoBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            HeaderStatusBar(
                profile = profile,
                cashRate = cashRate,
                respectRate = respectRate,
                powerRate = powerRate,
                onOpenStory = { viewModel.openIntroStory() }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = GeoSurface,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav")
            ) {
                MainNavDestination.values().forEachIndexed { index, destination ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = index },
                        icon = {
                            Icon(
                                imageVector = when (destination) {
                                    MainNavDestination.EMPIRE -> Icons.Default.Storefront
                                    MainNavDestination.TURF_WARS -> Icons.Default.LocationCity
                                    MainNavDestination.DIPLOMACY -> Icons.Default.Handshake
                                    MainNavDestination.LEADERBOARD -> Icons.Default.Leaderboard
                                    MainNavDestination.SYNDICATE_HQ -> Icons.Default.MilitaryTech
                                },
                                contentDescription = destination.label
                            )
                        },
                        label = {
                            Text(
                                text = destination.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF231B00),
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = GeoTextSecondary,
                            unselectedTextColor = GeoTextTertiary
                        ),
                        modifier = Modifier.testTag("nav_tab_${destination.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentTab, label = "screen_transition") { tab ->
                when (tab) {
                    0 -> EmpireScreen(
                        profile = profile,
                        businesses = businesses,
                        tapEffects = tapEffects,
                        onHustleTap = { viewModel.onHustleTap() },
                        onBuyBusiness = { viewModel.buyBusiness(it) },
                        onUpgradeBusiness = { viewModel.upgradeBusiness(it) },
                        onHireManager = { viewModel.hireManager(it) }
                    )
                    1 -> TurfWarScreen(
                        profile = profile,
                        districts = districts,
                        rivals = rivals,
                        warLogs = warLogs,
                        onAttackDistrict = { viewModel.attackDistrict(it) },
                        onLaunchRaid = { rivalId, strat -> viewModel.launchRaid(rivalId, strat) }
                    )
                    2 -> DiplomacyReputationScreen(
                        profile = profile,
                        alliances = alliances,
                        jointVentures = jointVentures,
                        reputationContracts = reputationContracts,
                        rivals = rivals,
                        onFormAlliance = { viewModel.formAlliance(it) },
                        onLeaveAlliance = { viewModel.leaveAlliance(it) },
                        onSendAllianceAid = { viewModel.sendAllianceAid(it) },
                        onBetrayAlliance = { viewModel.betrayAlliance(it) },
                        onFundJointVenture = { viewModel.fundJointVenture(it) },
                        onDeclareRivalry = { viewModel.declareRivalry(it) },
                        onSabotageRival = { rivalId, op -> viewModel.sabotageRival(rivalId, op) },
                        onExecuteContract = { viewModel.executeReputationContract(it) }
                    )
                    3 -> LeaderboardScreen(
                        profile = profile,
                        leaderboardEntries = leaderboardEntries,
                        onProposeAlliance = { syndicateName ->
                            val ally = alliances.find { it.name.contains(syndicateName, ignoreCase = true) }
                            if (ally != null) viewModel.formAlliance(ally.id)
                            else viewModel.formAlliance(alliances.firstOrNull()?.id ?: "")
                        },
                        onDeclareRivalry = { syndicateName ->
                            val rival = rivals.find { it.name.contains(syndicateName, ignoreCase = true) }
                            if (rival != null) viewModel.declareRivalry(rival.id)
                            else viewModel.declareRivalry(rivals.firstOrNull()?.id ?: "")
                        },
                        onChallengeDuel = {
                            currentTab = 4 // switch to Syndicate HQ (Duel Arena tab)
                        }
                    )
                    4 -> SyndicateHqScreen(
                        profile = profile,
                        operatives = operatives,
                        duelState = duelState,
                        playGamesState = playGamesState,
                        onHireOperative = { viewModel.hireOperative(it) },
                        onStartNewDuel = { viewModel.startNewDuel() },
                        onSelectSector = { viewModel.selectDuelSector(it) },
                        onPlayCard = { viewModel.playDuelCard(it) },
                        onEndTurn = { viewModel.endDuelTurn() },
                        onConfirmPhonePassed = { viewModel.confirmPhonePassed() },
                        onEquipCostume = { viewModel.equipCostume(it) },
                        onUnlockCostume = { viewModel.unlockCostume(it) },
                        onPerformRebirth = { viewModel.performRebirthAscension() },
                        onUpgradePerk = { viewModel.upgradeRebirthPerk(it) },
                        onSyncCloudSave = { viewModel.syncPlayGamesCloudSave() },
                        onTogglePlayGamesSignIn = { viewModel.togglePlayGamesSignIn() },
                        onUpdateProfile = { name, clan -> viewModel.updateProfileDetails(name, clan) },
                        onOpenStoryAndCredits = { viewModel.openIntroStory() }
                    )
                }
            }

            // Dialog Overlays
            if (showIntroStory) {
                IntroStoryDialog(
                    onDismiss = { viewModel.dismissIntroStory() }
                )
            }

            offlineGains?.let { gains ->
                OfflineGainsDialog(
                    offlineGains = gains,
                    onDismiss = { viewModel.dismissOfflineGains() }
                )
            }

            activeEvent?.let { event ->
                CityEventDialog(
                    event = event,
                    onChoice = { choice -> viewModel.handleEventChoice(choice) },
                    onDismiss = { viewModel.dismissEvent() }
                )
            }

            lastRaidResult?.let { warLog ->
                RaidResultDialog(
                    warLog = warLog,
                    onDismiss = { viewModel.dismissRaidResult() }
                )
            }
        }
    }
}
