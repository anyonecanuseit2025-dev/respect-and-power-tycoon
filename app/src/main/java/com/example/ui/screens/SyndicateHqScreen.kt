package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OperativeEntity
import com.example.data.local.PlayerProfileEntity
import com.example.data.playgames.PlayGamesState
import com.example.ui.theme.GeoBackground
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoTextSecondary
import com.example.ui.theme.GoldPrimary
import com.example.viewmodel.DuelCombatCard
import com.example.viewmodel.MultiplayerDuelState

@Composable
fun SyndicateHqScreen(
    profile: PlayerProfileEntity?,
    operatives: List<OperativeEntity>,
    duelState: MultiplayerDuelState,
    playGamesState: PlayGamesState,
    onHireOperative: (String) -> Unit,
    onStartNewDuel: () -> Unit,
    onSelectSector: (Int) -> Unit,
    onPlayCard: (DuelCombatCard) -> Unit,
    onEndTurn: () -> Unit,
    onConfirmPhonePassed: () -> Unit,
    onEquipCostume: (String) -> Unit,
    onUnlockCostume: (String) -> Unit,
    onPerformRebirth: () -> Unit,
    onUpgradePerk: (String) -> Unit,
    onSyncCloudSave: () -> Unit,
    onTogglePlayGamesSignIn: () -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    onOpenStoryAndCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Enforcers", "Wardrobe", "Rebirth", "2P Duel", "Play Games & Clan")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = GeoSurface,
            contentColor = GoldPrimary,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = GoldPrimary,
                    height = 3.dp
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedSubTab == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedSubTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) GoldPrimary else GeoTextSecondary
                        )
                    },
                    modifier = Modifier.testTag("hq_subtab_$index")
                )
            }
        }

        when (selectedSubTab) {
            0 -> OperativesScreen(
                profile = profile,
                operatives = operatives,
                onHireOperative = onHireOperative
            )
            1 -> CostumesScreen(
                profile = profile,
                onEquipCostume = onEquipCostume,
                onUnlockCostume = onUnlockCostume
            )
            2 -> RebirthAscensionScreen(
                profile = profile,
                onPerformRebirth = onPerformRebirth,
                onUpgradePerk = onUpgradePerk
            )
            3 -> PassAndPlayDuelScreen(
                duelState = duelState,
                onStartNewDuel = onStartNewDuel,
                onSelectSector = onSelectSector,
                onPlayCard = onPlayCard,
                onEndTurn = onEndTurn,
                onConfirmPhonePassed = onConfirmPhonePassed
            )
            4 -> PlayGamesAndClanScreen(
                profile = profile,
                playGamesState = playGamesState,
                onSyncCloudSave = onSyncCloudSave,
                onToggleSignIn = onTogglePlayGamesSignIn,
                onUpdateProfile = onUpdateProfile,
                onOpenStoryAndCredits = onOpenStoryAndCredits
            )
        }
    }
}
