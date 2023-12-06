package com.slayers.auth_todo

data class Todo (
    val id: String,
    val title: String = "",
    var isChecked: Boolean = false,
    val userId: String
)
{
    constructor() : this("", "", false,"")
}