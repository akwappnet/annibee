package com.devstree.annibee.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.devstree.annibee.common.BaseActivity
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.listener.IDialogButtonClick
import com.devstree.annibee.listener.IDialogDismissListener

open class BaseDialog : DialogFragment() {
    protected var base: BaseActivity? = null
    var activity: BaseActivity? = null
    var dialogButtonClick: IDialogButtonClick? = null
    var dialogDismissListener: IDialogDismissListener? = null
    protected var navigation: NavigationActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            base = context
        }
        if (context is NavigationActivity) {
            navigation = context
        }
    }

    fun showSnackBar(str: String?) {
        base?.showSnackBar(view, str)
    }

    fun showSnackBar(view: View?, str: String?) {
        base?.showSnackBar(view, str)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window ?: return dialog
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun show(activity: FragmentActivity): BaseDialog? {
        return show(activity, TAG)
    }

    fun show(activity: FragmentActivity, tag: String?): BaseDialog? {
        return if (activity.isFinishing || activity.isDestroyed) null else try {
            val fragmentManager = activity.supportFragmentManager
            val fragment = fragmentManager.findFragmentByTag(tag)
            if (fragment != null) return null
            showNow(activity.supportFragmentManager, tag)
            this
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private val TAG = BaseDialog::class.java.simpleName
    }
}