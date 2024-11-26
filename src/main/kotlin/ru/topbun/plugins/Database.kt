package ru.topbun.plugins

import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.meals.Meals
import ru.topbun.models.meals.MealsDTO
import ru.topbun.models.tours.Tours
import ru.topbun.network.api.ToursApi
import ru.topbun.utills.Env


fun Application.configureDatabases() {
    Database.connect(
        url = Env["DB_URL"],
        driver = Env["DB_DRIVER"],
        user = Env["DB_USER"],
        password = Env["DB_PASSWORD"],
    )
    transaction {
        SchemaUtils.create(Tours, Meals)
    }

}