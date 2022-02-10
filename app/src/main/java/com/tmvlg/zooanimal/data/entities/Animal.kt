package com.tmvlg.zooanimal.data.entities

data class Animal(
    var name: String = "",
    var latinName: String = "",
    var activeTime: String = "",
    var averageLength: Float = UNDEFINED_LENGTH,
    var averageWeight: Float = UNDEFINED_WEIGHT,
    var lifespan: Int = UNDEFINED_LIFESPAN,
    var habitat: String = "",
    var diet: String = "",
    var geo: String = "",
    var imageUrl: String = "",
    var id: Int = UNDEFINED_ID,
) {
    companion object {
        const val UNDEFINED_LENGTH = -1f
        const val UNDEFINED_WEIGHT = -1f
        const val UNDEFINED_LIFESPAN = -1
        const val UNDEFINED_ID = -1
    }
}