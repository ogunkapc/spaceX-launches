package com.example.spacexlaunches.shared

import com.example.spacexlaunches.shared.cache.Database
import com.example.spacexlaunches.shared.cache.DatabaseDriverFactory
import com.example.spacexlaunches.shared.entity.RocketLaunch
import com.example.spacexlaunches.shared.network.SpaceXApi

class SpaceXSDK (databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}