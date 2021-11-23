package com.devstree.annibee.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.activity.AnniversaryDetailActivity
import com.devstree.annibee.activity.EventDetailActivity
import com.devstree.annibee.adapter.EventBasedOnDateAdapter
import com.devstree.annibee.databinding.DialogEventBasedOnDateBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EventBasedOnDateDialog(
    val date: Int,
    val month: Int,
    val year: Int,
    val eventList: ArrayList<AnniversaryEvent>
) : BaseDialog(), View.OnClickListener {
    private var _binding: DialogEventBasedOnDateBinding? = null
    private val binding get() = _binding!!
    var adapter: EventBasedOnDateAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEventBasedOnDateBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
//        binding.txtDone.setOnClickListener(this)
        binding.txtCancel.setOnClickListener(this)

        val calendar = Calendar.getInstance()
        calendar.set(year, month, date)
        when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                val df = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                val dateString: String = df.format(calendar.time)
                binding.txtDate.text = dateString
            }
            Constants.JAPANESE -> {
                val df =  DateFormat.getDateInstance(DateFormat.LONG, Locale.JAPAN)
                val dateString: String = df.format(calendar.time)
                binding.txtDate.text = dateString
            }
            Constants.TRADITIONAL_CHINESE -> {
                val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.TRADITIONAL_CHINESE)
                val dateString: String = df.format(calendar.time)
                binding.txtDate.text = dateString
            }
            Constants.SIMPLIFIED_CHINESE -> {
                val df = DateFormat.getDateInstance(DateFormat.LONG, Locale.SIMPLIFIED_CHINESE)
                val dateString: String = df.format(calendar.time)
                binding.txtDate.text = dateString
            }
        }

        binding.rvEvent.layoutManager = LinearLayoutManager(context)
        adapter = EventBasedOnDateAdapter(requireContext())
        adapter!!.setOnItemClickListener(object : EventBasedOnDateAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                if (eventList[position].type == "1" || eventList[position].type == "3") {
                    val intent = Intent(context, AnniversaryDetailActivity::class.java)
                    intent.putExtra("anniversary", eventList[position].id)
                    startActivity(intent)
                    dismiss()
                } else if (eventList[position].type == "2") {
                    val intent = Intent(context, EventDetailActivity::class.java)
                    intent.putExtra("event", eventList[position].id)
                    startActivity(intent)
                    dismiss()
                }

            }

        })
        binding.rvEvent.adapter = adapter

        adapter!!.setData(eventList/*, date, month, year*/)
    }

    companion object {
        fun newInstance(
            date: Int,
            month: Int,
            year: Int,
            eventList: ArrayList<AnniversaryEvent>
        ): EventBasedOnDateDialog {
            val fragment = EventBasedOnDateDialog(date, month, year, eventList)
            return fragment
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.txtCancel -> {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}