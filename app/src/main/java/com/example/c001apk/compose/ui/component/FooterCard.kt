package com.example.c001apk.compose.ui.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.ui.component.cards.LoadingCard

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
fun LazyListScope.FooterCard(
    modifier: Modifier = Modifier,
    footerState: FooterState,
    loadMore: () -> Unit,
    isFeed: Boolean = false,
) {
    item {
        LoadingCard(
            modifier = modifier,
            state = footerState,
            onClick = if (footerState is FooterState.Error) loadMore
            else null,
            isFeed = isFeed,
        )
    }
}