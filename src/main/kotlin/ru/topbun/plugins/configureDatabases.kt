package ru.topbun.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.tours.Tours
import ru.topbun.utills.Env


fun Application.configureDatabases() {
    Database.connect(
        url = Env["DB_URL"],
        driver = Env["DB_DRIVER"],
        user = Env["DB_USER"],
        password = Env["DB_PASSWORD"],
    )
    transaction {
        SchemaUtils.create(Tours)
    }

}