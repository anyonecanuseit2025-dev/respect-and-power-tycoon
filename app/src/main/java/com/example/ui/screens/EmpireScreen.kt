package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BusinessEntity
import com.example.data.local.PlayerProfileEntity
import com.example.ui.components.FloatingTapEffect
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
import com.example.viewmodel.TapEffect
import kotlin.math.pow

@Composable
fun EmpireScreen(
    profile: PlayerProfileEntity?,
    businesses: List<BusinessEntity>,
    tapEffects: List<TapEffect>,
    onHustleTap: () -> Unit,
    onBuyBusiness: (String) -> Unit,
    onUpgradeBusiness: (String) -> Unit,
    onHireManager: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GeoBackground)
            .testTag("empire_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card: Interactive Hustle & Flex Power
        item {
            HeroHustleCard(
                onTap = onHustleTap,
                tapEffects = tapEffects,
                prestigeLevel = profile?.prestigeLevel ?: 0
            )
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PARK ATTRACTIONS",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = GoldPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${businesses.count { it.level > 0 }}/${businesses.size} Open",
                    fontSize = 12.sp,
                    color = GeoTextSecondary
                )
            }
        }

        // Business Cards
        items(businesses, key = { it.id }) { biz ->
            val currentCash = profile?.cash ?: 0.0
            val buyCost = biz.baseCost * (biz.costMultiplier.pow(biz.level))
            val upgradeCost = biz.baseCost * 5.0 * (biz.upgradeLevel + 1)
            val canAffordBuy = currentCash >= buyCost
            val canAffordUpgrade = currentCash >= upgradeCost && biz.level > 0
            val canAffordManager = currentCash >= biz.managerCost && !biz.isAutomated && biz.level > 0

            BusinessCard(
                business = biz,
                buyCost = buyCost,
                upgradeCost = upgradeCost,
                canAffordBuy = canAffordBuy,
                canAffordUpgrade = canAffordUpgrade,
                canAffordManager = canAffordManager,
                onBuy = { onBuyBusiness(biz.id) },
                onUpgrade = { onUpgradeBusiness(biz.id) },
                onHireManager = { onHireManager(biz.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HeroHustleCard(
    onTap: () -> Unit,
    tapEffects: List<TapEffect>,
    prestigeLevel: Int
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(),
        label = "hero_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        GeoSurfaceHighlight,
                        GeoSurfaceElevated,
                        Color(0xFFE8F5E9)
                    )
                )
            )
            .border(1.5.dp, Brush.horizontalGradient(listOf(GoldPrimary, CyanTactical)), RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onTap
            )
            .padding(18.dp)
            .testTag("hero_hustle_button")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = GoldPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MAGIC SPARKLE VAULT",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldPrimary,
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(GoldPrimary, GoldDark)
                        )
                    )
                    .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Tap for Coins",
                    tint = Color(0xFF231B00),
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "TAP TO COLLECT WONDER COINS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = GeoTextPrimary
            )
            Text(
                text = "Fast taps generate instant coins, smile stars & bonus critical spark bursts!",
                fontSize = 11.sp,
                color = GeoTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Tap Effects Floating Overlay
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                tapEffects.forEach { effect ->
                    FloatingTapEffect(effect = effect)
                }
            }
        }
    }
}

@Composable
private fun BusinessCard(
    business: BusinessEntity,
    buyCost: Double,
    upgradeCost: Double,
    canAffordBuy: Boolean,
    canAffordUpgrade: Boolean,
    canAffordManager: Boolean,
    onBuy: () -> Unit,
    onUpgrade: () -> Unit,
    onHireManager: () -> Unit
) {
    val isOwned = business.level > 0
    val cardAlpha = if (isOwned) 1.0f else 0.85f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isOwned) GeoBorder else GeoBorderSubtle,
                RoundedCornerShape(14.dp)
            ),
        color = GeoSurfaceElevated.copy(alpha = cardAlpha),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Icon + Title + Category + Level
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isOwned) GeoSurfaceHighlight else GeoSurfaceSubtle)
                            .border(
                                1.dp,
                                if (isOwned) GoldPrimary.copy(alpha = 0.6f) else GeoBorderSubtle,
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getBusinessIcon(business.iconName),
                            contentDescription = business.name,
                            tint = if (isOwned) GoldPrimary else GeoTextTertiary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = business.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isOwned) GeoTextPrimary else GeoTextSecondary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = business.category,
                                fontSize = 11.sp,
                                color = CyanTactical,
                                fontWeight = FontWeight.Medium
                            )
                            if (business.upgradeLevel > 0) {
                                Text(
                                    text = " • Mk ${business.upgradeLevel}",
                                    fontSize = 11.sp,
                                    color = GoldPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Level Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isOwned) GoldPrimary.copy(alpha = 0.2f) else GeoSurfaceHighlight)
                        .border(
                            1.dp,
                            if (isOwned) GoldPrimary else Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isOwned) "Lv ${business.level}" else "Locked",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isOwned) GoldPrimary else GeoTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Rates / Outputs Row
            if (isOwned) {
                val upgradeMul = (1 + business.upgradeLevel).toDouble()
                val autoMul = if (business.isAutomated) 1.0 else 0.4
                val totalCashRate = business.level * business.baseRevenuePerSec * upgradeMul * autoMul
                val totalRespectRate = business.level * business.baseRespectPerSec * upgradeMul * autoMul
                val totalPowerRate = business.level * business.basePowerPerSec * upgradeMul * autoMul

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GeoSurfaceSubtle)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = null,
                            tint = CashGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "+${formatCurrency(totalCashRate)}/s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CashGreen
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "+${formatNumber(totalRespectRate.toLong())} REP/s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = PowerPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "+${formatNumber(totalPowerRate.toLong())} PWR/s",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PowerPurple
                        )
                    }
                }
            } else {
                Text(
                    text = business.description,
                    fontSize = 12.sp,
                    color = GeoTextSecondary,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row: Buy/Upgrade + Manager Automation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Buy / Level Up Button
                Button(
                    onClick = onBuy,
                    enabled = canAffordBuy,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = Color(0xFF231B00),
                        disabledContainerColor = GeoSurfaceHighlight,
                        disabledContentColor = GeoTextTertiary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.weight(1.3f).testTag("buy_business_${business.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (isOwned) Icons.Default.Add else Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isOwned) "Lv Up (${formatCurrency(buyCost)})" else "Buy (${formatCurrency(buyCost)})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                // Facility Upgrade Button
                if (isOwned) {
                    OutlinedButton(
                        onClick = onUpgrade,
                        enabled = canAffordUpgrade,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Upgrade",
                                tint = if (canAffordUpgrade) CyanTactical else GeoTextTertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Upgrade (${formatCurrency(upgradeCost)})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (canAffordUpgrade) CyanTactical else GeoTextTertiary,
                                maxLines = 1
                            )
                        }
                    }
                }

                // Manager Button
                if (isOwned) {
                    if (business.isAutomated) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E3A2E))
                                .border(1.dp, CashGreen, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Automated",
                                    tint = CashGreen,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Auto",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CashGreen
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = onHireManager,
                            enabled = canAffordManager,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyanTactical,
                                contentColor = Color(0xFF00354E),
                                disabledContainerColor = GeoSurfaceHighlight,
                                disabledContentColor = GeoTextTertiary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Hire Manager (${formatCurrency(business.managerCost)})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getBusinessIcon(iconName: String): ImageVector {
    return when (iconName) {
        "storefront" -> Icons.Default.Storefront
        "nightlife" -> Icons.Default.Nightlife
        "sports_kabaddi" -> Icons.Default.SportsKabaddi
        "security" -> Icons.Default.Security
        "memory" -> Icons.Default.Memory
        "casino" -> Icons.Default.Casino
        "directions_boat" -> Icons.Default.DirectionsBoat
        "domain" -> Icons.Default.Domain
        else -> Icons.Default.Storefront
    }
}
