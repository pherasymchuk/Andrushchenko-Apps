package com.herasymchuk.andrushchenko.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.herasymchuk.andrushchenko.apps.customview.compoundlayout.CompoundCustomViewActivity
import com.herasymchuk.andrushchenko.apps.customview.fromscratch.CustomViewFromScratchActivity
import com.herasymchuk.andrushchenko.apps.handlerlooper.HandlerLooperActivity
import com.herasymchuk.andrushchenko.apps.handlerlooper.HandlerLooperActivity2
import com.herasymchuk.andrushchenko.apps.recyclerview.RecyclerViewActivity
import com.herasymchuk.andrushchenko.apps.sharedpreferences.SharedPreferencesActivity
import com.herasymchuk.andrushchenko.model.AppItem
import kotlin.reflect.KClass

class AppItemRepository(private val context: Context) {
    fun getAllApps(): List<AppItem> = listOf(
        AppItem("Shared Preferences") {
            startApp(SharedPreferencesActivity::class)
        },
        AppItem("RecyclerView") {
            startApp(RecyclerViewActivity::class)
        },
        AppItem("1 Handler, Looper, Main Thread") {
            startApp(HandlerLooperActivity::class)
        },
        AppItem("2 Handler, Looper, Main Thread") {
            startApp(HandlerLooperActivity2::class)
        },
        AppItem("Compound Custom View") {
            startApp(CompoundCustomViewActivity::class)
        },
        AppItem("Custom View from scratch") {
            startApp(CustomViewFromScratchActivity::class)
        },
    )

    private fun <T : Activity> startApp(appClass: KClass<T>) {
        context.startActivity(Intent(context, appClass.java))
    }
}
