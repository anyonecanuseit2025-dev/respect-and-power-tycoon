package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PlayerProfileEntity
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CyanTactical
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
import java.util.Locale

import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.IconButton
import androidx.compose.foundation.clickable

import com.example.data.models.CostumeCatalog

fun formatCurrency(amount: Double): String {
    return when {
        amount >= 1_000_000_000_000.0 -> String.format(Locale.US, "$%.2fT", amount / 1_000_000_000_000.0)
        amount >= 1_000_000_000.0 -> String.format(Locale.US, "$%.2fB", amount / 1_000_000_000.0)
        amount >= 1_000_000.0 -> String.format(Locale.US, "$%.2fM", amount / 1_000_000.0)
        amount >= 1_000.0 -> String.format(Locale.US, "$%.1fK", amount / 1_000.0)
        else -> String.format(Locale.US, "$%.0f", amount)
    }
}

fun formatNumber(amount: Long): String {
    return when {
        amount >= 1_000_000_000 -> String.format(Locale.US, "%.2fB", amount / 1_000_000_000.0)
        amount >= 1_000_000 -> String.format(Locale.US, "%.2fM", amount / 1_000_000.0)
        amount >= 1_000 -> String.format(Locale.US, "%.1fK", amount / 1_000.0)
        else -> amount.toString()
    }
}

@Composable
fun HeaderStatusBar(
    profile: PlayerProfileEntity?,
    cashRate: Double,
    respectRate: Double,
    powerRate: Double,
    onOpenStory: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = GeoSurface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Top Row: Player Info, Story & Credits, Prestige
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val equippedCostume = CostumeCatalog.getCostumeById(profile?.equippedCostumeId ?: "costume_classic_capo")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onOpenStory() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(equippedCostume.rarity.color.copy(alpha = 0.8f), GoldDark)
                                )
                            )
                            .border(1.5.dp, equippedCostume.rarity.color, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = equippedCostume.icon,
                            contentDescription = equippedCostume.name,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile?.name ?: "Don Valentino",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextPrimary
                            )
                            if ((profile?.prestigeLevel ?: 0) > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldPrimary)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "R${profile?.prestigeLevel}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF231B00)
                                    )
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${equippedCostume.name} • ${profile?.clanName ?: "Apex Syndicate"}",
                                fontSize = 11.sp,
                                color = CyanTactical,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Story & Credits button + Global Rank & Reputation Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GeoSurfaceHighlight)
                            .border(1.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                            .clickable { onOpenStory() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Story and Credits",
                                tint = GoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Story",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(GeoSurfaceElevated)
                                .border(1.dp, GeoBorder, RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rank",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "#${profile?.globalRank ?: 7} ${profile?.rankTier ?: "Capo"}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        val repScore = profile?.reputation ?: 10
                        val repColor = when {
                            repScore >= 25 -> CashGreen
                            repScore <= -25 -> Color(0xFFFF5252)
                            else -> CyanTactical
                        }
                        val repLabel = when {
                            repScore >= 25 -> "Honorable"
                            repScore <= -25 -> "Ruthless"
                            else -> "Pragmatic"
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(repColor.copy(alpha = 0.15f))
                                .border(0.8.dp, repColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$repLabel (${if (repScore > 0) "+" else ""}$repScore)",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = repColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Resource Metrics Bar: Cash | Respect | Power
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResourceStatCard(
                    icon = Icons.Default.AttachMoney,
                    iconColor = CashGreen,
                    value = formatCurrency(profile?.cash ?: 0.0),
                    rate = "+${formatCurrency(cashRate)}/s",
                    label = "Cash",
                    modifier = Modifier.weight(1f)
                )
                ResourceStatCard(
                    icon = Icons.Default.Star,
                    iconColor = GoldPrimary,
                    value = formatNumber(profile?.respect ?: 0),
                    rate = "+${formatNumber(respectRate.toLong())}/s",
                    label = "Respect",
                    modifier = Modifier.weight(1f)
                )
                ResourceStatCard(
                    icon = Icons.Default.FlashOn,
                    iconColor = PowerPurple,
                    value = formatNumber(profile?.power ?: 0),
                    rate = "+${formatNumber(powerRate.toLong())}/s",
                    label = "Power",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ResourceStatCard(
    icon: ImageVector,
    iconColor: Color,
    value: String,
    rate: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GeoSurfaceElevated)
            .border(1.dp, GeoBorderSubtle, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = rate,
                    fontSize = 10.sp,
                    color = iconColor.copy(alpha = 0.9f),
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GeoTextPrimary,
                maxLines = 1
            )
            Text(
                text = label,
                fontSize = 9.sp,
                color = GeoTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
