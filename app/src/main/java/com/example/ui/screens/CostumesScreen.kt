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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.data.models.BossCostume
import com.example.data.models.CostumeCatalog
import com.example.data.models.CostumeRarity
import com.example.data.models.CostumeUnlockType
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
fun CostumesScreen(
    profile: PlayerProfileEntity?,
    onEquipCostume: (String) -> Unit,
    onUnlockCostume: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val unlockedList = remember(profile?.unlockedCostumes) {
        profile?.unlockedCostumes?.split(",")?.map { it.trim() } ?: listOf("costume_classic_capo")
    }
    val equippedId = profile?.equippedCostumeId ?: "costume_classic_capo"
    val equippedCostume = remember(equippedId) {
        CostumeCatalog.getCostumeById(equippedId)
    }

    var selectedCostumeForDetail by remember { mutableStateOf<BossCostume?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .testTag("costumes_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Active Wardrobe Showcase
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(equippedCostume.rarity.color, GoldPrimary)
                        ),
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
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                equippedCostume.rarity.color.copy(alpha = 0.8f),
                                                GeoSurfaceHighlight
                                            )
                                        )
                                    )
                                    .border(2.dp, equippedCostume.rarity.color, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = equippedCostume.icon,
                                    contentDescription = equippedCostume.name,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = equippedCostume.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = GeoTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    RarityChip(rarity = equippedCostume.rarity)
                                }
                                Text(
                                    text = "Active Boss Outfit • ${equippedCostume.title}",
                                    fontSize = 12.sp,
                                    color = GeoTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Active Buffs Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BuffBadge(
                            label = "Cash Rate",
                            value = "+${((equippedCostume.cashMultiplier - 1f) * 100).toInt()}%",
                            color = CashGreen,
                            modifier = Modifier.weight(1f)
                        )
                        BuffBadge(
                            label = "Power",
                            value = "+${((equippedCostume.powerMultiplier - 1f) * 100).toInt()}%",
                            color = CyanTactical,
                            modifier = Modifier.weight(1f)
                        )
                        BuffBadge(
                            label = "Respect",
                            value = "+${((equippedCostume.respectMultiplier - 1f) * 100).toInt()}%",
                            color = GoldPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (equippedCostume.duelAttackBonus > 0 || equippedCostume.raidDefenseBonus > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (equippedCostume.duelAttackBonus > 0) {
                                BuffBadge(
                                    label = "Duel Clash",
                                    value = "+${equippedCostume.duelAttackBonus} PWR",
                                    color = PowerPurple,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (equippedCostume.raidDefenseBonus > 0) {
                                BuffBadge(
                                    label = "Raid Defense",
                                    value = "+${equippedCostume.raidDefenseBonus} DEF",
                                    color = CyanTactical,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Checkroom,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Syndicate Boss Wardrobe",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )
                }
                Text(
                    text = "${unlockedList.size} / ${CostumeCatalog.allCostumes.size} Unlocked",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary
                )
            }
        }

        // Costumes list
        items(CostumeCatalog.allCostumes, key = { it.id }) { costume ->
            val isUnlocked = unlockedList.contains(costume.id)
            val isEquipped = costume.id == equippedId

            CostumeItemCard(
                costume = costume,
                isUnlocked = isUnlocked,
                isEquipped = isEquipped,
                profile = profile,
                onEquip = { onEquipCostume(costume.id) },
                onUnlock = { onUnlockCostume(costume.id) },
                onInspect = { selectedCostumeForDetail = costume }
            )
        }
    }

    // Costume Lore & Details Dialog
    selectedCostumeForDetail?.let { costume ->
        CostumeDetailDialog(
            costume = costume,
            isUnlocked = unlockedList.contains(costume.id),
            isEquipped = costume.id == equippedId,
            profile = profile,
            onEquip = {
                onEquipCostume(costume.id)
                selectedCostumeForDetail = null
            },
            onUnlock = {
                onUnlockCostume(costume.id)
                selectedCostumeForDetail = null
            },
            onDismiss = { selectedCostumeForDetail = null }
        )
    }
}

@Composable
private fun CostumeItemCard(
    costume: BossCostume,
    isUnlocked: Boolean,
    isEquipped: Boolean,
    profile: PlayerProfileEntity?,
    onEquip: () -> Unit,
    onUnlock: () -> Unit,
    onInspect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isEquipped) costume.rarity.color else GeoBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable { onInspect() }
            .testTag("costume_card_${costume.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isEquipped) GeoSurfaceHighlight else GeoSurfaceElevated
        )
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
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            if (isUnlocked) costume.rarity.color.copy(alpha = 0.2f) else GeoSurfaceSubtle
                        )
                        .border(
                            1.5.dp,
                            if (isUnlocked) costume.rarity.color else GeoBorderSubtle,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = costume.icon,
                        contentDescription = costume.name,
                        tint = if (isUnlocked) costume.rarity.color else GeoTextTertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = costume.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = GeoTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        RarityChip(rarity = costume.rarity)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Cash +${((costume.cashMultiplier - 1f) * 100).toInt()}% • Pwr +${((costume.powerMultiplier - 1f) * 100).toInt()}% • Rep +${((costume.respectMultiplier - 1f) * 100).toInt()}%",
                        fontSize = 11.sp,
                        color = if (isUnlocked) CashGreen else GeoTextTertiary,
                        fontWeight = FontWeight.Medium
                    )

                    if (!isUnlocked) {
                        val requirementText = when (costume.unlockType) {
                            CostumeUnlockType.CASH -> "Cost: ${formatCurrency(costume.costCash)}"
                            CostumeUnlockType.RESPECT -> "Cost: ${formatNumber(costume.costRespect)} Respect"
                            CostumeUnlockType.PRESTIGE_TIER -> "Requires Rebirth Tier ${costume.requiredPrestigeLevel}"
                            CostumeUnlockType.PRESTIGE_TOKENS -> "Cost: ${costume.costTokens} Tokens"
                            else -> "Special Unlock"
                        }
                        Text(
                            text = requirementText,
                            fontSize = 11.sp,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            when {
                isEquipped -> {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(GoldPrimary)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "EQUIPPED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF231B00)
                        )
                    }
                }
                isUnlocked -> {
                    Button(
                        onClick = onEquip,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanTactical),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("equip_btn_${costume.id}")
                    ) {
                        Text(
                            text = "EQUIP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00363A)
                        )
                    }
                }
                else -> {
                    Button(
                        onClick = onUnlock,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("unlock_btn_${costume.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF231B00),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "UNLOCK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF231B00)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RarityChip(rarity: CostumeRarity) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(rarity.color.copy(alpha = 0.2f))
            .border(0.8.dp, rarity.color, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = rarity.label.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = rarity.color
        )
    }
}

@Composable
private fun BuffBadge(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(GeoSurfaceSubtle)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(vertical = 6.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = label,
                fontSize = 9.sp,
                color = GeoTextSecondary
            )
        }
    }
}

@Composable
private fun CostumeDetailDialog(
    costume: BossCostume,
    isUnlocked: Boolean,
    isEquipped: Boolean,
    profile: PlayerProfileEntity?,
    onEquip: () -> Unit,
    onUnlock: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(costume.rarity.color.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = costume.icon,
                        contentDescription = costume.name,
                        tint = costume.rarity.color,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = costume.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )
                    RarityChip(rarity = costume.rarity)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = costume.description,
                    fontSize = 13.sp,
                    color = GeoTextPrimary
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = GeoSurfaceSubtle
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "SYNDICATE LORE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = costume.lore,
                            fontSize = 11.sp,
                            color = GeoTextSecondary
                        )
                    }
                }

                Text(
                    text = "PASSIVE STAT BONUSES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanTactical
                )
                Text(text = "• Cash Production Multiplier: +${((costume.cashMultiplier - 1f) * 100).toInt()}%", fontSize = 12.sp, color = CashGreen)
                Text(text = "• Syndicate Power Multiplier: +${((costume.powerMultiplier - 1f) * 100).toInt()}%", fontSize = 12.sp, color = CyanTactical)
                Text(text = "• Respect Growth Multiplier: +${((costume.respectMultiplier - 1f) * 100).toInt()}%", fontSize = 12.sp, color = GoldPrimary)
                if (costume.duelAttackBonus > 0) {
                    Text(text = "• 2P Duel Combat Power: +${costume.duelAttackBonus} PWR", fontSize = 12.sp, color = PowerPurple)
                }
                if (costume.raidDefenseBonus > 0) {
                    Text(text = "• Turf Raid Defense: +${costume.raidDefenseBonus} DEF", fontSize = 12.sp, color = CyanTactical)
                }
            }
        },
        confirmButton = {
            if (isUnlocked && !isEquipped) {
                Button(
                    onClick = onEquip,
                    colors = ButtonDefaults.buttonColors(containerColor = CyanTactical)
                ) {
                    Text("EQUIP OUTFIT", fontWeight = FontWeight.Bold, color = Color(0xFF00363A))
                }
            } else if (!isUnlocked) {
                Button(
                    onClick = onUnlock,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("UNLOCK NOW", fontWeight = FontWeight.Bold, color = Color(0xFF231B00))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CLOSE", color = GeoTextSecondary)
            }
        },
        containerColor = GeoSurfaceElevated
    )
}
