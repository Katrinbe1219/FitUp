package com.example.fitup.items

data class User(
    val nickname: String = "",
    val on_board_complete: Boolean = false,
    val is_illness: Boolean = false,
    val perso_level: String? = "",
    val arm_energy_level: String? = "",
    val leg_energy_level: String? = "",
    val body_ebergy_level: String? = ""
)