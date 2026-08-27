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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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

import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.platform.LocalContext

@Composable
fun SyndicateClanScreen(
    profile: PlayerProfileEntity?,
    onPrestige: () -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    onOpenStoryAndCredits: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    var editName by remember(profile?.name) { mutableStateOf(profile?.name ?: "Don Valentino") }
    var editClan by remember(profile?.clanName) { mutableStateOf(profile?.clanName ?: "Apex Syndicate") }

    val prestigeLvl = profile?.prestigeLevel ?: 0
    val reqCash = 1_000_000.0 * (prestigeLvl + 1)
    val reqRespect = 25_000L * (prestigeLvl + 1)
    val canPrestige = (profile?.cash ?: 0.0) >= reqCash && (profile?.respect ?: 0) >= reqRespect

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .testTag("clan_screen_list")
    ) {
        // Clan & Boss Profile Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, GoldPrimary.copy(alpha = 0.7f), RoundedCornerShape(16.dp)),
                color = GeoSurfaceElevated,
                tonalElevation = 3.dp
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
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.linearGradient(listOf(GoldPrimary, Color(0xFFB45309)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = Color(0xFF231B00),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = profile?.clanName ?: "Apex Syndicate",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "Led by ${profile?.name ?: "Don Valentino"}",
                                    fontSize = 12.sp,
                                    color = CyanTactical,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = GoldPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Clan Stats Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GeoSurfaceSubtle)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Raid Record", fontSize = 10.sp, color = GeoTextTertiary)
                            Text(
                                text = "${profile?.totalRaidWins ?: 0}W / ${profile?.totalRaidLosses ?: 0}L",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CashGreen
                            )
                        }

                        Column {
                            Text(text = "All-Time Empire Cash", fontSize = 10.sp, color = GeoTextTertiary)
                            Text(
                                text = formatCurrency(profile?.allTimeCashEarned ?: 0.0),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        }

                        Column {
                            Text(text = "Alliance Tier", fontSize = 10.sp, color = GeoTextTertiary)
                            Text(
                                text = "Tier ${prestigeLvl + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanTactical
                            )
                        }
                    }
                }
            }
        }

        // Sovereign Prestige Rebirth Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, Brush.horizontalGradient(listOf(PowerPurple, GoldPrimary)), RoundedCornerShape(16.dp)),
                color = GeoSurfaceElevated,
                tonalElevation = 3.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = null,
                            tint = PowerPurple,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SYNDICATE REBIRTH (PRESTIGE)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = GoldPrimary,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ascend your syndicate to the next global tier. Resets cash and businesses in exchange for a permanent +50% multiplier on ALL operations, exclusive titles, and higher starting capital.",
                        fontSize = 12.sp,
                        color = GeoTextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress Requirements
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(GeoSurfaceSubtle)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Required Cash", fontSize = 10.sp, color = GeoTextTertiary)
                            Text(
                                text = "${formatCurrency(profile?.cash ?: 0.0)} / ${formatCurrency(reqCash)}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if ((profile?.cash ?: 0.0) >= reqCash) CashGreen else GeoTextPrimary
                            )
                        }

                        Column {
                            Text(text = "Required Respect", fontSize = 10.sp, color = GeoTextTertiary)
                            Text(
                                text = "${formatNumber(profile?.respect ?: 0)} / ${formatNumber(reqRespect)} REP",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if ((profile?.respect ?: 0) >= reqRespect) GoldPrimary else GeoTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onPrestige,
                        enabled = canPrestige,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PowerPurple,
                            contentColor = Color.White,
                            disabledContainerColor = GeoSurfaceHighlight,
                            disabledContentColor = GeoTextTertiary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("prestige_rebirth_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (canPrestige) "ASCEND TO PRESTIGE TIER ${prestigeLvl + 1}" else "REBIRTH REQUIREMENTS NOT MET",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Syndicate Achievements
        item {
            Text(
                text = "SYNDICATE MILESTONES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
        }

        item {
            AchievementItem(
                title = "Street Capo",
                description = "Earn your first $10,000 in total syndicate earnings.",
                isCompleted = (profile?.allTimeCashEarned ?: 0.0) >= 10000.0
            )
        }
        item {
            AchievementItem(
                title = "Underworld Warlord",
                description = "Win 5 syndicate PvP raids or territory attacks.",
                isCompleted = (profile?.totalRaidWins ?: 0) >= 5
            )
        }
        item {
            AchievementItem(
                title = "Metropolis Tycoon",
                description = "Reach 5,000 syndicate power score.",
                isCompleted = (profile?.power ?: 0) >= 5000
            )
        }
        item {
            AchievementItem(
                title = "Sovereign Apex",
                description = "Perform your first Syndicate Rebirth prestige ascension.",
                isCompleted = (profile?.prestigeLevel ?: 0) >= 1
            )
        }

        // Section Title: Underworld Chronicles & Developer Credits
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "UNDERWORLD ARCHIVES & CREATOR INTEL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
        }

        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, CyanTactical.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                color = GeoSurfaceElevated,
                tonalElevation = 2.dp
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
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(CyanTactical.copy(alpha = 0.2f))
                                    .border(1.dp, CyanTactical.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = CyanTactical,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Story Prologue & Introduction",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "Read the lore of New Sovereign & game guide",
                                    fontSize = 11.sp,
                                    color = GeoTextTertiary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onOpenStoryAndCredits,
                        colors = ButtonDefaults.buttonColors(containerColor = CyanTactical, contentColor = Color(0xFF002731)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("open_intro_story_clan_btn")
                    ) {
                        Text("Read Underworld Chronicles", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Developer Credits & Contact Card
        item {
            DeveloperCreditsCard(context = LocalContext.current)
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Edit Syndicate Credentials",
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Boss Name") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceElevated,
                            unfocusedContainerColor = GeoSurfaceElevated
                        )
                    )
                    OutlinedTextField(
                        value = editClan,
                        onValueChange = { editClan = it },
                        label = { Text("Syndicate / Clan Name") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceElevated,
                            unfocusedContainerColor = GeoSurfaceElevated
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(editName, editClan)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color(0xFF231B00))
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = GeoTextSecondary)
                }
            },
            containerColor = GeoSurfaceElevated
        )
    }
}

@Composable
private fun AchievementItem(
    title: String,
    description: String,
    isCompleted: Boolean
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                1.dp,
                if (isCompleted) CashGreen.copy(alpha = 0.5f) else GeoBorder,
                RoundedCornerShape(10.dp)
            ),
        color = GeoSurfaceElevated
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isCompleted) CashGreen else GeoSurfaceHighlight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = if (isCompleted) Color(0xFF003915) else GeoTextTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompleted) GeoTextPrimary else GeoTextSecondary
                )
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = GeoTextTertiary
                )
            }
            Text(
                text = if (isCompleted) "CLAIMED" else "LOCKED",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = if (isCompleted) CashGreen else GeoTextTertiary
            )
        }
    }
}
