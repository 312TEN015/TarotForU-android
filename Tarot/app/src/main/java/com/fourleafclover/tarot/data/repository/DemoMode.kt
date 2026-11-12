package com.fourleafclover.tarot.data.repository

import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.demo.data.demoHarmonyTarotResult
import com.fourleafclover.tarot.demo.data.demoTarotResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds runtime demo config fetched from TarotServer at app bootstrap.
 * Set before any ViewModel is constructed.
 */
@Singleton
class DemoMode @Inject constructor() {
    @Volatile var isDemo: Boolean = false
    var demoSingleResult: TarotOutputDto = demoTarotResult
    var demoHarmonyResult: TarotOutputDto = demoHarmonyTarotResult
}
