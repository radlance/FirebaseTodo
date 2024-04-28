package com.radlance.firebasetodo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: Int? = null,
    val title: String? = null,
    var isCompleted: Boolean? = null
) : Parcelable
