package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AllianceEntity
import com.example.data.local.JointVentureEntity
import com.example.data.local.PlayerProfileEntity
import com.example.data.local.ReputationContractEntity
import com.example.data.local.RivalSyndicateEntity
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

@Composable
fun DiplomacyReputationScreen(
    profile: PlayerProfileEntity?,
    alliances: List<AllianceEntity>,
    jointVentures: List<JointVentureEntity>,
    reputationContracts: List<ReputationContractEntity>,
    rivals: List<RivalSyndicateEntity>,
    onFormAlliance: (String) -> Unit,
    onLeaveAlliance: (String) -> Unit,
    onSendAllianceAid: (String) -> Unit,
    onBetrayAlliance: (String) -> Unit,
    onFundJointVenture: (String) -> Unit,
    onDeclareRivalry: (String) -> Unit,
    onSabotageRival: (String, String) -> Unit,
    onExecuteContract: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Reputation", "Alliances", "Rivalries & Sabotage")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
    ) {
        // Top Sub-Tab Navigation
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = GeoSurface,
            contentColor = GoldPrimary,
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
                    modifier = Modifier.testTag("diplomacy_tab_$index")
                )
            }
        }

        when (selectedSubTab) {
            0 -> ReputationTabContent(
                profile = profile,
                contracts = reputationContracts,
                onExecuteContract = onExecuteContract
            )
            1 -> AlliancesTabContent(
                profile = profile,
                alliances = alliances,
                jointVentures = jointVentures,
                onFormAlliance = onFormAlliance,
                onLeaveAlliance = onLeaveAlliance,
                onSendAllianceAid = onSendAllianceAid,
                onBetrayAlliance = onBetrayAlliance,
                onFundJointVenture = onFundJointVenture
            )
            2 -> RivalriesTabContent(
                profile = profile,
                rivals = rivals,
                onDeclareRivalry = onDeclareRivalry,
                onSabotageRival = onSabotageRival
            )
        }
    }
}

// ---------------------------------------------------------------------------------
// TAB 1: REPUTATION & DIPLOMATIC OPPORTUNITIES
// ---------------------------------------------------------------------------------

@Composable
private fun ReputationTabContent(
    profile: PlayerProfileEntity?,
    contracts: List<ReputationContractEntity>,
    onExecuteContract: (String) -> Unit
) {
    val repScore = profile?.reputation ?: 10
    val repNormalized = ((repScore + 100) / 200f).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Reputation Scoreboard Hero Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, GeoBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "UNDERWORLD REPUTATION",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanTactical,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = profile?.reputationTitle ?: "Pragmatic Dealmaker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = GeoTextPrimary
                            )
                        }

                        val repColor = when {
                            repScore >= 25 -> CashGreen
                            repScore <= -25 -> Color(0xFFFF5252)
                            else -> CyanTactical
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(repColor.copy(alpha = 0.15f))
                                .border(1.dp, repColor, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${if (repScore > 0) "+" else ""}$repScore REP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = repColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Visual Reputation Spectrum Bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ruthless (-100)", fontSize = 10.sp, color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                            Text("Pragmatic (0)", fontSize = 10.sp, color = GeoTextTertiary)
                            Text("Honorable (+100)", fontSize = 10.sp, color = CashGreen, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(GeoSurfaceElevated)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(
                                                Color(0xFFFF5252),
                                                Color(0xFFFFB300),
                                                CyanTactical,
                                                CashGreen
                                            )
                                        )
                                    )
                            )
                        }

                        LinearProgressIndicator(
                            progress = { repNormalized },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = GoldPrimary,
                            trackColor = Color.Transparent
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Alignment Perks Table
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GeoSurfaceElevated)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "CURRENT REPUTATION PERKS:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        if (repScore >= 25) {
                            PerkRow("Honorable Standing", "+30% Alliance Dividends & +25% Respect Gains", CashGreen)
                            PerkRow("Civic Immunity", "Lower police heat & unlocks Fortune 500 mergers", CashGreen)
                        } else if (repScore <= -25) {
                            PerkRow("Ruthless Notoriety", "+35% Plunder on Raids & +20% Sabotage Success", Color(0xFFFF5252))
                            PerkRow("Fear Factor", "Extortion deals yield massive immediate payouts", Color(0xFFFF5252))
                        } else {
                            PerkRow("Pragmatic Neutrality", "Balanced trade tariffs & access to shadow + light deals", CyanTactical)
                        }
                    }
                }
            }
        }

        // Section Title: Dynamic Reputation Opportunities
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "REPUTATION-LOCKED CONTRACTS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            }
        }

        items(contracts) { contract ->
            val isEligible = repScore in contract.requiredRepMin..contract.requiredRepMax
            ReputationContractCard(
                contract = contract,
                isEligible = isEligible,
                onExecute = { onExecuteContract(contract.id) }
            )
        }
    }
}

@Composable
private fun PerkRow(title: String, desc: String, color: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GeoTextPrimary)
            Text(desc, fontSize = 10.sp, color = GeoTextSecondary)
        }
    }
}

@Composable
private fun ReputationContractCard(
    contract: ReputationContractEntity,
    isEligible: Boolean,
    onExecute: () -> Unit
) {
    val categoryColor = when (contract.category) {
        "Honorable Deal" -> CashGreen
        "Ruthless Takeover" -> Color(0xFFFF5252)
        "Shadow Extortion" -> PowerPurple
        else -> CyanTactical
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = if (isEligible) GeoSurface else GeoSurfaceSubtle),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isEligible) categoryColor.copy(alpha = 0.5f) else GeoBorderSubtle,
                RoundedCornerShape(14.dp)
            )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(categoryColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = contract.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = categoryColor
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isEligible) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isEligible) CashGreen else GeoTextTertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Req: ${contract.requiredRepMin} to ${contract.requiredRepMax} REP",
                        fontSize = 10.sp,
                        color = if (isEligible) GeoTextSecondary else GeoTextTertiary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = contract.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GeoTextPrimary
            )
            Text(
                text = contract.description,
                fontSize = 11.sp,
                color = GeoTextSecondary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rewards Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("+$${formatCurrency(contract.rewardCash)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CashGreen)
                    Text("+${contract.rewardRespect} REP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    val shiftText = if (contract.reputationShift > 0) "+${contract.reputationShift} REP Align" else "${contract.reputationShift} REP Align"
                    Text(shiftText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (contract.reputationShift > 0) CashGreen else Color(0xFFFF5252))
                }

                Button(
                    onClick = onExecute,
                    enabled = isEligible,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = categoryColor,
                        contentColor = Color(0xFF1C1B1F),
                        disabledContainerColor = GeoSurfaceElevated,
                        disabledContentColor = GeoTextTertiary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("execute_contract_${contract.id}")
                ) {
                    Text(text = "Execute", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------
// TAB 2: ALLIANCES & JOINT VENTURES
// ---------------------------------------------------------------------------------

@Composable
private fun AlliancesTabContent(
    profile: PlayerProfileEntity?,
    alliances: List<AllianceEntity>,
    jointVentures: List<JointVentureEntity>,
    onFormAlliance: (String) -> Unit,
    onLeaveAlliance: (String) -> Unit,
    onSendAllianceAid: (String) -> Unit,
    onBetrayAlliance: (String) -> Unit,
    onFundJointVenture: (String) -> Unit
) {
    val currentAlliance = alliances.find { it.id == profile?.allianceId && it.isAllied }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Active Alliance Banner
        item {
            if (currentAlliance != null) {
                ActiveAllianceCard(
                    alliance = currentAlliance,
                    onLeave = { onLeaveAlliance(currentAlliance.id) },
                    onSendAid = { onSendAllianceAid(currentAlliance.id) },
                    onBetray = { onBetrayAlliance(currentAlliance.id) }
                )
            } else {
                IndependentStatusCard()
            }
        }

        // Joint Ventures Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CO-OP JOINT VENTURES",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            }
        }

        items(jointVentures) { jv ->
            JointVentureCard(
                jv = jv,
                playerCash = profile?.cash ?: 0.0,
                onFund = { onFundJointVenture(jv.id) }
            )
        }

        // Available Alliances to Join / Form
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Handshake, contentDescription = null, tint = CyanTactical, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SYNDICATE ALLIANCE COALITIONS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            }
        }

        items(alliances) { alliance ->
            AllianceRosterCard(
                alliance = alliance,
                isCurrentAlly = alliance.id == profile?.allianceId && alliance.isAllied,
                onJoin = { onFormAlliance(alliance.id) }
            )
        }
    }
}

@Composable
private fun ActiveAllianceCard(
    alliance: AllianceEntity,
    onLeave: () -> Unit,
    onSendAid: () -> Unit,
    onBetray: () -> Unit
) {
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
                            .background(GoldPrimary.copy(alpha = 0.2f))
                            .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Handshake, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("ACTIVE ALLIANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                        Text(alliance.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = GeoTextPrimary)
                        Text("Leader: ${alliance.leader} • ${alliance.pactType}", fontSize = 11.sp, color = GeoTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Trust Score Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Alliance Trust Level", fontSize = 11.sp, color = GeoTextSecondary)
                    Text("${alliance.trustScore}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CashGreen)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { alliance.trustScore / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = CashGreen,
                    trackColor = GeoSurfaceElevated
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dividends summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(GeoSurfaceElevated)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Dividend Rate", fontSize = 10.sp, color = GeoTextTertiary)
                    Text("+$${alliance.dividendPerSec.toInt()}/s", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CashGreen)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Turf Defense", fontSize = 10.sp, color = GeoTextTertiary)
                    Text("+${alliance.defenseBonus} DEF", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanTactical)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Syndicate Size", fontSize = 10.sp, color = GeoTextTertiary)
                    Text("${alliance.totalMembers} Clans", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GeoTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSendAid,
                    colors = ButtonDefaults.buttonColors(containerColor = CashGreen, contentColor = Color(0xFF1C1B1F)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("send_alliance_aid_btn")
                ) {
                    Text("Send Aid ($5K)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onBetray,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(Color(0xFFFF5252), Color(0xFFFF5252)))),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("betray_alliance_btn")
                ) {
                    Text("Betray Ally", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onLeave,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GeoTextTertiary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(0.7f).testTag("leave_alliance_btn")
                ) {
                    Text("Sever", fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun IndependentStatusCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = GeoSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GeoBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GeoSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Public, contentDescription = null, tint = CyanTactical, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("STATUS: INDEPENDENT CARTEL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanTactical)
                Text("You are not currently allied with any underworld syndicate.", fontSize = 12.sp, color = GeoTextSecondary)
                Text("Form a pact below to receive mutual defense and shared dividends.", fontSize = 10.sp, color = GeoTextTertiary)
            }
        }
    }
}

@Composable
private fun JointVentureCard(
    jv: JointVentureEntity,
    playerCash: Double,
    onFund: () -> Unit
) {
    val isAffordable = playerCash >= jv.costCash

    Card(
        colors = CardDefaults.cardColors(containerColor = GeoSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (jv.isFunded) GoldPrimary.copy(alpha = 0.6f) else GeoBorderSubtle, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = jv.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )

                if (jv.isFunded) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldPrimary)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("ACTIVE ASSET", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF231B00))
                    }
                }
            }

            Text(
                text = jv.description,
                fontSize = 11.sp,
                color = GeoTextSecondary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("+$${jv.payoutPerSec.toInt()}/s", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CashGreen)
                    Text("+${jv.respectBonus} REP/hr", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    Text("+${jv.powerBonus} PWR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PowerPurple)
                }

                if (!jv.isFunded) {
                    Button(
                        onClick = onFund,
                        enabled = isAffordable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color(0xFF231B00),
                            disabledContainerColor = GeoSurfaceElevated,
                            disabledContentColor = GeoTextTertiary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("fund_jv_${jv.id}")
                    ) {
                        Text("Fund $${formatCurrency(jv.costCash)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AllianceRosterCard(
    alliance: AllianceEntity,
    isCurrentAlly: Boolean,
    onJoin: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceElevated),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isCurrentAlly) GoldPrimary else GeoBorderSubtle, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(alliance.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GeoTextPrimary)
                    Text("Leader: ${alliance.leader} • ${alliance.pactType}", fontSize = 11.sp, color = CyanTactical)
                }

                if (isCurrentAlly) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(CashGreen)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("ACTIVE ALLY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1B1F))
                    }
                } else {
                    Button(
                        onClick = onJoin,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanTactical, contentColor = Color(0xFF1C1B1F)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("propose_alliance_${alliance.id}")
                    ) {
                        Text("Sign Pact", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(alliance.description, fontSize = 11.sp, color = GeoTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Base Dividend: +$${alliance.dividendPerSec.toInt()}/s", fontSize = 10.sp, color = CashGreen, fontWeight = FontWeight.Medium)
                Text("Defense Bonus: +${alliance.defenseBonus}", fontSize = 10.sp, color = CyanTactical, fontWeight = FontWeight.Medium)
                Text("Trust: ${alliance.trustScore}%", fontSize = 10.sp, color = GoldPrimary, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ---------------------------------------------------------------------------------
// TAB 3: RIVALRIES & DIRECT SABOTAGE OPS
// ---------------------------------------------------------------------------------

@Composable
private fun RivalriesTabContent(
    profile: PlayerProfileEntity?,
    rivals: List<RivalSyndicateEntity>,
    onDeclareRivalry: (String) -> Unit,
    onSabotageRival: (String, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = GeoSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFF5252).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("TACTICAL WARFARE & SABOTAGE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                        Text("Declare rivalries to enable direct sabotage strikes and earn +50% plunder rewards in turf raids.", fontSize = 11.sp, color = GeoTextSecondary)
                    }
                }
            }
        }

        items(rivals) { rival ->
            RivalSyndicateTacticalCard(
                rival = rival,
                playerCash = profile?.cash ?: 0.0,
                onDeclareRivalry = { onDeclareRivalry(rival.id) },
                onSabotage = { sabotageType -> onSabotageRival(rival.id, sabotageType) }
            )
        }
    }
}

@Composable
private fun RivalSyndicateTacticalCard(
    rival: RivalSyndicateEntity,
    playerCash: Double,
    onDeclareRivalry: () -> Unit,
    onSabotage: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceElevated),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (rival.isDeclaredRival) Color(0xFFFF5252) else GeoBorderSubtle,
                RoundedCornerShape(14.dp)
            )
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (rival.isDeclaredRival) Color(0xFFFF5252).copy(alpha = 0.2f) else GeoSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (rival.isDeclaredRival) Icons.Default.Warning else Icons.Default.Security,
                            contentDescription = null,
                            tint = if (rival.isDeclaredRival) Color(0xFFFF5252) else GeoTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(rival.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = GeoTextPrimary)
                            if (rival.isDeclaredRival) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFF5252))
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text("RIVAL", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }
                        }
                        Text("Boss: ${rival.leader} • ${rival.territory}", fontSize = 11.sp, color = GeoTextSecondary)
                    }
                }

                Button(
                    onClick = onDeclareRivalry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rival.isDeclaredRival) Color(0xFFFF5252) else GeoSurface,
                        contentColor = if (rival.isDeclaredRival) Color.White else GeoTextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("declare_rival_${rival.id}")
                ) {
                    Text(
                        text = if (rival.isDeclaredRival) "Declared" else "Declare Rival",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Defense Power: ${rival.power}", fontSize = 11.sp, color = PowerPurple, fontWeight = FontWeight.Bold)
                Text("Respect: ${rival.respect}", fontSize = 11.sp, color = GoldPrimary, fontWeight = FontWeight.Bold)
                Text("Bounty: $${formatCurrency(rival.bounty)}", fontSize = 11.sp, color = CashGreen, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable Sabotage Terminal
            Button(
                onClick = { isExpanded = !isExpanded },
                colors = ButtonDefaults.buttonColors(containerColor = GeoSurface),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("toggle_sabotage_${rival.id}")
            ) {
                Text(
                    text = if (isExpanded) "Hide Sabotage Operations" else "Launch Sabotage Operations",
                    fontSize = 11.sp,
                    color = CyanTactical,
                    fontWeight = FontWeight.Bold
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SabotageOpButton(
                        title = "EMP Blackout Grid ($4,000)",
                        desc = "Disables their top facility & siphons quick cash.",
                        onClick = { onSabotage("EMP_BLACKOUT") }
                    )
                    SabotageOpButton(
                        title = "Supply Chain Hijack ($8,000)",
                        desc = "Intercepts shipment cargo for high respect and cash loot.",
                        onClick = { onSabotage("SUPPLY_CHAIN") }
                    )
                    SabotageOpButton(
                        title = "Smear Campaign ($12,000)",
                        desc = "Leaks intel to permanently weaken their defense power by 15%.",
                        onClick = { onSabotage("SMEAR_CAMPAIGN") }
                    )
                    SabotageOpButton(
                        title = "Corporate Espionage ($18,000)",
                        desc = "Breaches their vault and maps enemy vulnerabilities.",
                        onClick = { onSabotage("CORPORATE_ESPIONAGE") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SabotageOpButton(
    title: String,
    desc: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GeoSurface)
            .border(1.dp, GeoBorderSubtle, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                Text(desc, fontSize = 10.sp, color = GeoTextSecondary)
            }
            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
        }
    }
}
