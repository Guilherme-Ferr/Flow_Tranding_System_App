package com.example.flowtrandingsystem.gui.model

import java.io.Serializable

data class ProductAdapter (
        var id: Int = 0,
        var qtd: Int = 0,
        var product_name: String = "",
        var description: String = "",
        var bar_code: String = "",
        var cost_per_item: Double = 0.0,
        var unit_of_measurement_id: Int = 0,
        var ProductType: ProductType = ProductType(),
        var company_id: Int = 0,
        var LogBookInventory: Logbook = Logbook()
)  : Serializable
