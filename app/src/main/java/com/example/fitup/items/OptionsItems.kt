package com.example.fitup.items

data class OptionToChose(
    val name: String,
    val mainOption: Int, // 0 - false, 1 - main OPtion (what do you want to change)
    val fitType : Int? = null,
    val imageId: Int? = null,
    val imageId2: Int? = null,
    val partBody: Int = 1,
    val subName: String? = null,
)