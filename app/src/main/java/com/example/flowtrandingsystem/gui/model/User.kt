package com.example.flowtrandingsystem.gui.model

data class User(
        var id: Int = 0,
        var cpf: String = "",
        var user_name: String = "",
        var rg: String = "",
        var Branch: Branch = Branch(),
        var Role: Role = Role(),
        var Permissions: ArrayList<Permissions>
)
