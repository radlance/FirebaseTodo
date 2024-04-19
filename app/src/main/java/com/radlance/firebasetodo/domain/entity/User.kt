package com.radlance.firebasetodo.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String? = null,
    val email: String? = null,
    val imageUrl: String = ""
) : Parcelable
