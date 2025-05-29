package com.example.fitup.items

data class Achievement(
    var id: Int? = null,
    var name: String? = null,
    var all_coins: Int? = null,
    var earned_coins: Int? = null,
    var all_steps: Int? = null,
    var cur_step: Int? = null,
    var dificulty: DifficultyAchievemnt? = null,
    var description: String? = null,
    var status: String? = null, // old, new
)

data class AddAchievment (
    var name: String? = null,
    var all_coins: Int? = null,
    var earned_coins: Int? = null,
    var all_steps: Int? = null,
    var cur_step: Int? = null,
    var dificulty: String? = null,
    var description: String? = null,
    var status: String? = null, // old, new
)

// for achievemnts make icons according to the type of body part tension

enum class DifficultyAchievemnt{
    EASY,
    HARD,
    SMERT,



}