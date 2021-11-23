package com.devstree.annibee.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.activity.HomeActivity
import com.devstree.annibee.adapter.HolidayListAdapter
import com.devstree.annibee.common.BaseFragment
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.dacorator.*
import com.devstree.annibee.databinding.FragmentDateBinding
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.CalendarData
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.MaterialCalendarView.SHOW_ALL
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("SetTextI18n")
class DateFragment : BaseFragment() {

    lateinit var binding: FragmentDateBinding
    var dateList: ArrayList<String> = ArrayList()
    var monthList: ArrayList<String> = ArrayList()
    var dayList: ArrayList<String> = ArrayList()
    var eventList: ArrayList<CalendarData> = ArrayList()
    //var holidayAdapter: HolidayListAdapter? = null

    var currentMonth: Int = 0
    var currentYear: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }

    private fun initUi() {

        val calendar = Calendar.getInstance()
       /* val sdf = when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
            }
            Constants.JAPANESE -> {
                SimpleDateFormat("yyyy MMMM", Locale.JAPANESE)
            }
            Constants.TRADITIONAL_CHINESE -> {
                SimpleDateFormat("yyyy MMMM", Locale.TRADITIONAL_CHINESE)
            }
            Constants.SIMPLIFIED_CHINESE -> {
                SimpleDateFormat("yyyy MMMM", Locale.SIMPLIFIED_CHINESE)
            }
            else -> {
                SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
            }
        }

        val dateString: String = sdf.format(calendar.time)*/
        currentMonth = calendar.get(Calendar.MONTH) + 1
        currentYear = calendar.get(Calendar.YEAR)

        (activity as HomeActivity).binding.homeToolbar.spinner.text = setMonthSpinner(currentMonth)
            /*when (AppHelper.getAppLanguage()) {
                Constants.ENGLISH -> {
                    "${setMonthSpinner(currentMonth)} $currentYear"
                }
                Constants.JAPANESE -> {
                    "${currentYear}年${setMonthSpinner(currentMonth)}"
                }
                Constants.TRADITIONAL_CHINESE -> {
                    "${currentYear}年${setMonthSpinner(currentMonth)}"
                }
                Constants.SIMPLIFIED_CHINESE -> {
                    "${currentYear}年${setMonthSpinner(currentMonth)}"
                }
                else -> {
                    "${setMonthSpinner(currentMonth)} $currentYear"
                }
            }*/


        (activity as HomeActivity).binding.homeToolbar.spinnerYear.text =
            when (AppHelper.getAppLanguage()) {
                Constants.ENGLISH -> {
                    "$currentYear"
                }
                Constants.JAPANESE -> {
                    "${currentYear}年"
                }
                Constants.TRADITIONAL_CHINESE -> {
                    "${currentYear}年"
                }
                Constants.SIMPLIFIED_CHINESE -> {
                    "${currentYear}年"
                }
                else -> {
                    "$currentYear"
                }
            }


        (activity as HomeActivity).binding.homeToolbar.spinner.setOnClickListener {
            spinnerMonthChangeListener(calendar)
        }
        (activity as HomeActivity).binding.homeToolbar.spinnerYear.setOnClickListener {
            spinnerYearChangeListener(calendar)
        }

        calenderView(currentYear, currentMonth, calendar.get(Calendar.DATE))

        binding.calendarView.setDateSelected(
            CalendarDay.from(
                currentYear,
                currentMonth,
                calendar.get(Calendar.DATE)
            ), true
        )

        binding.calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(
                widget: MaterialCalendarView,
                date: CalendarDay,
                selected: Boolean
            ) {
                dateChangeListener(widget, date, selected)

            }
        })

        binding.calendarView.setOnMonthChangedListener(object : OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                Controller.instance.listPopupWindow.dismiss()
                currentMonth = date?.month!!
                currentYear = date.year

                (activity as HomeActivity).binding.homeToolbar.spinner.text = setMonthSpinner(currentMonth)
                    /*when (AppHelper.getAppLanguage()) {
                        Constants.ENGLISH -> {
                            "${setMonthSpinner(date.month)} $currentYear"
                        }
                        Constants.JAPANESE -> {
                            "${currentYear}年${setMonthSpinner(date.month)}"
                        }
                        Constants.TRADITIONAL_CHINESE -> {
                            "${currentYear}年${setMonthSpinner(date.month)}"
                        }
                        Constants.SIMPLIFIED_CHINESE -> {
                            "${currentYear}年${setMonthSpinner(date.month)}"
                        }
                        else -> {
                            "${setMonthSpinner(date.month)} $currentYear"
                        }
                    }*/

                (activity as HomeActivity).binding.homeToolbar.spinnerYear.text =
                    when (AppHelper.getAppLanguage()) {
                        Constants.ENGLISH -> {
                            "$currentYear"
                        }
                        Constants.JAPANESE -> {
                            "${currentYear}年"
                        }
                        Constants.TRADITIONAL_CHINESE -> {
                            "${currentYear}年"
                        }
                        Constants.SIMPLIFIED_CHINESE -> {
                            "${currentYear}年"
                        }
                        else -> {
                            "$currentYear"
                        }
                    }

                calenderView(
                    currentYear,
                    currentMonth,
                    date.day
                )

                if (navigation?.isNetworkAvailable(context) == true) {
                    getCalendarData(currentMonth, currentYear)
                } else {
                    navigation?.noNetWorkAvailable()
                }
//                allDecorators(currentMonth)
            }

        })

        /*val language = AppHelper.getAppLanguage()

        if(language == Constants.JAPANESE){
            binding.cardHolidayView.visibility = View.VISIBLE
            binding.lnlHolidayList.visibility = View.VISIBLE
        }else{
            binding.cardHolidayView.visibility = View.GONE
            binding.lnlHolidayList.visibility = View.GONE
        }

        val mLayoutManager: LinearLayoutManager =
            GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvHolidayDay.layoutManager = mLayoutManager
        holidayAdapter = HolidayListAdapter()
        binding.rvHolidayDay.adapter = holidayAdapter

        Constants.JAPANESE_HOLIDAY.let { holidayAdapter?.setData(it) }*/
    }

    private fun getCalendarData(month: Int, year: Int) {
        navigation?.showProgressDialog()

        val params = AppHelper.getDefaultParam()
        params["month"] = month
        params["year"] = year
        params["lang_code"] = when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                "en"
            }
            Constants.JAPANESE -> {
                "ja"
            }
            Constants.TRADITIONAL_CHINESE -> {
                "zh-Hant"
            }
            Constants.SIMPLIFIED_CHINESE -> {
                "zh-Hans"
            }
            else -> {
                "en"
            }
        }

        NetworkCall.calendarData(
            params,
            object : BaseResponseListener<ResponseBody<List<CalendarData>>>() {
                override fun result(response: ResponseBody<List<CalendarData>>?) {
                    if (success) {

                        setAllCalendarData(response?.data)
                        allDecorators(month)
                        navigation?.hideProgressDialog()

                    } else {
                        navigation?.hideProgressDialog()
                        base?.unAuthorized(code, message.toString())
                    }
                }

            })
    }

    private fun setAllCalendarData(data: List<CalendarData>?) {
        if (data == null) return

        dateList.clear()
        monthList.clear()
        dayList.clear()
        eventList.clear()

        val month = SimpleDateFormat("M", Locale.ENGLISH)
        val day = SimpleDateFormat("d", Locale.ENGLISH)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        for (dataItem in data) {
            for (item in dataItem.items!!) {
                dateList.add(item.date.toString())

                val date = sdf.parse(item.date!!)
                val monthName = month.format(date!!)
                val dayName = day.format(date)

                monthList.add(monthName)
                dayList.add(dayName)
            }
        }

        eventList.addAll(data)
    }


    private fun allDecorators(month: Int) {
        if (home == null) return

        binding.calendarView.addDecorators(
            MySelectorDecorator(home!!),
            HighlightWeekendsDecorator(home!!, month),
            HighlightSundayDecorator(home!!, month),
            SundayDecorator(home!!, month),
            SaturdayDecorator(home!!, month),
            CurrentDateDecorator(home!!),
            EventDecorator(dayList, monthList, dateList, eventList)
        )
    }

    private fun spinnerMonthChangeListener(calendar: Calendar) {
        (activity as HomeActivity).showPopupMenu(
            base!!,
            (activity as HomeActivity).binding.homeToolbar.spinner,
            (activity as HomeActivity).monthList(),
            true,
            object :
                ObjectCallback<PopUpModel> {
                override fun response(obj: PopUpModel?) {
                    if (obj == null) return
                    (activity as HomeActivity).binding.homeToolbar.spinner.text = obj.title
                        /*when (AppHelper.getAppLanguage()) {
                            Constants.ENGLISH -> {
                                "${obj.title} $currentYear"
                            }
                            Constants.JAPANESE -> {
                                "${currentYear}年${obj.title}"
                            }
                            Constants.TRADITIONAL_CHINESE -> {
                                "${currentYear}年${obj.title}"
                            }
                            Constants.SIMPLIFIED_CHINESE -> {
                                "${currentYear}年${obj.title}"
                            }
                            else -> {
                                "${obj.title} $currentYear"
                            }
                        }*/

                    if (currentMonth != obj.id.toInt()) {
                        currentMonth = obj.id.toInt()
//                        currentYear = calendar.get(Calendar.YEAR)

                        calenderView(
                            currentYear,
                            currentMonth,
                            1
                        )
//                        getCalendarData(currentMonth, currentYear)
                    }
                }
            })
    }

    private fun spinnerYearChangeListener(calendar: Calendar) {
        (activity as HomeActivity).showPopupMenu(
            base!!,
            (activity as HomeActivity).binding.homeToolbar.spinner,
            getListOfYears(),
            true,
            object :
                ObjectCallback<PopUpModel> {
                override fun response(obj: PopUpModel?) {
                    if (obj == null) return

                    if (currentYear != obj.id.toInt()) {

                        currentYear = obj.id.toInt()

                        (activity as HomeActivity).binding.homeToolbar.spinnerYear.text =
                            when (AppHelper.getAppLanguage()) {
                                Constants.ENGLISH -> {
                                    "$currentYear"
                                }
                                Constants.JAPANESE -> {
                                    "${currentYear}年"
                                }
                                Constants.TRADITIONAL_CHINESE -> {
                                    "${currentYear}年"
                                }
                                Constants.SIMPLIFIED_CHINESE -> {
                                    "${currentYear}年"
                                }
                                else -> {
                                    "$currentYear"
                                }
                            }

                        calenderView(
                            currentYear,
                            currentMonth,
                            1
                        )

                        if (navigation?.isNetworkAvailable(context) == true) {
                            getCalendarData(currentMonth, currentYear)
                        } else {
                            navigation?.noNetWorkAvailable()
                        }
//                        getCalendarData(currentMonth, currentYear)
                    }
                }
            })
    }

    private fun dateChangeListener(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        Controller.instance.listPopupWindow.dismiss()

        if (dayList.contains(date.day.toString()) && monthList.contains(date.month.toString())) {

            val data = ArrayList<AnniversaryEvent>()
            val month = SimpleDateFormat("M", Locale.ENGLISH)
            val day = SimpleDateFormat("d", Locale.ENGLISH)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            for (i in eventList) {
                for (j in i.items!!) {

                    val days = sdf.parse(j.date!!)
                    val monthName = month.format(days!!)
                    val dayName = day.format(days)

                    if (monthName == date.month.toString() && dayName == date.day.toString()) {
                        data.add(j)
                    }
                }
            }

            navigation?.openEventBasedOnDateDialog(
                date.day,
                date.month - 1,
                date.year,
                data
            )
        }
//        (activity as HomeActivity).binding.homeToolbar.spinner.text =
//            "${setMonthSpinner(date.month)} ${date.year}"
//        allDecorators(date.month)

        if (currentMonth != date.month || currentYear != date.year) {
            currentMonth = date.month
            currentYear = date.year

            if (navigation?.isNetworkAvailable(context) == true) {
                getCalendarData(currentMonth, currentYear)
            } else {
                navigation?.noNetWorkAvailable()
            }
        }

        Controller.instance.currentDate = CalendarDay.from(date.year, date.month, date.day)
    }

    override fun onResume() {
        super.onResume()
        if (navigation?.isNetworkAvailable(context) == true) {
            getCalendarData(currentMonth, currentYear)
        } else {
            navigation?.noNetWorkAvailable()
        }
    }

    private fun calenderView(year: Int, month: Int, day: Int) {
        /*binding.calendarView.state().edit()
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .setMinimumDate(CalendarDay.from(year, 1, 1))
            .setMaximumDate(CalendarDay.from(year, 12, 31))
            .commit()*/

        binding.calendarView.topbarVisible = false
//        binding.calendarView.isPagingEnabled = false
        binding.calendarView.setCurrentDate(CalendarDay.from(year, month, day), true)
        binding.calendarView.showOtherDates = SHOW_ALL


        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getRealMetrics(displayMetrics)
        var height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        Log.e(height.toString())
        Log.e(width.toString())

        if (height >= 1920) {
            height = 1650
        } else if (height >= 1600) {
            height = 1500
        }
        Log.e(height.toString())

        binding.calendarView.tileHeight = height / 9

    }

    private fun setMonthSpinner(month: Int): String {
        when (month) {
            1 -> return getString(R.string.january)
            2 -> return getString(R.string.february)
            3 -> return getString(R.string.march)
            4 -> return getString(R.string.april)
            5 -> return getString(R.string.may)
            6 -> return getString(R.string.june)
            7 -> return getString(R.string.july)
            8 -> return getString(R.string.august)
            9 -> return getString(R.string.september)
            10 -> return getString(R.string.october)
            11 -> return getString(R.string.november)
            12 -> return getString(R.string.december)
        }
        return ""
    }

    private fun getListOfYears(): List<PopUpModel> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -100)
        val tenYearBack = cal.get(Calendar.YEAR)
        Log.i("yearList", "tenYearBack $tenYearBack")
        val nCal = Calendar.getInstance()
        nCal.add(Calendar.YEAR, 100)
        val tenYearAfter = nCal.get(Calendar.YEAR)
        Log.i("yearList", "tenYearAfter $tenYearAfter")
        val listOfYears = ArrayList<PopUpModel>()
        for (i in tenYearBack..tenYearAfter) {
            listOfYears.add(PopUpModel(i.toString(), i.toString()))
        }
        listOfYears.forEach {
            Log.i("yearList", "listOfYears $it")

        }
        return listOfYears

    }


/*private fun setCalendarData(calendar: Calendar, currentMonth: Int) {

   calenderView(calendar.get(Calendar.YEAR), currentMonth, calendar.get(Calendar.DATE))
   binding.calendarView.setDateSelected(
       CalendarDay.from(
           calendar.get(Calendar.YEAR),
           currentMonth,
           calendar.get(Calendar.DATE)
       ), true
   )

   eventList.add(
       DateEventModel(
           CalendarDay.from(2021, 3, 22),
           arrayListOf(
               EventModel(Color.BLUE, getString(R.string.oo_s_birthday))
           )
       )
   )
   eventList.add(
       DateEventModel(
           CalendarDay.from(2021, 3, 23),
           arrayListOf(
               EventModel(Color.parseColor("#964B00"), getString(R.string.wedding_anniversary)),
               EventModel(Color.BLUE, getString(R.string.dating_anniversary)),
               EventModel(Color.parseColor("#964B00"), getString(R.string.engagement_anniversary)),
               EventModel(Color.parseColor("#964B00"), getString(R.string.engagement_anniversary)),
               EventModel(Color.BLUE, getString(R.string.parents_wedding_anniversary)),
               EventModel(Color.BLUE, getString(R.string.parents_wedding_anniversary)),
               EventModel(Color.BLUE, getString(R.string.parents_wedding_anniversary))
           )
       )
   )
   eventList.add(
       DateEventModel(
           CalendarDay.from(2021, 3, 28),
           arrayListOf(
               EventModel(Color.parseColor("#964B00"), getString(R.string.oo_s_birthday)),
               EventModel(Color.BLUE, getString(R.string.wedding_anniversary))
           )
       )
   )

   for (i in eventList) {
       dateList.add(i.date)
   }
}*/

/*    fun locateView(v: View?): Rect? {
        val loc_int = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(loc_int)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }
        val location = Rect()
        location.left = loc_int[0]
        location.top = loc_int[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

    private fun openBottomSheet(date: String) {
        val mBottomSheetDialog = BottomSheetDialog(context!!, R.style.DialogStyle)
        val sheetView: View = layoutInflater.inflate(R.layout.dialog_event_based_on_date, null)

        val btnClose = sheetView.findViewById<ImageView>(R.id.txtDone)
        var txtDate = sheetView.findViewById<TextView>(R.id.txtDate)
        val rvEvent = sheetView.findViewById<RecyclerView>(R.id.rvEvent)

        btnClose.setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
        txtDate.text = date

        rvEvent.layoutManager = LinearLayoutManager(context)
        val adapter = EventBasedOnDateAdapter(context!!)
        adapter.setOnItemClickListener(object : EventBasedOnDateAdapter.ClickListener {
            override fun onItemClick(position: Int) {
                (activity as HomeActivity).openEventDetailActivity()
                mBottomSheetDialog.dismiss()
            }

        })
        rvEvent.adapter = adapter

        mBottomSheetDialog.setContentView(sheetView)
        mBottomSheetDialog.show()
    }*/
}