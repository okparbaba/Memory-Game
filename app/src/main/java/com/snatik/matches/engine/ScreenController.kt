package com.snatik.matches.engine

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.snatik.matches.R
import com.snatik.matches.common.Shared
import com.snatik.matches.events.ui.ResetBackgroundEvent
import com.snatik.matches.fragments.DifficultySelectFragment
import com.snatik.matches.fragments.GameFragment
import com.snatik.matches.fragments.MenuFragment
import com.snatik.matches.fragments.ThemeSelectFragment
import java.util.*

class ScreenController private constructor() {
    private var mFragmentManager: FragmentManager? = null

    enum class Screen {
        MENU, GAME, DIFFICULTY, THEME_SELECT
    }

    fun openScreen(screen: Screen?) {
        mFragmentManager = Shared.activity!!.supportFragmentManager
        if (screen == Screen.GAME && openedScreens!!.get(openedScreens.size - 1) == Screen.GAME) {
            openedScreens.removeAt(openedScreens.size - 1)
        } else if (screen == Screen.DIFFICULTY && openedScreens!!.get(openedScreens!!.size - 1) == Screen.GAME) {
            openedScreens.removeAt(openedScreens.size - 1)
            openedScreens.removeAt(openedScreens.size - 1)
        }
        val fragment = getFragment(screen)
        val fragmentTransaction = mFragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment!!)
        fragmentTransaction.commit()
        openedScreens!!.add(screen)
    }

    fun onBack(): Boolean {
        if (openedScreens!!.size > 0) {
            val screenToRemove = openedScreens.get(openedScreens.size - 1)
            openedScreens.removeAt(openedScreens.size - 1)
            if (openedScreens.size == 0) {
                return true
            }
            val screen = openedScreens.get(openedScreens.size - 1)
            openedScreens.removeAt(openedScreens.size - 1)
            openScreen(screen)
            if ((screen == Screen.THEME_SELECT || screen == Screen.MENU) &&
                    (screenToRemove == Screen.DIFFICULTY || screenToRemove == Screen.GAME)) {
                Shared.eventBus!!.notify(ResetBackgroundEvent())
            }
            return false
        }
        return true
    }

    private fun getFragment(screen: Screen?): Fragment? {
        when (screen) {
            Screen.MENU -> return MenuFragment()
            Screen.DIFFICULTY -> return DifficultySelectFragment()
            Screen.GAME -> return GameFragment()
            Screen.THEME_SELECT -> return ThemeSelectFragment()
            else -> {
            }
        }
        return null
    }

    companion object {
        private var mInstance: ScreenController? = null
        private val openedScreens: MutableList<Screen?>? = ArrayList()
        fun getInstance(): ScreenController? {
            if (mInstance == null) {
                mInstance = ScreenController()
            }
            return mInstance
        }

        fun getLastScreen(): Screen? {
            return openedScreens!!.get(openedScreens.size - 1)
        }
    }
}