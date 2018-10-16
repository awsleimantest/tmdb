package aws.com.tmdb.ui.adapters.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aws.com.tmdb.R

class SearchItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {

        val viewPosition = parent.getChildLayoutPosition(view)
        val topItemsCount = parent.context.resources.getInteger(R.integer.row_span_count)
        outRect.bottom = space
        if (viewPosition % topItemsCount == 0) {
            outRect.left = space
            outRect.right = space / 2
        } else {
            outRect.left = space / 2
            outRect.right = space
        }
        if (viewPosition < topItemsCount) {
            outRect.top = space
        } else {
            outRect.top = 0
        }
    }
}