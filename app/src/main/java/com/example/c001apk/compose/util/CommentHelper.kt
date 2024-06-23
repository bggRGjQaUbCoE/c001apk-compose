package com.example.c001apk.compose.util

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources
import com.example.c001apk.compose.view.CenteredImageSpan
import java.util.regex.Pattern

class EmojiTextWatcher(
    private val context: Context,
    private val size: Float,
    private val primary: Int,
    private val onAfterTextChanged: (() -> Unit)? = null,
) : TextWatcher {

    companion object {
        private val AT_PATTERN = Pattern.compile("@[\\w\\-._]+")
        private val TAG_PATTERN = Pattern.compile("#[^# @]+#")
        private val EMOJI_PATTERN = Pattern.compile("\\[[^\\]]+\\]")
    }

    override fun afterTextChanged(editable: Editable) {
        onAfterTextChanged?.let { it() }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(
        charSequence: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val spannable = charSequence as Spannable
        val total = start + count
        setEmoticonSpan(spannable, EMOJI_PATTERN, start, total)
        tintPatternColor(spannable, AT_PATTERN, start, total)
        tintPatternColor(spannable, TAG_PATTERN, start, total)
    }

    private fun tintPatternColor(spannable: Spannable, pattern: Pattern, start: Int, total: Int) {
        val region = pattern.matcher(spannable).region(start, total)
        while (region.find()) {
            val group = region.group()
            spannable.setSpan(
                ForegroundColorSpan(primary),
                region.start(),
                region.start() + group.length,
                33
            )
        }
    }

    private fun setEmoticonSpan(spannable: Spannable, pattern: Pattern, start: Int, total: Int) {
        val matcher = pattern.matcher(spannable).region(start, total)
        while (matcher.find()) {
            val group = matcher.group()
            EmojiUtils.emojiMap[group]?.let {
                AppCompatResources.getDrawable(context, it)?.let { emoji ->
                    if (group in listOf("[楼主]", "[层主]", "[置顶]"))
                        emoji.setBounds(0, 0, (size * 2).toInt(), size.toInt())
                    else
                        emoji.setBounds(0, 0, (size * 1.4).toInt(), (size * 1.4).toInt())
                    val imageSpan = CenteredImageSpan(emoji, (size * 1.4).toInt(), group)
                    spannable.setSpan(
                        imageSpan,
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

            }
        }
    }

}

class OnTextInputListener(
    private val text: String,
    private val onTextChange: () -> Unit
) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) {}
    override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (before == 0 && count == text.length
            && s.subSequence(start, start + count).toString() == text
        ) {
            onTextChange()
        }
    }
}

object FastDeleteAtUserKeyListener : View.OnKeyListener {
    override fun onKey(view: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        val editText = view as EditText
        if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
            if (removeFastDelete(editText)) {
                return true
            }

            val text = editText.text
            val selectionStart = editText.selectionStart
            if (selectionStart <= 0) {
                return false
            }
            val charAt = text[selectionStart - 1]
            if (charAt != ' ' && charAt != ':' || selectionStart != editText.selectionEnd) {
                return false
            }
            val lastIndexOfAt = lastIndexOfAt(text, selectionStart)
            val lastIndexOfTopicStart = lastIndexOfTopicStart(text, selectionStart)
            if (lastIndexOfAt >= 0 && lastIndexOfAt > lastIndexOfTopicStart) {
                val cArr = CharArray(selectionStart - lastIndexOfAt)
                text.getChars(lastIndexOfAt, selectionStart, cArr, 0)
                if (!AT_PATTERN.matcher(String(cArr)).matches()) {
                    return false
                }
                text.delete(lastIndexOfAt, selectionStart)
                return true
            }
            if (lastIndexOfTopicStart >= 0 && lastIndexOfTopicStart > lastIndexOfAt) {
                val cArr2 = CharArray(selectionStart - lastIndexOfTopicStart)
                text.getChars(lastIndexOfTopicStart, selectionStart, cArr2, 0)
                if (TAG_PATTERN.matcher(String(cArr2)).matches()) {
                    text.delete(lastIndexOfTopicStart, selectionStart)
                    return true
                }
            }
        }
        return false
    }

    private fun lastIndexOfAt(editable: Editable, i: Int): Int {
        for (i2 in i - 1 downTo 0) {
            if (editable[i2] == '@') {
                return i2
            }
        }
        return -1
    }

    private fun lastIndexOfTopicStart(editable: Editable, i: Int): Int {
        var i2 = 0
        for (i3 in i - 1 downTo 0) {
            if (editable[i3] == '#') {
                i2++
            }
            if (i2 == 2) {
                return i3
            }
        }
        return -1
    }

    private fun removeFastDelete(editText: EditText): Boolean {
        val spannableStringBuilder = editText.text as SpannableStringBuilder
        var selectionStart = editText.selectionStart
        val selectionEnd = editText.selectionEnd
        if (selectionEnd == selectionStart && selectionStart > 0) {
            selectionStart--
        }
        var z = false
        for (fastDeleteSpan in
        spannableStringBuilder.getSpans(
            selectionStart, selectionEnd, FastDeleteSpan::class.java
        ) as Array<FastDeleteSpan?>) {
            val spanStart = spannableStringBuilder.getSpanStart(fastDeleteSpan)
            val spanEnd = spannableStringBuilder.getSpanEnd(fastDeleteSpan)
            spannableStringBuilder.delete(spanStart, spanEnd)
            spannableStringBuilder.removeSpan(fastDeleteSpan)
            if (spanEnd == selectionEnd) {
                z = true
            }
        }
        return z
    }

    val AT_PATTERN = Pattern.compile("@[\\w\\-._]+[\\s:]")
    val TAG_PATTERN = Pattern.compile("#[^# @]+#\\s")

    class FastDeleteSpan

}