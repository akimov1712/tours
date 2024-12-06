package ru.topbun.models.tours

import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object Tours: IntIdTable() {

    val tourId = long("tour_id").index()
    val config = text("config")
    val date = date("insert_date")

    fun insertTours(tour: TourDTO, config: String) = transaction{
        insert {
            it[tourId] = tour.tourid
            it[this.config] = config
            it[date] = LocalDate.now().toKotlinLocalDate()
        }
    }

    fun insertTourList(tourList: List<TourDTO>, config: String) = tourList.map { insertTours(it, config) }

    fun clearTours() = transaction { Tours.deleteAll() }

    fun haveSuspension(tourId: Long, delayDays: Int, config: String) = transaction {
        deleteWhere {
            date.less(LocalDate.now().minusDays(delayDays.toLong()).toKotlinLocalDate()) and (Tours.config eq config)
        }
        selectAll().where { (Tours.tourId eq tourId)  and (Tours.config eq config)}.empty().not()
    }

}