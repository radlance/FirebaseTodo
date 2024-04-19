package com.radlance.firebasetodo.domain

interface FireBaseResult {
    fun <T : Any> map(mapper: Mapper<T>): T

    interface Mapper<T : Any> {
        fun <E : Any> mapSuccess(value: E): T
        fun mapError(message: String): T
    }

    data class Success<T : Any>(val value: T) : FireBaseResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapSuccess(value)
        }
    }

    data class Error(val message: String) : FireBaseResult {
        override fun <T : Any> map(mapper: Mapper<T>): T {
            return mapper.mapError(message)
        }
    }
}