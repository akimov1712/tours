package ru.topbun.models.meals

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Meals: IntIdTable() {

    val mealId = integer("meal_id")
    val name = text("name").index()
    val fullname = text("full_name")
    val russian = text("russian")
    val russianfull = text("russian_full")

    fun insertMeal(meal: MealsDTO) = transaction{
        insert{
            it[Meals.mealId] = meal.id
            it[Meals.name] = meal.name
            it[Meals.fullname] = meal.fullname
            it[Meals.russian] = meal.russian
            it[Meals.russianfull] = meal.russianfull
        }
    }

    fun selectMeal(name: String) = transaction{
        selectAll().where{ Meals.name eq name }.first().toMeal()
    }

    fun ResultRow.toMeal() = MealsDTO(
        id = this[Meals.mealId],
        name = this[Meals.name],
        fullname = this[Meals.fullname],
        russian = this[Meals.russian],
        russianfull = this[Meals.russianfull],
    )

}