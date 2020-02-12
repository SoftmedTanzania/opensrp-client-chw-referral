package org.smartregister.chw.referral.di.modules

import id.zelory.compressor.Compressor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.smartregister.Context
import org.smartregister.CoreLibrary
import org.smartregister.chw.referral.ReferralLibrary
import org.smartregister.chw.referral.di.modules.TestReferralKoinModule.appModule
import org.smartregister.chw.referral.di.modules.TestReferralKoinModule.providerModule
import org.smartregister.chw.referral.di.modules.TestReferralKoinModule.repositoryModule
import org.smartregister.chw.referral.provider.ReferralRepositoryProvider
import org.smartregister.chw.referral.repository.FollowupFeedbackRepository
import org.smartregister.chw.referral.repository.ReferralServiceRepository
import org.smartregister.repository.TaskNotesRepository
import org.smartregister.repository.TaskRepository
import org.smartregister.sync.ClientProcessorForJava
import org.smartregister.sync.helper.ECSyncHelper

/**
 * This is used internally to provide modules for testing, it includes:
 * [appModule], [repositoryModule] and [providerModule]
 */
object TestReferralKoinModule {
    @JvmField
    val appModule = module {
        single { ReferralLibrary.getInstance() }
        single { ECSyncHelper.getInstance(androidApplication()) }
        single { Context.getInstance() }
        single { ClientProcessorForJava.getInstance(androidApplication()) }
        single { Compressor.getDefault(androidApplication()) }
        single { CoreLibrary.init(get()) }
        single { TaskRepository(get()) }
        single { TaskNotesRepository() }
    }

    @JvmField
    val repositoryModule = module {
        single { ReferralServiceRepository() }
        single { FollowupFeedbackRepository() }
    }

    @JvmField
    val providerModule = module {
        single { ReferralRepositoryProvider() }
    }
}