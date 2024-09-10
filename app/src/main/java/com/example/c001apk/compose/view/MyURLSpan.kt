package com.example.c001apk.compose.view

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
class MyURLSpan(
    private val mUrl: String,
    private val linkTextColor: Int,
    private val onShowTotalReply: (() -> Unit)? = null,
    private val onOpenLink: (String, String?) -> Unit,
    private val onShowImages: (String) -> Unit,
) :
    ClickableSpan() {

    override fun onClick(widget: View) {
        if (mUrl.isNotEmpty()) {
            when {
                mUrl.contains("/feed/replyList") -> onShowTotalReply?.let { it() }

                mUrl.contains("image.coolapk.com") -> onShowImages(mUrl)

                else -> onOpenLink(mUrl, null)
            }
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = linkTextColor
        ds.isUnderlineText = false
    }
}