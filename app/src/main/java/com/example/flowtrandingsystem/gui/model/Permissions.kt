package com.example.flowtrandingsystem.gui.model

import java.io.Serializable

data class Permissions (
    val id: Int = 0,
    var permission_name: String = "",
    val Screens: ArrayList<Screens>
) : Serializable