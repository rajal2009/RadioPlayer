package com.intertech.radioplayer.data.network

import android.app.Dialog
import android.content.Context
import com.intertech.radioplayer.R
import com.intertech.radioplayer.dialog.DialogHelperCustom

var alertDialog: Dialog? = null

/**
 * Handle api errors
 */
fun handleApiError(
    context: Context,
    failure: Resource.Failure
) {

    when {
        // Handle network error
        failure.isNetworkError -> {
            displayMessage(context, context.getString(R.string.no_internet))
        }
        // Handle server error
        failure.errorCode == 500 -> {
            displayMessage(context, "Internal server error")
        }
        // Handle other errors
        else -> {
            val error = failure.errorBody?.string().toString()
            displayMessage(context, error)
        }
    }
}

/**
 * display message in alert dialog
 *
 * @param message Message
 */
fun displayMessage(context: Context, message: String) {

    if (message.isNotEmpty()) {

        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog?.dismiss()
        }
        alertDialog = DialogHelperCustom.showDialog(context, message)
    }
}
