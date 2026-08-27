package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.models.RebirthConfig
import com.example.data.models.RebirthPerk
import com.example.data.models.RebirthPerksCatalog
import com.example.ui.components.formatCurrency
import com.example.ui.components.formatNumber
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CyanTactical
import com.example.ui.theme.GeoBackground
import com.example.ui.theme.GeoBorder
import com.example.ui.theme.GeoBorderSubtle
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
fun RebirthAscensionScreen(
    profile: PlayerProfileEntity?,
    onPerformRebirth: () -> Unit,
    onUpgradePerk: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentPrestige = profile?.prestigeLevel ?: 0
    val nextTier = remember(currentPrestige) {
        RebirthConfig.getNextRebirthTier(currentPrestige)
    }

    val playerCash = profile?.cash ?: 0.0
    val playerRespect = profile?.respect ?: 0L

    val cashProgress = (playerCash / nextTier.requiredCash).toFloat().coerceIn(0f, 1f)
    val respectProgress = (playerRespect.toFloat() / nextTier.requiredRespect.toFloat()).coerceIn(0f, 1f)
    val isReadyToRebirth = playerCash >= nextTier.requiredCash && playerRespect >= nextTier.requiredRespect

    var showRebirthConfirmDialog by remember { mutableStateOf(false) }

    val perksList = remember(profile) {
        RebirthPerksCatalog.createPerkList(
            bankLevel = profile?.rebirthPerkBankLevel ?: 0,
            powerLevel = profile?.rebirthPerkPowerLevel ?: 0,
            respectLevel = profile?.rebirthPerkRespectLevel ?: 0,
            costReductionLevel = profile?.rebirthPerkCostReductionLevel ?: 0,
            heritageCashLevel = profile?.rebirthPerkHeritageLevel ?: 0,
            autoManagerLevel = profile?.rebirthPerkAutoManagerLevel ?: 0
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .testTag("rebirth_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Rebirth Ascension Chamber Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(GoldPrimary, PowerPurple)),
                        RoundedCornerShape(16.dp)
                    ),
                color = GeoSurfaceElevated,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(GoldPrimary, Color(0xFFB45309))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = null,
                                    tint = Color(0xFF231B00),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = nextTier.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "Current: Level $currentPrestige (${profile?.title ?: "Street Capo"})",
                                    fontSize = 12.sp,
                                    color = CyanTactical,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Prestige Tokens Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(GeoSurfaceHighlight)
                                .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${profile?.prestigeTokens ?: 0} Tokens",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GoldPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = nextTier.description,
                        fontSize = 12.sp,
                        color = GeoTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bars
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Cash Required:", fontSize = 11.sp, color = GeoTextSecondary)
                                Text(
                                    text = "${formatCurrency(playerCash)} / ${formatCurrency(nextTier.requiredCash)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (playerCash >= nextTier.requiredCash) CashGreen else GeoTextTertiary
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { cashProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CashGreen,
                                trackColor = GeoSurfaceSubtle
                            )
                        }

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Respect Required:", fontSize = 11.sp, color = GeoTextSecondary)
                                Text(
                                    text = "${formatNumber(playerRespect)} / ${formatNumber(nextTier.requiredRespect)} REP",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (playerRespect >= nextTier.requiredRespect) GoldPrimary else GeoTextTertiary
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            LinearProgressIndicator(
                                progress = { respectProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = GoldPrimary,
                                trackColor = GeoSurfaceSubtle
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rebirth Rewards summary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GeoSurfaceSubtle)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+${nextTier.tokensAwarded}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = GoldPrimary)
                            Text(text = "Rebirth Tokens", fontSize = 10.sp, color = GeoTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+${nextTier.globalMultiplierBonus}%", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CashGreen)
                            Text(text = "Global Multiplier", fontSize = 10.sp, color = GeoTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = formatCurrency(nextTier.startingBonusCash), fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyanTactical)
                            Text(text = "Starting Cash", fontSize = 10.sp, color = GeoTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showRebirthConfirmDialog = true },
                        enabled = isReadyToRebirth,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            disabledContainerColor = GeoSurfaceSubtle
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("rebirth_ascend_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (isReadyToRebirth) Color(0xFF231B00) else GeoTextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isReadyToRebirth) "ASCEND & REBIRTH NOW" else "REQUIREMENTS NOT MET",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (isReadyToRebirth) Color(0xFF231B00) else GeoTextTertiary
                        )
                    }
                }
            }
        }

        // Section Title: Permanent Rebirth Skill Tree
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Permanent Rebirth Perks",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )
                }
                Text(
                    text = "Permanent Across Rebirths",
                    fontSize = 11.sp,
                    color = GeoTextSecondary
                )
            }
        }

        // Perks List
        items(perksList, key = { it.id }) { perk ->
            RebirthPerkCard(
                perk = perk,
                availableTokens = profile?.prestigeTokens ?: 0,
                onUpgrade = { onUpgradePerk(perk.id) }
            )
        }
    }

    // Confirmation Rebirth Dialog
    if (showRebirthConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showRebirthConfirmDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirm Syndicate Rebirth",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Ascending will reset your temporary business levels and district controls, but unlocks immense permanent underworld supremacy:",
                        fontSize = 12.sp,
                        color = GeoTextSecondary
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = GeoSurfaceSubtle,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "YOU KEEP & GAIN:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CashGreen)
                            Text(text = "• +${nextTier.tokensAwarded} Rebirth Tokens", fontSize = 11.sp, color = GoldPrimary)
                            Text(text = "• Permanent +${nextTier.globalMultiplierBonus}% Global Multiplier", fontSize = 11.sp, color = CashGreen)
                            Text(text = "• All Unlocked Costumes & Outfits", fontSize = 11.sp, color = CyanTactical)
                            Text(text = "• All Permanent Rebirth Perk Upgrades", fontSize = 11.sp, color = PowerPurple)
                            Text(text = "• Starting Package: ${formatCurrency(nextTier.startingBonusCash)} + ${formatNumber(nextTier.startingBonusRespect)} REP", fontSize = 11.sp, color = GeoTextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRebirthConfirmDialog = false
                        onPerformRebirth()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("YES, ASCEND NOW", fontWeight = FontWeight.Black, color = Color(0xFF231B00))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRebirthConfirmDialog = false }) {
                    Text("CANCEL", color = GeoTextSecondary)
                }
            },
            containerColor = GeoSurfaceElevated
        )
    }
}

@Composable
private fun RebirthPerkCard(
    perk: RebirthPerk,
    availableTokens: Int,
    onUpgrade: () -> Unit
) {
    val isMaxed = perk.currentLevel >= perk.maxLevel
    val canAfford = availableTokens >= perk.tokenCostPerLevel && !isMaxed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, GeoBorder, RoundedCornerShape(14.dp))
            .testTag("perk_card_${perk.id}"),
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceElevated)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(perk.iconColor.copy(alpha = 0.2f))
                        .border(1.dp, perk.iconColor, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = perk.icon,
                        contentDescription = perk.name,
                        tint = perk.iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = perk.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isMaxed) GoldPrimary else GeoSurfaceSubtle)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = if (isMaxed) "MAX" else "LVL ${perk.currentLevel}/${perk.maxLevel}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMaxed) Color(0xFF231B00) else GeoTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = perk.description,
                        fontSize = 11.sp,
                        color = GeoTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isMaxed) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GeoSurfaceSubtle)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Maxed",
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                Button(
                    onClick = onUpgrade,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        disabledContainerColor = GeoSurfaceSubtle
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("upgrade_perk_${perk.id}")
                ) {
                    Text(
                        text = "${perk.tokenCostPerLevel} ✦",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = if (canAfford) Color(0xFF231B00) else GeoTextTertiary
                    )
                }
            }
        }
    }
}
