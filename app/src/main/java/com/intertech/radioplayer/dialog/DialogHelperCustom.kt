package com.intertech.radioplayer.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.intertech.radioplayer.R
import com.intertech.radioplayer.listeners.OnDialogButtonClickListener
import kotlinx.android.synthetic.main.custom_alert_dialog.*


/**
 * Dialog helper class
 */
object DialogHelperCustom {

    /**
     * Display alert dialog
     *
     * @param context    activity context
     * @param title      dialog title, if null or blank then it will not show
     * @param message    dialog message
     * @param isCancelable    dialog is cancellable or not
     * @param positiveButtonText label of the positive button of dialog
     * @param negativeButtonText label of the negative button of dialog
     * @param onDialogPositiveButtonClickListener   positive button click listener
     * @param onDialogNegativeButtonClickListener   negative button click listener
     * @return object of alert dialog
     */
    fun showDialog(
        context: Context,
        icon: Int?,
        title: String,
        message: String,
        isCancelable: Boolean,
        positiveButtonText: String,
        negativeButtonText: String,
        onDialogPositiveButtonClickListener: OnDialogButtonClickListener?,
        onDialogNegativeButtonClickListener: OnDialogButtonClickListener?
    ): Dialog? {

        try {

            val dialog = Dialog(context, R.style.AlertDialogTheme)
            dialog.setContentView(R.layout.custom_alert_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(isCancelable)

            /**
             * set dialog box's icon drawable (Optional)
             */
            if (icon == null) {
                dialog.ivIcon.visibility = View.GONE
            } else {
                dialog.ivIcon.visibility = View.VISIBLE
                dialog.ivIcon.setBackgroundResource(icon)
            }

            /**
             * set dialog box's title (Optional)
             */
            if (title.isNotEmpty()) {
                dialog.tvTitle.text = title
                dialog.tvTitle.visibility = View.VISIBLE
            } else {
                dialog.tvTitle.visibility = View.GONE
            }

            /**
             *  set dialog box message
             */
            if (message.isNotEmpty()) {
                dialog.tvMessage.text = message
                dialog.tvMessage.visibility = View.VISIBLE
            } else {
                dialog.tvMessage.visibility = View.GONE
            }

            /**
             * set dialog box's positive button text
             */
            if (positiveButtonText.isNotEmpty()) {
                dialog.tvPositive.text = positiveButtonText
                dialog.tvPositive.visibility = View.VISIBLE

                /**
                 * Handle dialog box's positive button click event
                 */
                dialog.tvPositive.setOnClickListener { view ->
                    /**
                     * Check onDialogPositiveButtonClickListener!=null
                     */
                    onDialogPositiveButtonClickListener?.onDialogButtonClicked(view)
                    dialog.dismiss()
                }
            } else {
                dialog.tvPositive.visibility = View.GONE
            }

            /**
             * set dialog box's negative button text
             */
            if (negativeButtonText.isNotEmpty()) {
                dialog.tvNegative.text = negativeButtonText
                dialog.tvNegative.visibility = View.VISIBLE

                /**
                 * Handle dialog box's negative button click event
                 */
                dialog.tvNegative.setOnClickListener { view ->
                    /**
                     * Check onDialogNegativeButtonClickListener!=null
                     */
                    onDialogNegativeButtonClickListener?.onDialogButtonClicked(view)
                    dialog.dismiss()
                }
            } else {
                dialog.tvNegative.visibility = View.GONE
            }

            dialog.show()
            return dialog
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Simplify alert dialog with title, message param to show only generic or error message
     *
     * @param context       activity context
     * @param title         dialog title
     * @param message       dialog message
     * @return object of alert dialog
     */
    fun showDialog(
        context: Context,
        message: String,
        title: String = ""
    ): Dialog? {

        return showDialog(
            context,
            null,
            title,
            message,
            true,
            context.getString(R.string.ok),
            "",
            null,
            null
        )
    }
}
