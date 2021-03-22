package me.ranieripieper.android.marvel

import android.app.Application
import me.ranieripieper.android.marvel.di.applicationModule
import me.ranieripieper.android.marvel.di.courotinesModule
import me.ranieripieper.android.marvel.di.databaseModule
import me.ranieripieper.android.marvel.di.networkModule
import me.ranieripieper.android.marvel.feature.characters.di.charactersModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MarvelApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.INFO)
            androidContext(this@MarvelApplication)
            modules(
                listOf(
                    applicationModule,
                    networkModule,
                    databaseModule,
                    courotinesModule,
                    charactersModule
                )
            )
        }
    }
}