package com.jina.paintor.ui

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jina.paintor.R

fun TextView.setHighlightText(all: String?, highlight: String?) {
    if (all.isNullOrEmpty()) return

    if (highlight.isNullOrEmpty()) {
        text = all
        return
    }


    val highlightColor = Color.parseColor("#DC9100")
    val highlightColorSpan = ForegroundColorSpan(highlightColor)

    val spannableStringBuilder = SpannableStringBuilder(all)
    val lowercaseAll = all.lowercase()
    val lowercaseHighlight = highlight.lowercase()

    val startIndex = lowercaseAll.indexOf(lowercaseHighlight)
    val endIndex = startIndex + lowercaseHighlight.length

    if (startIndex < 0 || startIndex > all.lastIndex) {
        text = all
        return
    }

    spannableStringBuilder.setSpan(
        highlightColorSpan,
        startIndex,
        endIndex,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = spannableStringBuilder
}

fun ImageView.setCountryFlag(location: String) {
    if (location.contains("대한민국")) {
        this.setBackgroundResource(R.drawable.ic_korea_flag)
    } else if (location.contains("일본")) {
        this.setBackgroundResource(R.drawable.ic_japan_flag)
    } else if (location.contains("미국")) {
        this.setBackgroundResource(R.drawable.ic_usa_flag)
    } else {
        this.setBackgroundResource(R.drawable.ic_korea_flag)
    }
}
