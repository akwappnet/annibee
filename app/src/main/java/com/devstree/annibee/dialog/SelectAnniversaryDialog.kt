package com.devstree.annibee.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devstree.annibee.adapter.SelectAnniversaryAdapter
import com.devstree.annibee.databinding.DialogSelectAnniversaryBinding
import com.devstree.annibee.model.response_model.AlertNotification

class SelectAnniversaryDialog() : BaseDialog(), View.OnClickListener {
    private lateinit var binding: DialogSelectAnniversaryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogSelectAnniversaryBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding.btnCancel.setOnClickListener(this)
        binding.btnDone.setOnClickListener(this)

        val adapter = SelectAnniversaryAdapter()
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : SelectAnniversaryAdapter.ClickListener {
            override fun onItemClick(
              /*  notification: AlertNotification,
                checked: Boolean,*/
                position: Int
            ) {
                adapter.lastSelectedPosition = position
                adapter.notifyDataSetChanged()
            }

        })

    }

    companion object {
        fun newInstance(): SelectAnniversaryDialog {
            val fragment = SelectAnniversaryDialog()
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCancel -> {
                dismiss()
            }
            binding.btnDone -> {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}