package ru.topbun.models.tours

import kotlinx.coroutines.selects.select
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.config.Config
import java.time.LocalDate

object Tours: IntIdTable() {

    val tourId = long("tour_id").index()
    val date = date("insert_date")

    fun insertTours(tour: TourDTO) = transaction{
        insert {
            it[tourId] = tour.tourid
            it[date] = LocalDate.now().toKotlinLocalDate()
        }
    }

    fun insertTourList(tourList: List<TourDTO>) = tourList.map { insertTours(it) }

    fun clearTours() = transaction { Tours.deleteAll() }

    fun haveSuspension(tourId: Long, delayDays: Int) = transaction {
        deleteWhere {
            date.less(LocalDate.now().minusDays(delayDays.toLong())
                .toKotlinLocalDate())
        }
        selectAll().where { Tours.tourId eq tourId }.empty().not()
    }

}