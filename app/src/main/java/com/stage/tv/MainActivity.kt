package com.stage.tv

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.BrowseFrameLayout
import com.stage.tv.fragment.*
import com.stage.tv.utils.Common
import com.stage.tv.utils.Constants

class MainActivity : FragmentActivity(), View.OnKeyListener {

    lateinit var navBar: BrowseFrameLayout
    lateinit var fragmentContainer: FrameLayout

    lateinit var btnSearch: TextView
    lateinit var btnHome: TextView
    lateinit var btnTvshow: TextView
    lateinit var btnMovie: TextView
    lateinit var btnWatchist: TextView
    lateinit var btnSetting: TextView
    lateinit var btnApplication: TextView

    var SIDE_MENU = false
    var selectedMenu = Constants.MENU_HOME

    lateinit var lastSelectedMenu: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentContainer = findViewById(R.id.container)
        navBar = findViewById(R.id.blfNavBar)

        btnSearch = findViewById(R.id.btn_search)
        btnHome = findViewById(R.id.btn_home)
        btnTvshow = findViewById(R.id.btn_tv)
        btnMovie = findViewById(R.id.btn_movies)
       // btnWatchist = findViewById(R.id.btn_watchist)
        btnSetting = findViewById(R.id.btn_settings)
       // btnApplication = findViewById(R.id.btn_application)


        btnSearch.setOnKeyListener(this)
        btnHome.setOnKeyListener(this)
        btnTvshow.setOnKeyListener(this)
        btnMovie.setOnKeyListener(this)
        //btnWatchist.setOnKeyListener(this)
        btnSetting.setOnKeyListener(this)
        //btnApplication.setOnKeyListener(this)

        btnHome.setOnKeyListener(this)
        btnTvshow.setOnKeyListener(this)
        btnMovie.setOnKeyListener(this)
        //btnWatchist.setOnKeyListener(this)
        btnSetting.setOnKeyListener(this)
        //btnApplication.setOnKeyListener(this)

        lastSelectedMenu = btnHome
        lastSelectedMenu.isActivated = true
        changeFragment(HomeFragment())
    }

    fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()

        closeMenu()
    }

    override fun onKey(view: View?, i: Int, key_event: KeyEvent?): Boolean {
        when (i) {
            KeyEvent.KEYCODE_DPAD_CENTER -> {

                lastSelectedMenu.isActivated = false
                view?.isActivated = true
                lastSelectedMenu = view!!

                when (view.id) {
                    R.id.btn_search -> {
                        selectedMenu = Constants.MENU_SEARCH
                        changeFragment(SearchFragment())
                    }
                    R.id.btn_home -> {
                        selectedMenu = Constants.MENU_HOME
                        changeFragment(HomeFragment())
                    }
                    R.id.btn_tv -> {
                        selectedMenu = Constants.MENU_TV
                        changeFragment(TvShowFragment())
                    }
                    R.id.btn_movies -> {
                        selectedMenu = Constants.MENU_MOVIE
                        changeFragment(MovieFragment())
                    }
                    /*R.id.btn_watchist -> {
                        selectedMenu = Constants.MENU_SPORTS
                        changeFragment(WatchlistFragment())
                    }*/
                    R.id.btn_settings -> {
                        selectedMenu = Constants.MENU_SETTINGS
                        changeFragment(SettingsFragment())
                    }
                }

            }

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (!SIDE_MENU) {
                    switchToLastSelectedMenu()

                    openMenu()
                    SIDE_MENU = true
                }
            }
        }
        return false
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && SIDE_MENU) {
            SIDE_MENU = false
            closeMenu()
        }

        return super.onKeyDown(keyCode, event)
    }*/

    override fun onBackPressed() {
        if (SIDE_MENU) {
            SIDE_MENU = false
            closeMenu()
        } else {
            super.onBackPressed()
        }
    }

    fun switchToLastSelectedMenu() {
        when (selectedMenu) {
            Constants.MENU_SEARCH -> {
                btnSearch.requestFocus()
            }
            Constants.MENU_HOME -> {
                btnHome.requestFocus()
            }
            Constants.MENU_TV -> {
                btnTvshow.requestFocus()
            }
            Constants.MENU_MOVIE -> {
                btnMovie.requestFocus()
            }
            Constants.MENU_SPORTS -> {
                btnWatchist.requestFocus()
            }
            Constants.MENU_APPLICATION -> {
                btnApplication.requestFocus()
            }
            Constants.MENU_SETTINGS -> {
                btnSetting.requestFocus()
            }
        }
    }

    fun openMenu() {
       val animSlide : Animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        navBar.startAnimation(animSlide)

        navBar.requestLayout()
        navBar.layoutParams.width = Common.getWidthInPercent(this, 16)
    }

    fun closeMenu() {
        navBar.requestLayout()
        navBar.layoutParams.width = Common.getWidthInPercent(this, 5)

        fragmentContainer.requestFocus()
        SIDE_MENU = false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                // When focus is on content, open menu
                if (!SIDE_MENU) {
                    openMenu()
                    switchToLastSelectedMenu()
                    SIDE_MENU = true
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                // When menu is open, close it and focus content
                if (SIDE_MENU) {
                    closeMenu()
                    fragmentContainer.requestFocus()
                    SIDE_MENU = false
                    return true
                }
            }

            KeyEvent.KEYCODE_DPAD_CENTER -> {
                // Handle selection if focus is on menu
                val focusedView = currentFocus ?: return super.onKeyDown(keyCode, event)

                if (SIDE_MENU) {
                    lastSelectedMenu.isActivated = false
                    focusedView.isActivated = true
                    lastSelectedMenu = focusedView

                    when (focusedView.id) {
                        R.id.btn_search -> {
                            selectedMenu = Constants.MENU_SEARCH
                            changeFragment(SearchFragment())
                        }
                        R.id.btn_home -> {
                            selectedMenu = Constants.MENU_HOME
                            changeFragment(HomeFragment())
                        }
                        R.id.btn_tv -> {
                            selectedMenu = Constants.MENU_TV
                            changeFragment(TvShowFragment())
                        }
                        R.id.btn_movies -> {
                            selectedMenu = Constants.MENU_MOVIE
                            changeFragment(MovieFragment())
                        }
                        R.id.btn_settings -> {
                            selectedMenu = Constants.MENU_SETTINGS
                            changeFragment(SettingsFragment())
                        }
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }


}