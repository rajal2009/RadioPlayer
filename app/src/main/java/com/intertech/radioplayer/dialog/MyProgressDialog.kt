package com.intertech.radioplayer.dialog

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup.LayoutParams
import android.view.Window
import com.intertech.radioplayer.R
import com.wang.avi.AVLoadingIndicatorView

/**
 * MyProgressDialog class is used to show progress bar with transparent
 * background using custom theme
 */
class MyProgressDialog(context: Context) : Dialog(context, R.style.AppTheme_ProgressBarDialog) {

    private var progressIndicator: AVLoadingIndicatorView? = null

    /**
     * Instantiates a new My progress dialog.
     */
    init {
        init()
    }

    // initialize progress dialog
    private fun init() {

        try {
            if (super.isShowing()) {
                super.dismiss()
            }
            super.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val mProgressView = layoutInflater.inflate(R.layout.progressbar_dialog, null)
            progressIndicator =
                mProgressView.findViewById(R.id.progressIndicator) as AVLoadingIndicatorView
            progressIndicator!!.show()
            super.addContentView(
                mProgressView,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            )
            super.setCancelable(false)
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // dismiss progress dialog
    override fun dismiss() {

        try {
            if (this.isShowing) {
                progressIndicator?.hide()
                super.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
