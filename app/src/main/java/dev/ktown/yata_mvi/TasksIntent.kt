package dev.ktown.yata_mvi

sealed class TasksIntent {
    object InitialIntent: TasksIntent()
    object RefreshIntent: TasksIntent()
    // TODO: Give this a param
    class TapIconIntent(): TasksIntent()
    class UserTapIntent(): TasksIntent()
}