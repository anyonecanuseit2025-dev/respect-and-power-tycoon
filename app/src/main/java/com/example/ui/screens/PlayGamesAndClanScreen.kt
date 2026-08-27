package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PlayerProfileEntity
import com.example.data.playgames.PlayGamesAchievement
import com.example.data.playgames.PlayGamesState
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
fun PlayGamesAndClanScreen(
    profile: PlayerProfileEntity?,
    playGamesState: PlayGamesState,
    onSyncCloudSave: () -> Unit,
    onToggleSignIn: () -> Unit,
    onUpdateProfile: (String, String) -> Unit,
    onOpenStoryAndCredits: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editName by remember(profile?.name) { mutableStateOf(profile?.name ?: "Don Valentino") }
    var editClan by remember(profile?.clanName) { mutableStateOf(profile?.clanName ?: "Apex Syndicate") }

    val unlockedAchievementsCount = playGamesState.achievements.count { it.isUnlocked }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .testTag("play_games_clan_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Google Play Games Services Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(listOf(CashGreen, CyanTactical)),
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
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(CashGreen, Color(0xFF004D40))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = "Google Play Games",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Google Play Games",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = GeoTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Connected",
                                        tint = CashGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "${playGamesState.gamerTag} • Level ${playGamesState.playGamesLevel}",
                                    fontSize = 12.sp,
                                    color = CyanTactical,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Cloud Sync button
                        IconButton(
                            onClick = {
                                onSyncCloudSave()
                                Toast.makeText(context, "Google Play Cloud Save Synced!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(GeoSurfaceHighlight)
                                .size(38.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Sync Cloud",
                                tint = CashGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats summary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(GeoSurfaceSubtle)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$unlockedAchievementsCount / ${playGamesState.achievements.size}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = GoldPrimary)
                            Text(text = "Achievements", fontSize = 10.sp, color = GeoTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${playGamesState.totalXp} XP", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CashGreen)
                            Text(text = "Play Games XP", fontSize = 10.sp, color = GeoTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Active", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyanTactical)
                            Text(text = "Cloud Save", fontSize = 10.sp, color = GeoTextSecondary)
                        }
                    }
                }
            }
        }

        // 100% Ad-Free Guarantee Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                color = GeoSurfaceElevated
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GoldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "100% Pure & Ad-Free Experience",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = GeoTextPrimary
                        )
                        Text(
                            text = "Zero ads, zero interstitials, zero forced videos. All boosts and offline rewards are 100% earned through gameplay strategy!",
                            fontSize = 11.sp,
                            color = GeoTextSecondary
                        )
                    }
                }
            }
        }

        // Clan & Boss Profile Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GeoBorder, RoundedCornerShape(16.dp)),
                color = GeoSurfaceElevated
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
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(listOf(GoldPrimary, Color(0xFFB45309)))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = Color(0xFF231B00),
                                    modifier = Modifier.size(26.dp)
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
                                    text = "Led by ${profile?.name ?: "Don Valentino"} • ${profile?.title ?: "Street Capo"}",
                                    fontSize = 12.sp,
                                    color = GeoTextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GeoSurfaceHighlight)
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Clan Credentials",
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Underworld Story & Developer Attribution Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, GoldPrimary.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                color = GeoSurfaceElevated
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
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(GoldPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "Story Lore",
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Underworld Archives & Story",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = GeoTextPrimary
                                )
                                Text(
                                    text = "Game Lore & Creator Shashwat Shaurya",
                                    fontSize = 11.sp,
                                    color = GeoTextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = onOpenStoryAndCredits,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "READ STORY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF231B00)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = GeoSurfaceSubtle,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "All credits belong to Shashwat Shaurya",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                                Text(
                                    text = "shashwatshaurya505@gmail.com",
                                    fontSize = 10.sp,
                                    color = GeoTextSecondary
                                )
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Developer Email", "shashwatshaurya505@gmail.com")
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "Email copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Email",
                                        tint = CyanTactical,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                data = Uri.parse("mailto:shashwatshaurya505@gmail.com")
                                                putExtra(Intent.EXTRA_SUBJECT, "Power & Respect: Syndicate Tycoon Feedback")
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Cannot launch email client", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Send Email",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Google Play Games Achievements
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = CashGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Play Games Achievements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )
                }
                Text(
                    text = "$unlockedAchievementsCount Unlocked",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CashGreen
                )
            }
        }

        // Achievements List
        items(playGamesState.achievements, key = { it.id }) { achievement ->
            PlayGamesAchievementCard(achievement = achievement)
        }
    }

    // Edit Profile Modal
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = "Edit Syndicate Credentials",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = GeoTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Syndicate Boss Name") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceSubtle,
                            unfocusedContainerColor = GeoSurfaceSubtle,
                            focusedIndicatorColor = GoldPrimary,
                            unfocusedIndicatorColor = GeoBorder
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editClan,
                        onValueChange = { editClan = it },
                        label = { Text("Syndicate Clan / Family Name") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = GeoTextPrimary,
                            unfocusedTextColor = GeoTextPrimary,
                            focusedContainerColor = GeoSurfaceSubtle,
                            unfocusedContainerColor = GeoSurfaceSubtle,
                            focusedIndicatorColor = GoldPrimary,
                            unfocusedIndicatorColor = GeoBorder
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editName.isNotBlank() && editClan.isNotBlank()) {
                            onUpdateProfile(editName.trim(), editClan.trim())
                            showEditProfileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("SAVE CHANGES", fontWeight = FontWeight.Bold, color = Color(0xFF231B00))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("CANCEL", color = GeoTextSecondary)
                }
            },
            containerColor = GeoSurfaceElevated
        )
    }
}

@Composable
private fun PlayGamesAchievementCard(achievement: PlayGamesAchievement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (achievement.isUnlocked) CashGreen.copy(alpha = 0.5f) else GeoBorder,
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) GeoSurfaceHighlight else GeoSurfaceElevated
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (achievement.isUnlocked) CashGreen.copy(alpha = 0.2f) else GeoSurfaceSubtle
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (achievement.isUnlocked) achievement.icon else Icons.Default.Lock,
                        contentDescription = achievement.title,
                        tint = if (achievement.isUnlocked) CashGreen else GeoTextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = GeoTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = achievement.description,
                        fontSize = 11.sp,
                        color = GeoTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (achievement.isUnlocked) CashGreen else GeoSurfaceSubtle)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (achievement.isUnlocked) "+${achievement.xpValue} XP" else "${achievement.xpValue} XP",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (achievement.isUnlocked) Color(0xFF00332C) else GeoTextTertiary
                )
            }
        }
    }
}
