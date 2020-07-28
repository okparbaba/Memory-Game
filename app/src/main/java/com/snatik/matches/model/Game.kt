package com.snatik.matches.model

import com.snatik.matches.themes.Theme

/**
 * This is instance of active playing game
 *
 * @author sromku
 */
class Game {
    /**
     * The board configuration
     */
    var boardConfiguration: BoardConfiguration? = null

    /**
     * The board arrangment
     */
    var boardArrangment: BoardArrangment? = null

    /**
     * The selected theme
     */
    var theme: Theme? = null
    var gameState: GameState? = null
}