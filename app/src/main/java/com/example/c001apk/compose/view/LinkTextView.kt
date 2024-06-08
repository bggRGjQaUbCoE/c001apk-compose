package com.example.c001apk.compose.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.TextView
import com.example.c001apk.compose.util.SpannableStringBuilderUtil

//https://stackoverflow.com/questions/8558732
class LinkTextView : TextView {

    override fun getHighlightColor(): Int {
        return Color.TRANSPARENT
    }

    private var dontConsumeNonUrlClicks = true
    var linkHit = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        linkHit = false
        val res = super.onTouchEvent(event)
        return if (dontConsumeNonUrlClicks) linkHit else res
    }

    class LocalLinkMovementMethod(private val isReply: Boolean) : LinkMovementMethod() {
        override fun onTouchEvent(
            widget: TextView,
            buffer: Spannable, event: MotionEvent
        ): Boolean {
            val action = event.action
            if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN
            ) {
                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop
                x += widget.scrollX
                y += widget.scrollY
                val layout = widget.layout
                val isOutOfLineBounds: Boolean = if (y < 0 || y > layout.height) {
                    true
                } else {
                    val line = layout.getLineForVertical(y)
                    (x < layout.getLineLeft(line) || x > layout.getLineRight(line))
                }
                if (isOutOfLineBounds) {
                    Selection.removeSelection(buffer)
                    return Touch.onTouchEvent(widget, buffer, event)
                }
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())
                val link = buffer.getSpans(
                    off, off, ClickableSpan::class.java
                )
                if (link.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget)
                    } else {
                        /*Selection.setSelection(
                            buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0])
                        )*/
                    }
                    val linkText =
                        buffer.substring(buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
                    if (widget is LinkTextView) {
                        widget.linkHit = if (isReply) true else linkText != "查看更多"
                    }
                    return true
                } else {
                    Selection.removeSelection(buffer)
                    return Touch.onTouchEvent(widget, buffer, event)
                }
            }
            return super.onTouchEvent(widget, buffer, event)
        }

        companion object {
            private var sInstance: LocalLinkMovementMethod? = null
            private var rInstance: LocalLinkMovementMethod? = null
            val instance: LocalLinkMovementMethod?
                get() {
                    if (sInstance == null) sInstance = LocalLinkMovementMethod(false)
                    return sInstance
                }

            val instanceR: LocalLinkMovementMethod?
                get() {
                    if (rInstance == null) rInstance = LocalLinkMovementMethod(true)
                    return rInstance
                }
        }
    }

    fun setSpText(
        isReply: Boolean = false,
        text: String,
        color: Int,
        showTotalReply: (() -> Unit)? = null,
        onOpenLink: (String) -> Unit,
        size:Float,
        fontScale:Float
    ) {
        movementMethod =
            if (isReply) LocalLinkMovementMethod.instanceR else LocalLinkMovementMethod.instance
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size * fontScale)
        val spText = SpannableStringBuilderUtil.setText(
            context,
            text,
            textSize,
            color,
            null,
            showTotalReply,
            onOpenLink
        )
        setText(spText)
    }

}