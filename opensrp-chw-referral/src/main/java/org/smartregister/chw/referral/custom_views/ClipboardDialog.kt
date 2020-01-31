package org.smartregister.chw.referral.custom_views

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import org.smartregister.chw.referral.R
import timber.log.Timber

class ClipboardDialog : Dialog, View.OnClickListener {

    @JvmField
    @VisibleForTesting
    var content: String? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, style: Int) : super(context, style)

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.widget_copy_to_clipboard)
        findViewById<View>(R.id.copyToClipboardMessage).setOnClickListener(this)
        (findViewById<View>(R.id.copyToClipboardHeader) as TextView).text = content
    }

    override fun onClick(v: View) {
        try {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText(context.getString(R.string.copy_to_clipboard), content)
            clipboard.primaryClip = clip
            dismiss()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}