package com.Derick.Ideagauge.View.Adapters

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

class RecyclerItemDecoration(private val bottomMarginPx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        // Applys bottom margin only to the last item
        if (position == itemCount - 1) {
            outRect.bottom = bottomMarginPx
        } else {
            outRect.bottom = 0
        }
    }
}