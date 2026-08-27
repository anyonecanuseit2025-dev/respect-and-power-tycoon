package com.example.viewmodel

enum class DuelPlayer(val displayName: String, val syndicateName: String) {
    PLAYER_ONE("Captain Sparkle", "Sunny Star Squad"),
    PLAYER_TWO("Robo Champion", "Rainbow Rocket Club")
}

data class DuelSector(
    val id: Int,
    val name: String,
    val description: String,
    var p1Power: Int = 0,
    var p2Power: Int = 0,
    var controlledBy: DuelPlayer? = null,
    val cashReward: Int = 150,
    val respectReward: Int = 50
)

data class DuelCombatCard(
    val id: String,
    val title: String,
    val apCost: Int,
    val description: String,
    val effectType: CardEffect
)

enum class CardEffect {
    DEPLOY_BRUISERS, // +40 Power to selected sector
    TACTICAL_SNIPERS, // +80 Power to selected sector
    BRIBE_INSPECTOR, // -30 Opponent power on selected sector
    SABOTAGE_GRID, // -50 Opponent power on selected sector
    RESPECT_SURGE, // +100 Respect instantly
    HOSTILE_BUYOUT // Convert 30 opponent power to yours
}

data class MultiplayerDuelState(
    val isGameActive: Boolean = false,
    val currentRound: Int = 1,
    val maxRounds: Int = 5,
    val activePlayer: DuelPlayer = DuelPlayer.PLAYER_ONE,
    val p1Cash: Int = 500,
    val p2Cash: Int = 500,
    val p1Respect: Int = 100,
    val p2Respect: Int = 100,
    val p1ActionPoints: Int = 3,
    val p2ActionPoints: Int = 3,
    val selectedSectorId: Int = 1,
    val sectors: List<DuelSector> = listOf(
        DuelSector(1, "Candy Cloud Castle", "Cotton candy trees and rainbow slides", p1Power = 10, p2Power = 10, cashReward = 150, respectReward = 40),
        DuelSector(2, "Toy Rollercoaster Plaza", "High-speed loop-de-loops and arcade prizes", p1Power = 0, p2Power = 0, cashReward = 250, respectReward = 60),
        DuelSector(3, "Cosmic Planetarium Vault", "Glowing stars and space telescope games", p1Power = 0, p2Power = 0, cashReward = 450, respectReward = 100),
        DuelSector(4, "Puppy Play Park", "Obstacle courses and cheerful mascot pals", p1Power = 0, p2Power = 0, cashReward = 300, respectReward = 80),
        DuelSector(5, "Mega Wonder Castle", "Nightly grand fireworks and fairytale parades", p1Power = 0, p2Power = 0, cashReward = 600, respectReward = 200)
    ),
    val lastClashLog: String = "Pass & Play Wonder Duel initiated! Score points across wonder zones to win!",
    val isPassingPhone: Boolean = false,
    val isMatchFinished: Boolean = false,
    val winner: DuelPlayer? = null,
    val isClashingAnimation: Boolean = false
) {
    companion object {
        fun getAvailableCards(): List<DuelCombatCard> = listOf(
            DuelCombatCard("c1", "Deploy Mascot Pals", 1, "+40 Power to Zone", CardEffect.DEPLOY_BRUISERS),
            DuelCombatCard("c2", "Super Hero Flight Team", 2, "+90 Power to Zone", CardEffect.TACTICAL_SNIPERS),
            DuelCombatCard("c3", "Tickle Challenge", 1, "-35 Rival Power in Zone", CardEffect.BRIBE_INSPECTOR),
            DuelCombatCard("c4", "Rainbow Confetti Cannon", 2, "-65 Rival Power in Zone", CardEffect.SABOTAGE_GRID),
            DuelCombatCard("c5", "Friendship Hug", 2, "Convert 30 Rival Power to your side", CardEffect.HOSTILE_BUYOUT),
            DuelCombatCard("c6", "Smile Star Boost", 1, "+120 Smile Stars", CardEffect.RESPECT_SURGE)
        )
    }
}
