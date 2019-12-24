package dev.ktown.yata_mvi.View

import dev.ktown.yata_mvi.Model.MapState

interface MainView {
    fun render(state: MapState)
}