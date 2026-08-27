package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PowerPurple
import com.example.viewmodel.DuelCombatCard
import com.example.viewmodel.DuelPlayer
import com.example.viewmodel.DuelSector
import com.example.viewmodel.MultiplayerDuelState

@Composable
fun PassAndPlayDuelScreen(
    duelState: MultiplayerDuelState,
    onStartNewDuel: () -> Unit,
    onSelectSector: (Int) -> Unit,
    onPlayCard: (DuelCombatCard) -> Unit,
    onEndTurn: () -> Unit,
    onConfirmPhonePassed: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!duelState.isGameActive) {
        DuelLobbyView(onStart = onStartNewDuel, modifier = modifier)
    } else if (duelState.isPassingPhone) {
        PassPhonePrivacyView(
            nextPlayer = duelState.activePlayer,
            onReady = onConfirmPhonePassed,
            modifier = modifier
        )
    } else if (duelState.isMatchFinished) {
        DuelVictoryView(
            duelState = duelState,
            onPlayAgain = onStartNewDuel,
            modifier = modifier
        )
    } else {
        ActiveDuelGameView(
            duelState = duelState,
            onSelectSector = onSelectSector,
            onPlayCard = onPlayCard,
            onEndTurn = onEndTurn,
            modifier = modifier
        )
    }
}

@Composable
private fun DuelLobbyView(onStart: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(listOf(CrimsonAlert, GoldPrimary))
                    )
                    .border(2.dp, GoldPrimary, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = Color(0xFF231B00),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "2-PLAYER MULTIPLAYER DUEL",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = GoldPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Pass & Play Syndicate Territory Clash",
                fontSize = 13.sp,
                color = CyanTactical,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, GeoBorder, RoundedCornerShape(14.dp)),
                color = GeoSurfaceElevated
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HOW TO PLAY ON ONE DEVICE:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Player 1 (The Don) vs Player 2 (The Baron) compete over 5 key metropolis districts.\n2. Spend 3 Action Points each round deploying Enforcers, Bribery, EMP Sabotage, or Hostile Takeovers.\n3. Pass the phone after each turn. After 5 rounds, the syndicate with the highest power, cash & respect rules the city!",
                        fontSize = 12.sp,
                        color = GeoTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color(0xFF231B00)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_multiplayer_duel_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "START 2-PLAYER SYNDICATE CLASH",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun ActiveDuelGameView(
    duelState: MultiplayerDuelState,
    onSelectSector: (Int) -> Unit,
    onPlayCard: (DuelCombatCard) -> Unit,
    onEndTurn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activePlayer = duelState.activePlayer
    val isP1 = activePlayer == DuelPlayer.PLAYER_ONE
    val activeColor = if (isP1) CrimsonAlert else GoldPrimary
    val currentAp = if (isP1) duelState.p1ActionPoints else duelState.p2ActionPoints
    val cards = MultiplayerDuelState.getAvailableCards()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Active Player Header
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.5.dp, activeColor, RoundedCornerShape(14.dp)),
                color = GeoSurfaceElevated
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "ROUND ${duelState.currentRound} OF ${duelState.maxRounds}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = CyanTactical,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${activePlayer.displayName} (${activePlayer.syndicateName})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = activeColor
                            )
                        }

                        // Action Points Pills
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AP: ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextPrimary
                            )
                            repeat(3) { index ->
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(if (index < currentAp) CyanTactical else GeoSurfaceHighlight)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = duelState.lastClashLog,
                        fontSize = 11.sp,
                        color = GeoTextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        // Section Title: Contested Sectors
        item {
            Text(
                text = "CONTESTED CITY SECTORS (TAP TO TARGET)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
        }

        // 5 Sectors List
        items(duelState.sectors, key = { it.id }) { sector ->
            val isSelected = sector.id == duelState.selectedSectorId
            DuelSectorCard(
                sector = sector,
                isSelected = isSelected,
                onSelect = { onSelectSector(sector.id) }
            )
        }

        // Section Title: Tactical Cards
        item {
            Text(
                text = "TACTICAL MOVES (SPEND AP)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = CyanTactical,
                letterSpacing = 1.sp
            )
        }

        // Cards Deck Grid / Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                cards.chunked(2).forEach { pair ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        pair.forEach { card ->
                            val canPlay = currentAp >= card.apCost
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        1.dp,
                                        if (canPlay) CyanTactical.copy(alpha = 0.5f) else GeoBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable(enabled = canPlay) { onPlayCard(card) },
                                color = if (canPlay) GeoSurfaceHighlight else GeoSurfaceSubtle
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = card.title,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (canPlay) GeoTextPrimary else GeoTextTertiary
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(if (canPlay) CyanTactical else GeoSurfaceHighlight)
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = "${card.apCost} AP",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (canPlay) Color(0xFF00354E) else GeoTextTertiary
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = card.description,
                                        fontSize = 10.sp,
                                        color = GeoTextSecondary
                                    )
                                }
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // End Turn Button
        item {
            Button(
                onClick = onEndTurn,
                colors = ButtonDefaults.buttonColors(
                    containerColor = activeColor,
                    contentColor = if (isP1) Color(0xFF491216) else Color(0xFF231B00)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("end_duel_turn_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isP1) "END TURN & PASS TO PLAYER 2" else "SUBMIT TURN & RESOLVE CLASH",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun DuelSectorCard(
    sector: DuelSector,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val totalPower = (sector.p1Power + sector.p2Power).coerceAtLeast(1)
    val p1Pct = sector.p1Power.toFloat() / totalPower
    val p2Pct = sector.p2Power.toFloat() / totalPower

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.5.dp,
                if (isSelected) CyanTactical else GeoBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelect),
        color = GeoSurfaceElevated
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${sector.id}. ${sector.name}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )

                Text(
                    text = when (sector.controlledBy) {
                        DuelPlayer.PLAYER_ONE -> "Held by Don"
                        DuelPlayer.PLAYER_TWO -> "Held by Baron"
                        else -> "Contested"
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (sector.controlledBy) {
                        DuelPlayer.PLAYER_ONE -> CrimsonAlert
                        DuelPlayer.PLAYER_TWO -> GoldPrimary
                        else -> GeoTextSecondary
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Power Tug-of-War Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(GeoSurfaceSubtle)
            ) {
                Box(
                    modifier = Modifier
                        .weight(if (sector.p1Power == 0 && sector.p2Power == 0) 0.5f else p1Pct.coerceAtLeast(0.01f))
                        .fillMaxSize()
                        .background(CrimsonAlert)
                )
                Box(
                    modifier = Modifier
                        .weight(if (sector.p1Power == 0 && sector.p2Power == 0) 0.5f else p2Pct.coerceAtLeast(0.01f))
                        .fillMaxSize()
                        .background(GoldPrimary)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don PWR: ${sector.p1Power}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CrimsonAlert
                )
                Text(
                    text = "+$${sector.cashReward} / +${sector.respectReward} REP",
                    fontSize = 10.sp,
                    color = CashGreen
                )
                Text(
                    text = "Baron PWR: ${sector.p2Power}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            }
        }
    }
}

@Composable
private fun PassPhonePrivacyView(
    nextPlayer: DuelPlayer,
    onReady: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (nextPlayer == DuelPlayer.PLAYER_ONE) CrimsonAlert else GoldPrimary
    val contentTextColor = if (nextPlayer == DuelPlayer.PLAYER_ONE) Color(0xFF491216) else Color(0xFF231B00)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color.copy(alpha = 0.2f))
                    .border(2.dp, color, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "PASS PHONE TO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = GeoTextSecondary,
                letterSpacing = 2.sp
            )

            Text(
                text = nextPlayer.displayName.uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = color
            )

            Text(
                text = "(${nextPlayer.syndicateName})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CyanTactical
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Keep tactical decisions confidential! When ready, tap the button below to review sectors and place your orders.",
                fontSize = 12.sp,
                color = GeoTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onReady,
                colors = ButtonDefaults.buttonColors(
                    containerColor = color,
                    contentColor = contentTextColor
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_pass_phone_button")
            ) {
                Text(
                    text = "I AM READY, TAKE COMMAND",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
private fun DuelVictoryView(
    duelState: MultiplayerDuelState,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val winner = duelState.winner ?: DuelPlayer.PLAYER_ONE
    val winColor = if (winner == DuelPlayer.PLAYER_ONE) CrimsonAlert else GoldPrimary
    val p1Controlled = duelState.sectors.count { it.controlledBy == DuelPlayer.PLAYER_ONE }
    val p2Controlled = duelState.sectors.count { it.controlledBy == DuelPlayer.PLAYER_TWO }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GoldPrimary)
                    .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color(0xFF231B00),
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SUPREME SYNDICATE CORONATION",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )

            Text(
                text = "${winner.displayName} WINS!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = winColor
            )

            Text(
                text = "${winner.syndicateName} has seized absolute dominance of the metropolis!",
                fontSize = 12.sp,
                color = GeoTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Scoreboard Summary
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, GeoBorder, RoundedCornerShape(12.dp)),
                color = GeoSurfaceElevated
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "FINAL STANDINGS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CyanTactical
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(text = "Player 1 (The Don)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonAlert)
                            Text(text = "Cash: $${duelState.p1Cash}", fontSize = 11.sp, color = CashGreen)
                            Text(text = "Respect: ${duelState.p1Respect} REP", fontSize = 11.sp, color = GoldPrimary)
                            Text(text = "Sectors: $p1Controlled / 5", fontSize = 11.sp, color = GeoTextPrimary)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Player 2 (The Baron)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldPrimary)
                            Text(text = "Cash: $${duelState.p2Cash}", fontSize = 11.sp, color = CashGreen)
                            Text(text = "Respect: ${duelState.p2Respect} REP", fontSize = 11.sp, color = GoldPrimary)
                            Text(text = "Sectors: $p2Controlled / 5", fontSize = 11.sp, color = GeoTextPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onPlayAgain,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color(0xFF231B00)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "REMATCH TURF DUEL",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
