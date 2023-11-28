package com.trivago.database

import com.trivago.database.tables.Accommodations
import com.trivago.database.tables.Locations
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    private fun hikari(): HikariDataSource {
        val envConfig = ConfigFactory.load()

        val databaseConfig = HikariConfig().apply {
            driverClassName = envConfig.getString("database.driver")
            jdbcUrl = envConfig.getString("database.jdbcUrl")
            username = envConfig.getString("database.username")
            password = envConfig.getString("database.password")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        return HikariDataSource(databaseConfig)
    }

    fun initialize() {
        Database.connect(datasource = hikari())

        transaction {
            SchemaUtils.create(Accommodations, Locations)
            SchemaUtils.createMissingTablesAndColumns(Accommodations, Locations)
        }
    }

    suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}