package com.example.flowtrandingsystem.gui.model

import java.sql.Date

data class Lot(
    val id: Int = 0,
    val lot_number: Int = 0,
    val manufacture_date: String = "",
    val expiration_date: String = ""
)

