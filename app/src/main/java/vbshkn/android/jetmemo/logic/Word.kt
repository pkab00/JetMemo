package vbshkn.android.jetmemo.logic

import vbshkn.android.jetmemo.data.WordEntity

data class Word(
    val original: String,
    val translation: String
) {
    constructor(entity: WordEntity) : this(
        original = entity.original,
        translation = entity.translation
    )
}