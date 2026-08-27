package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OperativeEntity
import com.example.data.local.PlayerProfileEntity
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
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.PowerPurple

@Composable
fun OperativesScreen(
    profile: PlayerProfileEntity?,
    operatives: List<OperativeEntity>,
    onHireOperative: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .testTag("operatives_screen_list")
    ) {
        item {
            Column {
                Text(
                    text = "SYNDICATE LIEUTENANTS & ENFORCERS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Recruit underworld legends to amplify global respect, power, and cash generation.",
                    fontSize = 12.sp,
                    color = GeoTextSecondary
                )
            }
        }

        items(operatives, key = { it.id }) { op ->
            val isHired = op.isHired
            val costCash = if (isHired) op.costCash * (op.level + 1) else op.costCash
            val costRespect = if (isHired) op.costRespect * (op.level + 1) else op.costRespect

            val currentCash = profile?.cash ?: 0.0
            val currentRespect = profile?.respect ?: 0

            val canAfford = currentCash >= costCash && currentRespect >= costRespect

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        1.dp,
                        if (isHired) GoldPrimary.copy(alpha = 0.6f) else GeoBorder,
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isHired) GoldPrimary else GeoSurfaceHighlight)
                                    .border(1.dp, GoldPrimary, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MilitaryTech,
                                    contentDescription = null,
                                    tint = if (isHired) Color(0xFF231B00) else GoldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = op.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "${op.alias} • ${op.role}",
                                    fontSize = 11.sp,
                                    color = CyanTactical,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Rank Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isHired) GoldPrimary.copy(alpha = 0.2f) else GeoSurfaceSubtle)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isHired) "Rank ${op.level}" else "Available",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHired) GoldPrimary else GeoTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = op.bio,
                        fontSize = 12.sp,
                        color = GeoTextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bonuses Bar
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
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = PowerPurple,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+${op.powerBonus * op.level} PWR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PowerPurple
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+${((op.respectMultiplier - 1) * 100 * op.level).toInt()}% REP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = CashGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+${((op.cashMultiplier - 1) * 100 * op.level).toInt()}% CASH",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CashGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Button
                    Button(
                        onClick = { onHireOperative(op.id) },
                        enabled = canAfford,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHired) CyanTactical else GoldPrimary,
                            contentColor = if (isHired) Color(0xFF00354E) else Color(0xFF231B00),
                            disabledContainerColor = GeoSurfaceHighlight,
                            disabledContentColor = GeoTextTertiary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("recruit_operative_${op.id}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHired) {
                                    "PROMOTE TO RANK ${op.level + 1} (${formatCurrency(costCash)} & ${costRespect} REP)"
                                } else {
                                    "RECRUIT OPERATIVE (${formatCurrency(costCash)} & ${costRespect} REP)"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
