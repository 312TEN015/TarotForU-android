package com.fourleafclover.tarot.data.repository

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Holds the runtime demo flag. Set once at app bootstrap (after Firestore lookup)
 * before any ViewModel is constructed.
 */
@Singleton
class DemoMode @Inject constructor() {
    @Volatile var isDemo: Boolean = false
}
