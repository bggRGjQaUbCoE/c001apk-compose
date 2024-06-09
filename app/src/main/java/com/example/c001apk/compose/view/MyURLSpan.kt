package com.example.c001apk.compose.view

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.example.c001apk.compose.util.ImageShowUtil
import java.net.URLDecoder

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
class MyURLSpan(
    private val mContext: Context,
    private val mUrl: String,
    private val linkTextColor: Int,
    private val imgList: List<String>? = null,
    private val showTotalReply: (() -> Unit)? = null,
    private val onOpenLink: (String) -> Unit
) :
    ClickableSpan() {

    override fun onClick(widget: View) {
        if (mUrl.isNotEmpty()) {
            when {
                mUrl.contains("/feed/replyList") -> showTotalReply?.let { it() }

                mUrl.contains("image.coolapk.com") ->
                    ImageShowUtil.startBigImgViewSimple(mContext, imgList ?: listOf(mUrl))

                else -> onOpenLink(URLDecoder.decode(mUrl, "UTF-8"))
            }
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = linkTextColor
        ds.isUnderlineText = false
    }
}