package com.intertech.radioplayer.ui.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intertech.radioplayer.data.network.RemoteDataSource
import com.intertech.radioplayer.data.repository.BaseRepository
import com.intertech.radioplayer.dialog.MyProgressDialog

abstract class BaseActivity<VM : ViewModel, Repo : BaseRepository> : AppCompatActivity() {


    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()

    private var progressDialog: MyProgressDialog? = null
    private var alertDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view model instance
        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

    }

    abstract fun getViewModel(): Class<VM>

    abstract fun getActivityRepository(): Repo

    /**
     * display progress dialog
     */
    protected fun showProgressDialog() {
        try {
            dismissProgressDialog()
            progressDialog =
                MyProgressDialog(this)
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
}