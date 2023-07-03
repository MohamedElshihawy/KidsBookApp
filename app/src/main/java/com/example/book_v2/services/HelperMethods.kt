package com.example.book_v2.services

import android.content.res.Resources
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()


val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()


class SimpleItemDecoration(topBottomSpace: Int = 15, leftRightSpace: Int = 8) :
    RecyclerView.ItemDecoration() {

    private val topBottomSpaceInDp = topBottomSpace.dpToPx
    private val leftRightSpaceInDp = leftRightSpace.dpToPx

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val leftSpace = ((parent.width * .05) / 2).toInt().dpToPx

        outRect.left = leftRightSpaceInDp
        // outRect.right = spaceInDp
        outRect.bottom = topBottomSpaceInDp
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = topBottomSpaceInDp
        }
    }
}


fun setHighLightedText(tv: TextView, textToHighlight: String, textColor: Int) {
    val tvt = tv.text.toString()
    var ofe = tvt.indexOf(textToHighlight, 0)
    val wordToSpan: Spannable = SpannableString(tv.text)
    var ofs = 0
    while (ofs < tvt.length && ofe != -1) {
        ofe = tvt.indexOf(textToHighlight, ofs)
        if (ofe == -1) break else {
            // set color here
            wordToSpan.setSpan(
                ForegroundColorSpan(textColor),
                ofe,
                ofe + textToHighlight.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tv.setText(wordToSpan, TextView.BufferType.SPANNABLE)
        }
        ofs = ofe + 1
    }
}
