package vbshkn.android.jetmemo.model.sub

/**
 * Интерфейс саб-модели для управления представлениями различных типов Exercise.
 * По сути является обёрткой над LearningScreenModel.
 */
interface LearningScreenSubModel {
    /**
     * Метод, инициализирующий состояния компонентов view.
     * Для переопределения используется оодноимённый метод LearnScreenModel.
     */
    fun resetAllStates()
}