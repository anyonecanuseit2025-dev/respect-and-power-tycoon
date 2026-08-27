package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

data class StoryChapter(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconColor: Color,
    val storyText: String,
    val bulletPoints: List<Pair<String, String>>,
    val quote: String
)

val INTRO_STORY_CHAPTERS = listOf(
    StoryChapter(
        title = "Prologue: Welcome to Wonder City",
        subtitle = "Chapter I • The Magic Begins",
        icon = Icons.Default.LocationCity,
        iconColor = GoldPrimary,
        storyText = "Welcome to Wonder City! The grand amusement kingdom is ready for a new lead director. Across the sunny hills and sparkling rainbow lake, kids and families are waiting for thrilling rollercoasters, sweet cookie bakeries, and cheerful mascot parades.\n\nYou arrive with $500 in starter tokens, a cheerful buddy companion, and big dreams to build the happiest theme park kingdom in the galaxy!",
        bulletPoints = listOf(
            "Sparkling Wonder Zones" to "Open new carnival zones and amusement attractions filled with joy and games.",
            "Start From Scratch" to "Run sweet lemonade stands and balloon kiosks to build your first golden coin vault.",
            "Rise to Grand Master" to "Climb from a Junior Park Helper to the beloved Grand Director of Wonder City!"
        ),
        quote = "“In Wonder City, every smile makes the whole world shine a little brighter!”"
    ),
    StoryChapter(
        title = "The 3 Pillars of Wonder Kingdom",
        subtitle = "Chapter II • Core Masteries",
        icon = Icons.Default.AttachMoney,
        iconColor = CashGreen,
        storyText = "Every great theme park stands upon three core pillars of happiness:",
        bulletPoints = listOf(
            "Wonder Coins ($)" to "Upgrade attractions, recruit friendly mascot managers, and collect joyful passive coin earnings per second.",
            "Smile Stars (⭐)" to "Earned through happiness, achievements, and friendly teamwork. Unlocks legendary hero outfits and special festival clubs.",
            "Hero Power (⚡)" to "Gather cheerful mascots, upgrade friendly bumper cars, and team up to win carnival party games and balloon tag duels."
        ),
        quote = "“Coins build the rides, Power runs the fun, and Smiles bring everyone together!”"
    ),
    StoryChapter(
        title = "Friendship, Festivals & Teamwork",
        subtitle = "Chapter III • Community Spirit",
        icon = Icons.Default.Handshake,
        iconColor = CyanTactical,
        storyText = "Your decisions bring happiness and friendship to everyone. Your actions build your friendly reputation across Wonder City:",
        bulletPoints = listOf(
            "Park Alliances" to "Team up with friendly clubs and robot leagues to earn bonus happiness tokens and festival parades.",
            "Playful Challenges" to "Participate in water balloon tag and dance-offs for grand carnival trophies and applause.",
            "Happiness Quests" to "Complete joyful community quests to shower the park with shiny confetti and prize gifts."
        ),
        quote = "“True champions always lend a hand and share the fun with all their friends!”"
    ),
    StoryChapter(
        title = "Pass & Play Duels & Wonder Sparkle",
        subtitle = "Chapter IV • Playful Mini-Games",
        icon = Icons.Default.SportsKabaddi,
        iconColor = PowerPurple,
        storyText = "Play mini-games against your friends on the same phone, and achieve cosmic wonder sparkle:",
        bulletPoints = listOf(
            "2P Pass & Play Arena" to "Pass the phone to a friend! Play friendly tactic cards across 5 wonder zones in a fun turn-based match.",
            "Wonder Sparkle (Ascension)" to "Ascend to higher wonder sparkle tiers for permanent +50% coin multipliers and sparkly mascot outfits.",
            "Global Hall of Fame" to "See your name among the top happiest park builders in the world on the global leaderboard!"
        ),
        quote = "“The best games are the ones you play and laugh together with your best friends!”"
    ),
    StoryChapter(
        title = "Developer Dedication & Credits",
        subtitle = "Chapter V • Creator & Contact",
        icon = Icons.Default.AutoAwesome,
        iconColor = GoldPrimary,
        storyText = "Power & Respect Tycoon was designed and created to deliver an imaginative, joyful, and deeply engaging tycoon empire simulation.",
        bulletPoints = listOf(
            "Lead Creator & Architect" to "Shashwat Shaurya",
            "Attribution" to "All credits belong to Shashwat Shaurya",
            "Developer Contact" to "shashwatshaurya505@gmail.com"
        ),
        quote = "“Built with passion for joyful creativity, inspiring strategy, and delightful gameplay.”"
    )
)

@Composable
fun IntroStoryDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentChapterIndex by remember { mutableIntStateOf(0) }
    val chapter = INTRO_STORY_CHAPTERS[currentChapterIndex]
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, GoldPrimary.copy(alpha = 0.8f), RoundedCornerShape(20.dp)),
            color = GeoSurface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Top Bar: Navigation & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(listOf(GoldPrimary, GoldDark))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "Story Icon",
                                tint = Color(0xFF231B00),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "WONDER CITY CHRONICLES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Story & Game Introduction",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GeoTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_intro_story_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = GeoTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Chapter Progress Stepper Dots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    INTRO_STORY_CHAPTERS.forEachIndexed { idx, _ ->
                        val isCurrent = idx == currentChapterIndex
                        val isPassed = idx < currentChapterIndex
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(6.dp)
                                .width(if (isCurrent) 28.dp else 12.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> GoldPrimary
                                        isPassed -> CyanTactical
                                        else -> GeoSurfaceHighlight
                                    }
                                )
                                .clickable { currentChapterIndex = idx }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable Content Area with Animated Transitions
                AnimatedContent(
                    targetState = currentChapterIndex,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "story_content_transition",
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                ) { targetIndex ->
                    val curChapter = INTRO_STORY_CHAPTERS[targetIndex]
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Chapter Header Hero Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = GeoSurfaceElevated),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, GeoBorderSubtle, RoundedCornerShape(14.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(curChapter.iconColor.copy(alpha = 0.2f))
                                        .border(1.dp, curChapter.iconColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = curChapter.icon,
                                        contentDescription = null,
                                        tint = curChapter.iconColor,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = curChapter.subtitle,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = curChapter.iconColor
                                    )
                                    Text(
                                        text = curChapter.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = GeoTextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Narrative Text
                        Text(
                            text = curChapter.storyText,
                            fontSize = 13.sp,
                            color = GeoTextSecondary,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Bullet Highlights
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            curChapter.bulletPoints.forEach { (label, desc) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(GeoSurfaceSubtle)
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(curChapter.iconColor)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GeoTextPrimary
                                        )
                                        Text(
                                            text = desc,
                                            fontSize = 11.sp,
                                            color = GeoTextTertiary,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quote Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(GeoSurfaceHighlight.copy(alpha = 0.5f))
                                .border(1.dp, GeoBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = curChapter.quote,
                                fontSize = 11.sp,
                                color = GoldPrimary,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Special Developer Contact Card on Chapter V
                        if (targetIndex == 4) {
                            Spacer(modifier = Modifier.height(14.dp))
                            DeveloperCreditsCard(context = context)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GeoBorderSubtle, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Navigation Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentChapterIndex > 0) {
                        OutlinedButton(
                            onClick = { currentChapterIndex-- },
                            shape = RoundedCornerShape(10.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(GeoBorder, GeoBorder))),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GeoTextSecondary),
                            modifier = Modifier.testTag("intro_story_prev_btn")
                        ) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Back", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("intro_story_skip_btn")
                        ) {
                            Text("Skip to Game", color = GeoTextTertiary, fontSize = 11.sp)
                        }
                    }

                    if (currentChapterIndex < INTRO_STORY_CHAPTERS.size - 1) {
                        Button(
                            onClick = { currentChapterIndex++ },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = Color(0xFF231B00)),
                            modifier = Modifier.testTag("intro_story_next_btn")
                        ) {
                            Text("Next Chapter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next", modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CashGreen,
                                contentColor = Color(0xFF002A10)
                            ),
                            modifier = Modifier.testTag("intro_story_begin_empire_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Begin", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enter Wonder City!", fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeveloperCreditsCard(
    context: Context,
    modifier: Modifier = Modifier
) {
    val developerEmail = "shashwatshaurya505@gmail.com"

    Card(
        colors = CardDefaults.cardColors(containerColor = GeoSurfaceElevated),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, GoldPrimary, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GoldPrimary, GoldDark))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Creator",
                        tint = Color(0xFF231B00),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "LEAD ARCHITECT & DEVELOPER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Shashwat Shaurya",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GeoTextPrimary
                    )
                    Text(
                        text = "All credits belong to Shashwat Shaurya",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanTactical
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = GeoBorderSubtle, thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Developer Contact Channel:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GeoTextTertiary
            )
            Text(
                text = developerEmail,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = GeoTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Email Intent Button
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:$developerEmail")
                                putExtra(Intent.EXTRA_SUBJECT, "Power & Respect Tycoon - Feedback / Inquiry")
                            }
                            context.startActivity(Intent.createChooser(intent, "Contact Developer"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Email client not available. Copied to clipboard!", Toast.LENGTH_SHORT).show()
                            copyToClipboard(context, developerEmail)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanTactical,
                        contentColor = Color(0xFF002731)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("email_developer_button")
                ) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "Email", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Email Dev", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Copy Email Button
                OutlinedButton(
                    onClick = {
                        copyToClipboard(context, developerEmail)
                        Toast.makeText(context, "Developer email copied: $developerEmail", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(GoldPrimary, GoldPrimary))),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldPrimary),
                    modifier = Modifier.weight(1f).testTag("copy_email_button")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy Email", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Developer Email", text)
    clipboard.setPrimaryClip(clip)
}
