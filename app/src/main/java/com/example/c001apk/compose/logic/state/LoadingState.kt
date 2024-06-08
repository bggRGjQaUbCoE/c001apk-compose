package com.example.c001apk.compose.logic.state

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
sealed class LoadingState<out T> : State() {
    data object Loading : LoadingState<Nothing>()
    data object Empty : LoadingState<Nothing>()
    data class Success<out T>(val response: T) : LoadingState<T>()
    data class Error(val errMsg: String) : LoadingState<Nothing>()
}