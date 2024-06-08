package com.example.c001apk.compose.logic.state

/**
 * Created by bggRGjQaUbCoE on 2024/6/3
 */
sealed class FooterState : State(){
    data object Loading : FooterState()
    data object End : FooterState()
    data object Success : FooterState()
    data class Error(val errMsg: String) : FooterState()
}