package aws.com.tmdb.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aws.com.tmdb.ui.base.Displayable

open abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    abstract fun bind(item : Displayable)
}