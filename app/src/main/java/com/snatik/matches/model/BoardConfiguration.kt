package com.snatik.matches.model

class BoardConfiguration(val difficulty: Int) {
    var numTiles = 0
    var numTilesInRow = 0
    var numRows = 0
    var time = 0

    companion object {
        private const val _6 = 6
        private const val _12 = 12
        private const val _18 = 18
        private const val _28 = 28
        private const val _32 = 32
        private const val _50 = 50
    }

    init {
        when (difficulty) {
            1 -> {
                numTiles = _6
                numTilesInRow = 3
                numRows = 2
                time = 60
            }
            2 -> {
                numTiles = _12
                numTilesInRow = 4
                numRows = 3
                time = 90
            }
            3 -> {
                numTiles = _18
                numTilesInRow = 6
                numRows = 3
                time = 120
            }
            4 -> {
                numTiles = _28
                numTilesInRow = 7
                numRows = 4
                time = 150
            }
            5 -> {
                numTiles = _32
                numTilesInRow = 8
                numRows = 4
                time = 180
            }
            6 -> {
                numTiles = _50
                numTilesInRow = 10
                numRows = 5
                time = 210
            }
            else -> throw IllegalArgumentException("Select one of predefined sizes")
        }
    }
}