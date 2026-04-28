package com.fourleafclover.tarot.di

import com.fourleafclover.tarot.data.repository.DemoMode
import com.fourleafclover.tarot.data.repository.FakeTarotRepository
import com.fourleafclover.tarot.data.repository.RemoteTarotRepository
import com.fourleafclover.tarot.data.repository.TarotRepository
import com.fourleafclover.tarot.network.TarotService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTarotRepository(
        demoMode: DemoMode,
        tarotService: TarotService
    ): TarotRepository =
        if (demoMode.isDemo) FakeTarotRepository()
        else RemoteTarotRepository(tarotService)
}
