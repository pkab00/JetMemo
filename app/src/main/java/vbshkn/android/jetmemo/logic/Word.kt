package vbshkn.android.jetmemo.logic

import vbshkn.android.jetmemo.data.WordEntity

/**
 * Data-класс, представляющий собой слово, и используемый тренажёром.
 * По сути является "усечённой" версией WordEntity, лишённой ненужных для тренажёра полей.
 * @see WordEntity
 */
 // TODO: Избавиться от данного класса, полностью заменив его WordEntity.
data class Word(
    val original: String,
    val translation: String
) {
    constructor(entity: WordEntity) : this(
        original = entity.original,
        translation = entity.translation
    )
}