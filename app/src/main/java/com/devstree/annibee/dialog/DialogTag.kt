package com.devstree.annibee.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.devstree.annibee.databinding.DialogAddTagBinding
import com.devstree.annibee.listener.IDialogButtonClick

class DialogTag : BaseDialog(), View.OnClickListener {
    lateinit var binding: DialogAddTagBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogAddTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
    }

    override fun onClick(view: View?) {
        if (view === binding.btnClose) {
            dismiss()
        } else if (view === binding.btnDone) {
            string = binding.edtName.text.toString()
            dialogButtonClick?.onButtonClick(true)
            dismiss()
        }
    }


    override fun show(activity: FragmentActivity): BaseDialog? {
        return super.show(activity, DialogTag::class.java.simpleName)
    }

    companion object {
        private val TAG = DialogTag::class.java.simpleName

        var string: String? = null

        fun newInstance(
            dialogButtonClick: IDialogButtonClick?
        ): DialogTag {
            val fragment = DialogTag()
            fragment.dialogButtonClick = dialogButtonClick
            return fragment
        }
    }
}