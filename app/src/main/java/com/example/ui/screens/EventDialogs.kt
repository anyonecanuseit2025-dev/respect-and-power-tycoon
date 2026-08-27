package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SyndicateWarLogEntity
import com.example.ui.components.formatCurrency
import com.example.ui.components.formatNumber
import com.example.ui.theme.CashGreen
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanTactical
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
import com.example.viewmodel.CityEvent
import com.example.viewmodel.EventChoice
import com.example.viewmodel.OfflineGains

@Composable
fun OfflineGainsDialog(
    offlineGains: OfflineGains,
    onDismiss: () -> Unit
) {
    val hours = offlineGains.secondsElapsed / 3600
    val minutes = (offlineGains.secondsElapsed % 3600) / 60

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = Color(0xFF231B00)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "COLLECT PARK EARNINGS",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WELCOME BACK, HERO!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = GeoTextPrimary
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "While you were away ($hours hr $minutes min), your wonderful park attractions kept delighting visitors:",
                    fontSize = 12.sp,
                    color = GeoTextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, GeoBorder, RoundedCornerShape(10.dp)),
                    color = GeoSurfaceSubtle
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney,
                                contentDescription = null,
                                tint = CashGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${formatCurrency(offlineGains.cashGained)} Wonder Coins",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CashGreen
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${formatNumber(offlineGains.respectGained)} Smile Stars",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = PowerPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${formatNumber(offlineGains.powerGained)} Hero Power",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = PowerPurple
                            )
                        }
                    }
                }
            }
        },
        containerColor = GeoSurfaceElevated
    )
}

@Composable
fun CityEventDialog(
    event: CityEvent,
    onChoice: (EventChoice) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NotificationImportant,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = event.title.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary,
                    letterSpacing = 1.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = event.description,
                    fontSize = 13.sp,
                    color = GeoTextSecondary,
                    lineHeight = 18.sp
                )

                Button(
                    onClick = { onChoice(event.choiceA) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF231B00)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = event.choiceA.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                OutlinedButton(
                    onClick = { onChoice(event.choiceB) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = event.choiceB.label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanTactical,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        containerColor = GeoSurfaceElevated
    )
}

@Composable
fun RaidResultDialog(
    warLog: SyndicateWarLogEntity,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (warLog.isVictory) CashGreen else CrimsonAlert,
                    contentColor = if (warLog.isVictory) Color(0xFF003915) else Color(0xFF491216)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "AWESOME! CONTINUE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (warLog.isVictory) Icons.Default.CheckCircle else Icons.Default.Dangerous,
                    contentDescription = null,
                    tint = if (warLog.isVictory) CashGreen else CrimsonAlert,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (warLog.isVictory) "CHALLENGE VICTORY!" else "NICE TRY, KEEP PRACTICING!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (warLog.isVictory) CashGreen else CrimsonAlert
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = warLog.description,
                    fontSize = 13.sp,
                    color = GeoTextSecondary,
                    lineHeight = 18.sp
                )

                if (warLog.isVictory && (warLog.rewardCash > 0 || warLog.rewardRespect > 0 || warLog.rewardPower > 0)) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, GeoBorder, RoundedCornerShape(8.dp)),
                        color = GeoSurfaceSubtle
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "CHALLENGE PRIZE REWARDS:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary
                            )
                            if (warLog.rewardCash > 0) {
                                Text(
                                    text = "+${formatCurrency(warLog.rewardCash)} Wonder Coins Earned",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CashGreen
                                )
                            }
                            if (warLog.rewardRespect > 0) {
                                Text(
                                    text = "+${formatNumber(warLog.rewardRespect)} Smile Stars Awarded",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            }
                            if (warLog.rewardPower > 0) {
                                Text(
                                    text = "+${formatNumber(warLog.rewardPower)} Hero Power Gained",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PowerPurple
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = GeoSurfaceElevated
    )
}
