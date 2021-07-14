package com.example.flowtrandingsystem.gui.model

data class UserToken(
        var id: Int = 0,
        var branch: Branch = Branch(),
        var user_name: String = "",
        var user_cpf: String = "",
        var user_rg: String = "",
        var Role: Role = Role(),
        var permissions: Array<Permissions>
)
