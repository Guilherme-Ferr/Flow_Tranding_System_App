package com.example.flowtrandingsystem.gui.model

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

data class Logbook (
    val id: Int = 0,
    val date_of_acquisition: String = "",
    var quantity_acquired: Int = 0,
    var Product: ProductForLogbook = ProductForLogbook(),
    val Lot: Lot = Lot()
)

