package com.ei.smart_farming.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var prefName = "Smart Farm"

    private var swAutoP1 = "auto1"
    private var swAutoP2 = "auto2"
    private var swAutoLamp = "autoLamp"

    init {
        pref = context.getSharedPreferences(prefName, 0)
        editor = pref?.edit()
    }

    var stateAutoP1: Boolean?
        get() = pref?.getBoolean(swAutoP1, false)
        set(stateAutoP1) {
            editor?.putBoolean(swAutoP1, stateAutoP1!!)
            editor?.commit()
        }

    var stateAutoP2: Boolean?
        get() = pref?.getBoolean(swAutoP2, false)
        set(stateAutoP2) {
            editor?.putBoolean(swAutoP2, stateAutoP2!!)
            editor?.commit()
        }

    var stateAutoLamp: Boolean?
        get() = pref?.getBoolean(swAutoLamp, false)
        set(stateAutoLamp) {
            editor?.putBoolean(swAutoLamp, stateAutoLamp!!)
            editor?.commit()
        }
}