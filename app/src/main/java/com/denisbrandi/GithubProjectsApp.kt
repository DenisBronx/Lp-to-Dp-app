package com.denisbrandi

import android.app.Application
import com.ashokvarma.gander.Gander
import com.ashokvarma.gander.imdb.GanderIMDB
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GithubProjectsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Gander.setGanderStorage(GanderIMDB.getInstance())
    }
}