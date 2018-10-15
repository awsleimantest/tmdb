package aws.com.tmdb.ui.base

import aws.com.tmdb.ui.base.Displayable
import aws.com.tmdb.utils.TYPE_LOADING

class Loading : Displayable {
    override fun getType(): Int {
        return TYPE_LOADING
    }
}