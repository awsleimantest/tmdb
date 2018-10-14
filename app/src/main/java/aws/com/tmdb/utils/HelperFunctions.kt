package aws.com.tmdb.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import java.text.NumberFormat
import java.util.*


fun getReleaseYear(releaseDate: String?): String? {
    if (TextUtils.isEmpty(releaseDate)) {
        return null
    }
    return releaseDate!!.split("-")[0]
}

fun getBoldSpannable(input: String, from : Int, to: Int): SpannableStringBuilder{
    val spannableBuilder = SpannableStringBuilder()
    spannableBuilder.append(input)
    spannableBuilder.setSpan(StyleSpan(Typeface.BOLD),from, to,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableBuilder
}

fun formatNumber(count: Long): String {
    val format = NumberFormat.getNumberInstance(Locale.US)
    format.isGroupingUsed = true
    format.maximumFractionDigits = 1

    val formattedCount: String
    val scaleSpecifier: String

    if (count > 1000000) {
        val scaledCount = count.toDouble() / 1000000.toDouble()
        scaleSpecifier = "M"
        formattedCount = format.format(scaledCount)
    } else if (count > 100000) {
        val scaledCount = count.toDouble() / 1000.toDouble()
        scaleSpecifier = "K"
        format.maximumFractionDigits = 0
        formattedCount = format.format(scaledCount)
        if (formattedCount.startsWith("1,000")) {
            return "1M"
        }
    } else {
        return format.format(count.toLong())
    }

    return formattedCount + scaleSpecifier
}