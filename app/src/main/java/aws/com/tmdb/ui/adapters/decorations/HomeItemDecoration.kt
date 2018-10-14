package aws.com.tmdb.ui.adapters.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {


        val viewPosition = parent.getChildLayoutPosition(view)
        val topItemsCount = 2

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