package com.intertech.radioplayer.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.intertech.radioplayer.dialog.MyProgressDialog

abstract class BaseFragment<B : ViewBinding>: Fragment() {

    protected lateinit var binding: B

    private var progressDialog: MyProgressDialog? = null
    private var alertDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        // Return view binding
        return binding.root
    }

    /**
     * Method to return view binding
     */
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    /**
     * display progress dialog
     */
    protected fun showProgressDialog() {
        try {
            if (activity != null) {
                dismissProgressDialog()
                progressDialog =
                    MyProgressDialog(
                        requireContext()
                    )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * dismiss progress dialog if it is visible
     */
    protected fun dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * dismiss progress dialog on UI thread if it is visible
     */
    protected fun dismissProgressDialogOnUIThread() {

        this.activity?.runOnUiThread {
            Runnable {
                try {
                    if (progressDialog != null && progressDialog!!.isShowing) {
                        progressDialog!!.dismiss()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}