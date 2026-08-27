package com.example

import com.example.data.local.AllianceEntity
import com.example.data.local.LeaderboardEntryEntity
import com.example.data.local.PlayerProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TycoonGameLogicTest {

    @Test
    fun testDominanceRankScoreCalculation() {
        val respect = 1000L
        val power = 500L
        val netWorth = 50000.0

        val score = (respect * 1.5 + power * 2.0 + netWorth / 100.0).toLong()
        assertEquals(3000L, score)
    }

    @Test
    fun testReputationClamping() {
        val currentRep = 95
        val shift = 15
        val clampedRep = (currentRep + shift).coerceIn(-100, 100)
        assertEquals(100, clampedRep)

        val lowRep = -90
        val negativeShift = -25
        val clampedLow = (lowRep + negativeShift).coerceIn(-100, 100)
        assertEquals(-100, clampedLow)
    }

    @Test
    fun testAllianceDividendsAndTrust() {
        val alliance = AllianceEntity(
            id = "ally_silverline",
            name = "Silverline Consortium",
            leader = "Victoria Sterling",
            pactType = "Trade Conglomerate",
            trustScore = 50,
            dividendPerSec = 45.0,
            defenseBonus = 250,
            isAllied = true,
            avatar = "handshake",
            description = "High finance syndicate",
            totalMembers = 14
        )

        assertTrue(alliance.isAllied)
        assertEquals(45.0, alliance.dividendPerSec, 0.001)
        val improvedTrust = (alliance.trustScore + 15).coerceAtMost(100)
        assertEquals(65, improvedTrust)
    }

    @Test
    fun testLeaderboardSortingOrder() {
        val entry1 = LeaderboardEntryEntity(
            id = "1", rank = 1, syndicateName = "Omega", leaderName = "Boss A",
            power = 1000, respect = 5000, netWorth = 1000000.0, reputationScore = 50,
            reputationAlignment = "Honorable", rankTier = "Sovereign", clan = "Apex",
            territoriesCount = 5, alliesCount = 2
        )
        val entry2 = LeaderboardEntryEntity(
            id = "2", rank = 2, syndicateName = "Alpha", leaderName = "Boss B",
            power = 500, respect = 2000, netWorth = 200000.0, reputationScore = -30,
            reputationAlignment = "Ruthless", rankTier = "Overlord", clan = "Viper",
            territoriesCount = 2, alliesCount = 1
        )

        val list = listOf(entry2, entry1)
        val sortedByRank = list.sortedBy { it.rank }
        assertEquals("Omega", sortedByRank[0].syndicateName)
        assertEquals("Alpha", sortedByRank[1].syndicateName)
    }

    @Test
    fun testIntroStoryAndDeveloperCreditsAttribution() {
        val chapters = com.example.ui.screens.INTRO_STORY_CHAPTERS
        assertTrue(chapters.isNotEmpty())
        assertEquals(5, chapters.size)

        val creditsChapter = chapters.last()
        val authorAttribution = creditsChapter.bulletPoints.find { it.first == "Attribution" }?.second
        val devContact = creditsChapter.bulletPoints.find { it.first == "Developer Contact" }?.second

        assertEquals("All credits belong to Shashwat Shaurya", authorAttribution)
        assertEquals("shashwatshaurya505@gmail.com", devContact)
    }
}
