package com.herasymchuk.andrushchenko.repository

import android.content.Context
import com.herasymchuk.andrushchenko.apps.customview.compoundlayout.CompoundCustomViewActivity
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.CustomViewFromScratchActivity
import com.herasymchuk.andrushchenko.apps.handlerlooper.HandlerLooperActivity
import com.herasymchuk.andrushchenko.apps.handlerlooper.HandlerLooperActivity2
import com.herasymchuk.andrushchenko.apps.recyclerview.RecyclerViewActivity
import com.herasymchuk.andrushchenko.apps.sharedpreferences.SharedPreferencesActivity
import com.herasymchuk.andrushchenko.model.AppItem
import com.herasymchuk.andrushchenko.utils.startApp

class AppItemRepository(private val context: Context) {
    fun getAllApps(): List<AppItem> = listOf(
        AppItem("Shared Preferences") {
            context.startApp(SharedPreferencesActivity::class)
        },
        AppItem("RecyclerView") {
            context.startApp(RecyclerViewActivity::class)
        },
        AppItem("1 Handler, Looper, Main Thread") {
            context.startApp(HandlerLooperActivity::class)
        },
        AppItem("2 Handler, Looper, Main Thread") {
            context.startApp(HandlerLooperActivity2::class)
        },
        AppItem("Compound Custom View") {
            context.startApp(CompoundCustomViewActivity::class)
        },
        AppItem("Custom View from scratch") {
            context.startApp(CustomViewFromScratchActivity::class)
        },
    )
}
