package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.RivalSyndicateEntity
import com.example.data.local.SyndicateWarLogEntity
import com.example.data.local.TurfDistrictEntity
import com.example.ui.components.formatCurrency
import com.example.ui.components.formatNumber
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CrimsonDark
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
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PowerPurple

@Composable
fun TurfWarScreen(
    profile: PlayerProfileEntity?,
    districts: List<TurfDistrictEntity>,
    rivals: List<RivalSyndicateEntity>,
    warLogs: List<SyndicateWarLogEntity>,
    onAttackDistrict: (String) -> Unit,
    onLaunchRaid: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Wonder Zones", "Challenge League", "Activity Feed")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = GeoSurface,
            contentColor = GoldPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldPrimary,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            color = if (selectedTab == index) GoldPrimary else GeoTextSecondary
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> CityDistrictsTab(profile, districts, onAttackDistrict)
            1 -> RivalLeagueTab(profile, rivals, onLaunchRaid)
            2 -> WarLogsTab(warLogs)
        }
    }
}

@Composable
private fun CityDistrictsTab(
    profile: PlayerProfileEntity?,
    districts: List<TurfDistrictEntity>,
    onAttackDistrict: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize().testTag("city_turfs_list")
    ) {
        item {
            DistrictControlBanner(districts)
        }

        items(districts, key = { it.id }) { district ->
            val playerPower = profile?.power ?: 0
            val isOwned = district.isPlayerControlled
            val canConquer = playerPower >= (district.defensePower * 0.7) && !isOwned

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        1.dp,
                        if (isOwned) CashGreen.copy(alpha = 0.6f) else GeoBorder,
                        RoundedCornerShape(14.dp)
                    ),
                color = GeoSurfaceElevated,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isOwned) Color(0xFF1E3A2E) else GeoSurfaceHighlight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isOwned) Icons.Default.CheckCircle else Icons.Default.LocationCity,
                                    contentDescription = null,
                                    tint = if (isOwned) CashGreen else CyanTactical,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = district.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "Controlled By: ${district.controlledBy}",
                                    fontSize = 11.sp,
                                    color = if (isOwned) CashGreen else GeoTextSecondary
                                )
                            }
                        }

                        // Difficulty Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GeoSurfaceSubtle)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = district.difficulty,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (district.difficulty) {
                                    "Easy" -> CashGreen
                                    "Moderate" -> CyanTactical
                                    "Hard" -> GoldPrimary
                                    else -> CrimsonAlert
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = district.description,
                        fontSize = 12.sp,
                        color = GeoTextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stats & Bonuses Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(GeoSurfaceSubtle)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = PowerPurple,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Def: ${formatNumber(district.defensePower)} PWR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextPrimary
                            )
                        }

                        Text(
                            text = "+${((district.revenueMultiplier - 1) * 100).toInt()}% Cash Boost",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CashGreen
                        )

                        Text(
                            text = "+${formatNumber(district.respectBonus)} REP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (isOwned) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ZONE CELEBRATING & OPEN",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = CashGreen,
                                letterSpacing = 1.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = { onAttackDistrict(district.id) },
                            enabled = canConquer,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = Color(0xFF231B00),
                                disabledContainerColor = GeoSurfaceHighlight,
                                disabledContentColor = GeoTextTertiary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("attack_district_${district.id}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (playerPower >= district.defensePower * 0.7) "UNLOCK WONDER ZONE" else "NEED MORE HERO POWER (${playerPower}/${district.defensePower})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DistrictControlBanner(districts: List<TurfDistrictEntity>) {
    val controlledCount = districts.count { it.isPlayerControlled }
    val totalCount = districts.size
    val pct = if (totalCount > 0) (controlledCount.toFloat() / totalCount * 100).toInt() else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    listOf(GeoSurfaceHighlight, GeoSurfaceElevated)
                )
            )
            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "WONDER KINGDOM DOMINANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$controlledCount of $totalCount Zones Opened & Glowing ($pct%)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$pct%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF231B00)
                )
            }
        }
    }
}

@Composable
private fun RivalLeagueTab(
    profile: PlayerProfileEntity?,
    rivals: List<RivalSyndicateEntity>,
    onLaunchRaid: (String, String) -> Unit
) {
    var selectedStratagem by remember { mutableStateOf("Balloon Tag") }
    val stratagems = listOf("Balloon Tag", "Dance-Off", "Mascot Relay")

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize().testTag("rival_league_list")
    ) {
        item {
            // Stratagem Selector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GeoSurfaceElevated)
                    .border(1.dp, GeoBorderSubtle, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "SELECT PLAYFUL CHALLENGE TACTIC",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = CyanTactical,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    stratagems.forEach { strat ->
                        FilterChip(
                            selected = selectedStratagem == strat,
                            onClick = { selectedStratagem = strat },
                            label = { Text(text = strat, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary,
                                selectedLabelColor = Color(0xFF231B00),
                                containerColor = GeoSurfaceSubtle,
                                labelColor = GeoTextPrimary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        items(rivals, key = { it.id }) { rival ->
            val playerPower = profile?.power ?: 0
            val winChancePct = when {
                playerPower >= rival.power * 1.5 -> 90
                playerPower >= rival.power -> 65
                playerPower >= rival.power * 0.7 -> 40
                else -> 15
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, GeoBorder, RoundedCornerShape(14.dp)),
                color = GeoSurfaceElevated,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(CyanTactical.copy(alpha = 0.2f))
                                .border(1.dp, CyanTactical, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#${rival.rank}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CyanTactical
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = rival.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "Leader: ${rival.leader}",
                                    fontSize = 11.sp,
                                    color = GeoTextSecondary
                                )
                            }
                        }

                        // Status Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE0F7FA))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = rival.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanTactical
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rival Metrics: Power | Respect | Bounty Loot
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(GeoSurfaceSubtle)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Club Power", fontSize = 9.sp, color = GeoTextTertiary)
                            Text(
                                text = "${formatNumber(rival.power)} PWR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PowerPurple
                            )
                        }

                        Column {
                            Text(text = "Smile Stars", fontSize = 9.sp, color = GeoTextTertiary)
                            Text(
                                text = "${formatNumber(rival.respect)} REP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }

                        Column {
                            Text(text = "Prize Vault", fontSize = 9.sp, color = GeoTextTertiary)
                            Text(
                                text = formatCurrency(rival.bounty),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CashGreen
                            )
                        }

                        Column {
                            Text(text = "Win Odds", fontSize = 9.sp, color = GeoTextTertiary)
                            Text(
                                text = "$winChancePct%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = if (winChancePct >= 65) CashGreen else if (winChancePct >= 40) GoldPrimary else CyanTactical
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onLaunchRaid(rival.id, selectedStratagem) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF231B00)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("raid_rival_${rival.id}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PLAY $selectedStratagem MATCH",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WarLogsTab(warLogs: List<SyndicateWarLogEntity>) {
    if (warLogs.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = GeoTextTertiary,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No Syndicate Battles Yet",
                    color = GeoTextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Conquer city sectors or raid rivals to build history!",
                    fontSize = 12.sp,
                    color = GeoTextTertiary
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(warLogs, key = { it.id }) { log ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            if (log.isVictory) CashGreen.copy(alpha = 0.4f) else CrimsonAlert.copy(alpha = 0.4f),
                            RoundedCornerShape(10.dp)
                        ),
                    color = GeoSurfaceElevated
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = log.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (log.isVictory) CashGreen else CrimsonAlert
                            )
                            Text(
                                text = if (log.isVictory) "VICTORY" else "DEFEAT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = if (log.isVictory) CashGreen else CrimsonAlert
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = log.description,
                            fontSize = 12.sp,
                            color = GeoTextSecondary
                        )

                        if (log.isVictory && (log.rewardCash > 0 || log.rewardRespect > 0 || log.rewardPower > 0)) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                if (log.rewardCash > 0) {
                                    Text(
                                        text = "+${formatCurrency(log.rewardCash)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CashGreen
                                    )
                                }
                                if (log.rewardRespect > 0) {
                                    Text(
                                        text = "+${formatNumber(log.rewardRespect)} REP",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldPrimary
                                    )
                                }
                                if (log.rewardPower > 0) {
                                    Text(
                                        text = "+${formatNumber(log.rewardPower)} PWR",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PowerPurple
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
