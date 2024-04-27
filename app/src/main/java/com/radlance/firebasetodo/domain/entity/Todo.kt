package com.radlance.firebasetodo.domain.entity

import com.google.firebase.database.PropertyName

data class Todo(
    val id: Int? = null,
    val title: String? = null,
    var isCompleted: Boolean? = null
)
