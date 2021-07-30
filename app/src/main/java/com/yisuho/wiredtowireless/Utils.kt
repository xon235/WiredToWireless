package com.yisuho.wiredtowireless

import android.widget.TextView

fun TextView.appendWithScrollToBottom(text: String) {
    append(text)
    if(movementMethod != null && isVerticalScrollBarEnabled) {
        post {
            scrollTo(0, Integer.max(0, layout.getLineTop(lineCount) - height))
        }
    }
}