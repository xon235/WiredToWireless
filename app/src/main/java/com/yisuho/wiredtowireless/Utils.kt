package com.yisuho.wiredtowireless

import android.widget.TextView

fun TextView.appendWithScrollToBottom(text: String) {
    append(text)
    post {
        scrollTo(0, Integer.max(0, layout.getLineTop(lineCount) - height))
    }
}