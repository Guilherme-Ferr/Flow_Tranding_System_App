package com.example.flowtrandingsystem.gui.model

import java.io.Serializable

data class ProductForLogbook (
        var id: Int = 0,
        var product_name: String = "",
        var description: String = "",
        var bar_code: String = "",
        var cost_per_item: Double = 0.0,
        var UnitOfMeasurement: UnitOfMeasurement = UnitOfMeasurement(),
        var ProductType: ProductType = ProductType()
)  : Serializable
