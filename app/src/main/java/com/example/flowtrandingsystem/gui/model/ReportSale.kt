package com.example.flowtrandingsystem.gui.model

import java.io.Serializable
data class ReportSale(
    val id: Long,
    val bar_code: Int,
    val product_name: String,
    val description: String,
    val quantity: Int,
    val cost_per_item: Double,
    val unit_name: String
):Serializable {
}