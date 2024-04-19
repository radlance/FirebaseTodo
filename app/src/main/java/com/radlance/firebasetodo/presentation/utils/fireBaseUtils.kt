package com.radlance.firebasetodo.presentation.utils

fun validateFireBaseEmail(email: String): Boolean {
    val emailRegex =
        Regex("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
    return email.matches(emailRegex)
}