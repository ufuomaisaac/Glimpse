package com.example.glimpse.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


sealed class DataState<out T> {

    /** Data that is being wrapped by [DataState]. */
    abstract val data: T?

    /** Loading state that has no data is available. */
    data object Loading : DataState<Nothing>() {
        override val data: Nothing? get() = null
    }

    /** Loaded state that has data available. */
    data class Success <T>(
        override val data: T,
    ): DataState<T>()

    /** Pending state that has data available. */
    data class Pending<T>(
        override val data: T,
    ): DataState<T>()

    /** Error state that may have data available. */
    data class Error<T>(
        val error: String,
        override val data: T? = null,
    ): DataState<T>()


    /** No network state that may have data is available. */
    data class NoNetwork<T>(
        override val data: T? = null
    ): DataState<T>()

}


/*
fun <T> Flow<T>.asDataStateFlow(): Flow<DataState<T>> =
    map<T, DataState<T>> { DataState.Success(it) }
        .onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(it, null)) }

*/

