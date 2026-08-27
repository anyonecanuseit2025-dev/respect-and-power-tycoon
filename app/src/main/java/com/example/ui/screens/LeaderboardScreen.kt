package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LeaderboardEntryEntity
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.formatCurrency
import com.example.ui.components.formatNumber
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CyanTactical
import com.example.ui.theme.GeoBackground
import com.example.ui.theme.GeoBorder
import com.example.ui.theme.GeoBorderSubtle
import com.example.ui.theme.GeoSurface
import com.example.ui.theme.GeoSurfaceElevated
import com.example.ui.theme.GeoSurfaceHighlight
import com.example.ui.theme.GeoSurfaceSubtle
import com.example.ui.theme.GeoTextPrimary
import com.example.ui.theme.GeoTextSecondary
import com.example.ui.theme.GeoTextTertiary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PowerPurple

enum class LeaderboardCategory(val label: String) {
    OVERALL("Dominance Score"),
    RESPECT("Respect Points"),
    POWER("Power Index"),
    NET_WORTH("Empire Wealth")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    profile: PlayerProfileEntity?,
    leaderboardEntries: List<LeaderboardEntryEntity>,
    onProposeAlliance: (String) -> Unit,
    onDeclareRivalry: (String) -> Unit,
    onChallengeDuel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(LeaderboardCategory.OVERALL) }
    var selectedEntryForDossier by remember { mutableStateOf<LeaderboardEntryEntity?>(null) }
    val sheetState = rememberModalBottomSheetState()

    // Sort entries based on selected category
    val sortedEntries = remember(leaderboardEntries, selectedCategory) {
        when (selectedCategory) {
            LeaderboardCategory.OVERALL -> leaderboardEntries.sortedBy { it.rank }
            LeaderboardCategory.RESPECT -> leaderboardEntries.sortedByDescending { it.respect }
            LeaderboardCategory.POWER -> leaderboardEntries.sortedByDescending { it.power }
            LeaderboardCategory.NET_WORTH -> leaderboardEntries.sortedByDescending { it.netWorth }
        }
    }

    val playerRankEntry = leaderboardEntries.find { it.isPlayer }
    val playerScore = playerRankEntry?.let {
        (it.respect * 1.5 + it.power * 2.0 + it.netWorth / 100.0).toLong()
    } ?: 0L

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Hero Card: Player's Rank & Tier Status
        Card(
            colors = CardDefaults.cardColors(containerColor = GeoSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, GoldPrimary, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(GoldPrimary, GoldDark)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = Color(0xFF231B00),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("YOUR GLOBAL RANKING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                            Text(
                                text = "#${profile?.globalRank ?: 7} in the Underworld",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GeoTextPrimary
                            )
                            Text(
                                text = "Tier: ${profile?.rankTier ?: "Capo"} • Score: ${formatNumber(playerScore)} PTS",
                                fontSize = 11.sp,
                                color = CyanTactical
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldPrimary)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = profile?.rankTier ?: "Capo",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF231B00)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Formula Explainer
                Text(
                    text = "Score = (Respect × 1.5) + (Power × 2.0) + (Net Worth / 100)",
                    fontSize = 10.sp,
                    color = GeoTextTertiary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            LeaderboardCategory.values().forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = category },
                    label = {
                        Text(
                            text = category.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = Color(0xFF231B00),
                        containerColor = GeoSurfaceElevated,
                        labelColor = GeoTextSecondary
                    ),
                    modifier = Modifier.testTag("filter_leaderboard_${category.name.lowercase()}")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Leaderboard List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(sortedEntries) { displayIndex, entry ->
                LeaderboardRowItem(
                    entry = entry,
                    displayRank = displayIndex + 1,
                    selectedCategory = selectedCategory,
                    onInspect = { selectedEntryForDossier = entry }
                )
            }
        }
    }

    // Modal Bottom Sheet: Full Syndicate Dossier
    selectedEntryForDossier?.let { entry ->
        ModalBottomSheet(
            onDismissRequest = { selectedEntryForDossier = null },
            sheetState = sheetState,
            containerColor = GeoSurface,
            contentColor = GeoTextPrimary
        ) {
            SyndicateDossierSheetContent(
                entry = entry,
                onDismiss = { selectedEntryForDossier = null },
                onProposeAlliance = {
                    onProposeAlliance(entry.syndicateName)
                    selectedEntryForDossier = null
                },
                onDeclareRivalry = {
                    onDeclareRivalry(entry.syndicateName)
                    selectedEntryForDossier = null
                },
                onChallengeDuel = {
                    onChallengeDuel()
                    selectedEntryForDossier = null
                }
            )
        }
    }
}

@Composable
private fun LeaderboardRowItem(
    entry: LeaderboardEntryEntity,
    displayRank: Int,
    selectedCategory: LeaderboardCategory,
    onInspect: () -> Unit
) {
    val isPodium = displayRank <= 3
    val rankBadgeBg = when (displayRank) {
        1 -> Brush.linearGradient(listOf(GoldPrimary, GoldDark))
        2 -> Brush.linearGradient(listOf(Color(0xFFE0E0E0), Color(0xFF9E9E9E)))
        3 -> Brush.linearGradient(listOf(Color(0xFFCD7F32), Color(0xFF8B4513)))
        else -> Brush.linearGradient(listOf(GeoSurfaceElevated, GeoSurfaceElevated))
    }
    val rankTextColor = if (isPodium) Color(0xFF231B00) else GeoTextPrimary

    val repColor = when {
        entry.reputationScore >= 25 -> CashGreen
        entry.reputationScore <= -25 -> Color(0xFFFF5252)
        else -> CyanTactical
    }

    val overallScore = (entry.respect * 1.5 + entry.power * 2.0 + entry.netWorth / 100.0).toLong()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isPlayer) GeoSurfaceHighlight else GeoSurface
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (entry.isPlayer) GoldPrimary else GeoBorderSubtle,
                RoundedCornerShape(12.dp)
            )
            .clickable { onInspect() }
            .testTag("leaderboard_entry_${entry.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rank Number Pill
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(rankBadgeBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$displayRank",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = rankTextColor
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = entry.syndicateName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (entry.isPlayer) GoldPrimary else GeoTextPrimary
                        )
                        if (entry.isPlayer) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(GoldPrimary)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text("YOU", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color(0xFF231B00))
                            }
                        }
                    }
                    Text(
                        text = "Leader: ${entry.leaderName} • ${entry.clan}",
                        fontSize = 10.sp,
                        color = GeoTextSecondary
                    )
                    Row(
                        modifier = Modifier.padding(top = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entry.reputationAlignment,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = repColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tier: ${entry.rankTier}",
                            fontSize = 9.sp,
                            color = GeoTextTertiary
                        )
                    }
                }
            }

            // Metric Value
            Column(horizontalAlignment = Alignment.End) {
                val primaryValue = when (selectedCategory) {
                    LeaderboardCategory.OVERALL -> "${formatNumber(overallScore)} PTS"
                    LeaderboardCategory.RESPECT -> "${formatNumber(entry.respect)} REP"
                    LeaderboardCategory.POWER -> "${formatNumber(entry.power)} PWR"
                    LeaderboardCategory.NET_WORTH -> formatCurrency(entry.netWorth)
                }

                val primaryColor = when (selectedCategory) {
                    LeaderboardCategory.OVERALL -> GoldPrimary
                    LeaderboardCategory.RESPECT -> GoldPrimary
                    LeaderboardCategory.POWER -> PowerPurple
                    LeaderboardCategory.NET_WORTH -> CashGreen
                }

                Text(
                    text = primaryValue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryColor
                )

                Text(
                    text = "Tap to Inspect",
                    fontSize = 9.sp,
                    color = CyanTactical
                )
            }
        }
    }
}

@Composable
private fun SyndicateDossierSheetContent(
    entry: LeaderboardEntryEntity,
    onDismiss: () -> Unit,
    onProposeAlliance: () -> Unit,
    onDeclareRivalry: () -> Unit,
    onChallengeDuel: () -> Unit
) {
    val repColor = when {
        entry.reputationScore >= 25 -> CashGreen
        entry.reputationScore <= -25 -> Color(0xFFFF5252)
        else -> CyanTactical
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SYNDICATE INTELLIGENCE DOSSIER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanTactical,
                    letterSpacing = 1.sp
                )
                Text(
                    text = entry.syndicateName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = GeoTextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Dossier Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(GeoSurfaceElevated)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DossierMetricRow("Leader & Capo", entry.leaderName)
            DossierMetricRow("Syndicate Clan", entry.clan)
            DossierMetricRow("Rank Standing", "#${entry.rank} (${entry.rankTier})")
            DossierMetricRow("Respect Score", formatNumber(entry.respect))
            DossierMetricRow("Power Index", formatNumber(entry.power))
            DossierMetricRow("Controlled Districts", "${entry.territoriesCount} Districts")
            DossierMetricRow("Empire Net Worth", formatCurrency(entry.netWorth))
            DossierMetricRow("Reputation Alignment", "${entry.reputationAlignment} (${entry.reputationScore})", repColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!entry.isPlayer) {
            Text(
                text = "DIPLOMATIC & TACTICAL ACTIONS:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GeoTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onProposeAlliance,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanTactical, contentColor = Color(0xFF1C1B1F)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("dossier_alliance_btn")
                ) {
                    Text("Pact", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onDeclareRivalry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("dossier_rival_btn")
                ) {
                    Text("Rivalry", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onChallengeDuel,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color(0xFF231B00)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("dossier_duel_btn")
                ) {
                    Text("Duel", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DossierMetricRow(label: String, value: String, valueColor: Color = GeoTextPrimary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 11.sp, color = GeoTextSecondary)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
