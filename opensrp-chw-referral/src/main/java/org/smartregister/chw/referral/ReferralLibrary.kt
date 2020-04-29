package org.smartregister.chw.referral

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.logger.EmptyLogger
import org.smartregister.Context
import org.smartregister.chw.referral.di.modules.ReferralKoinModule.appModule
import org.smartregister.chw.referral.di.modules.TestReferralKoinModule
import org.smartregister.repository.TaskRepository
import org.smartregister.sync.ClientProcessorForJava
import org.smartregister.sync.helper.ECSyncHelper

/**
 * This class is the main entry point of the library. Used to initialize all the required dependencies
 * and load the referral service indicators
 */
class ReferralLibrary private constructor() : KoinComponent {

    val taskRepository: TaskRepository by inject()
    val context = get<Context>()
    val syncHelper by inject<ECSyncHelper>()
    val clientProcessorForJava by inject<ClientProcessorForJava>()
    var appVersion = 1
    var databaseVersion = 1

    companion object {
        @Volatile
        private var instance: ReferralLibrary? = null

        @JvmStatic
        fun getInstance() = instance ?: synchronized(this) {
            ReferralLibrary().also {
                instance = it
            }
        }

        /**
         * Initializes the library with tha context [application]
         */
        @JvmStatic
        fun init(application: Application) {
            startKoin {
                if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
                androidContext(application)
                modules(listOf(appModule))
            }
        }

        @JvmStatic
        internal fun testInit(application: Application) {
            startKoin {
                if (BuildConfig.DEBUG) androidLogger() else EmptyLogger()
                androidContext(application)
                modules(
                    listOf(
                        TestReferralKoinModule.appModule
                    )
                )
            }
        }
    }
}