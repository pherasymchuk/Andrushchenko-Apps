package com.herasymchuk.andrushchenko.repository

import com.herasymchuk.andrushchenko.R
import com.herasymchuk.andrushchenko.model.AppItem

class AppItemRepository {
    fun getAllApps(): List<AppItem> {
        return listOf(
            AppItem("Shared Preferences") {
                it.navigate(R.id.action_FirstFragment_to_sharedPreferencesActivity)
            },
            AppItem("RecyclerView") {
                it.navigate(R.id.action_FirstFragment_to_recyclerViewActivity)
            },
            AppItem("1 Handler, Looper, Main Thread") {
                it.navigate(R.id.action_FirstFragment_to_handlerLooperActivity)
            },
            AppItem("2 Handler, Looper, Main Thread") {
                it.navigate(R.id.action_FirstFragment_to_handlerLooperActivity2)
            }
        )
    }
}
