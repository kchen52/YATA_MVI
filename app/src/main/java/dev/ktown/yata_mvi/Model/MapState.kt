package dev.ktown.yata_mvi.Model

sealed class MapState {
    // TODO: Rename param
    class DataState(val points: List<String>): MapState()
    class ErrorState(val description: String): MapState()
    object LoadingState: MapState()
}