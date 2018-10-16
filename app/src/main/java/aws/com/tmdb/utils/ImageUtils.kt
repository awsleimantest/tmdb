package aws.com.themoviedb.app.utils

import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder

fun loadImage(draweeView: SimpleDraweeView, url: String?, placeHolderResId: Int) {
    if (placeHolderResId != -1) {
        draweeView.hierarchy.setPlaceholderImage(placeHolderResId)
    }

    if (TextUtils.isEmpty(url)) {
        if (placeHolderResId != -1) {
            draweeView.setActualImageResource(placeHolderResId)
        }
        return
    }
    val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build()

    draweeView.controller = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setOldController(draweeView.controller)
            .build()
}

fun getImagePath(posterPath: String?, res : String): String? {
    if(TextUtils.isEmpty(posterPath)){
        return null
    }
    val result =  "https://image.tmdb.org/t/p/$res$posterPath"
    Log.v("awslog", " getImagePath() called result : $result")
    return result
}