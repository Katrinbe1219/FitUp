package com.example.fitup.items

data class Illness (
    val id: Int,
    val name: String
)

// болезнь  - столбец
// упражнение - столбец
// ограничения - столбец, где будут Douciemnt Id по болзени,

data class Restriction(
    val id: Int,
    val excericese_id: Int,
    val illness_id: Int
)

data class UserWithIllness(
    val uid: String? = null,
    val illness_id: String
){
    fun toMap(): Map<String, String?> = mapOf(
        "uid" to uid,
        "illness_id" to illness_id
    )
}
