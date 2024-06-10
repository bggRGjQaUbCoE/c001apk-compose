package com.example.c001apk.compose.view

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.example.c001apk.compose.util.ImageShowUtil

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
class MyURLSpan(
    private val mContext: Context,
    private val mUrl: String,
    private val linkTextColor: Int,
    private val imgList: List<String>? = null,
    private val onShowTotalReply: (() -> Unit)? = null,
    private val onOpenLink: (String) -> Unit
) :
    ClickableSpan() {

    override fun onClick(widget: View) {
        if (mUrl.isNotEmpty()) {
            when {
                mUrl.contains("/feed/replyList") -> onShowTotalReply?.let { it() }

                mUrl.contains("image.coolapk.com") ->
                    ImageShowUtil.startBigImgViewSimple(mContext, imgList ?: listOf(mUrl))

                else -> onOpenLink(mUrl)
            }
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = linkTextColor
        ds.isUnderlineText = false
    }
}