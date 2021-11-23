package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.recyclerview.widget.GridLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.adapter.AddPhotoAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.databinding.ActivityCreateEventBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.AnniversaryEvent
import com.devstree.annibee.model.response_model.Attribute
import com.devstree.annibee.model.response_model.Image
import com.devstree.annibee.network.BaseResponseListener
import com.devstree.annibee.network.NetworkCall
import com.devstree.annibee.network.model.ResponseBody
import com.devstree.annibee.utility.*
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.AlbumFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity : NavigationActivity(), View.OnClickListener {

    lateinit var binding: ActivityCreateEventBinding

    private var selectedStartDate: String? = null
    private var selectedEndDate: String? = null
    private var selectedStartTime: String? = null
    private var selectedEndTime: String? = null
    var photoAdapter: AddPhotoAdapter? = null
//    var peopleTagAdapter: PeopleTagAdapter? = null

    var imageList = ArrayList<Image?>()
    var oldImageList = ArrayList<Image?>()
    var removeImageList = ArrayList<Long?>()
    var list = ArrayList<PopUpModel>()
    var albumList = ArrayList<AlbumFile?>()
    var peopleList = ArrayList<String>()

    private var event: AnniversaryEvent? = null
    private var anniversaryId: Long? = 0
    private var anniversary: AnniversaryEvent? = null

    var startDate: Int? = 0
    var startMonth: Int? = 0
    var startYear: Int? = 0
    var endDate: Int? = 0
    var endMonth: Int? = 0
    var endYear: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        event = intent.getParcelableExtra("event")
        anniversary = intent.getParcelableExtra("anniversary")

        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(getString(R.string.create_an_event), true)
        binding.eventToolbar.btnSave.visibility = View.VISIBLE
        binding.eventToolbar.btnSave.setOnClickListener(this)

        /*startDate = Controller.instance.currentDate.day
        startMonth = Controller.instance.currentDate.month
        startYear = Controller.instance.currentDate.year

        endDate = Controller.instance.currentDate.day
        endMonth = Controller.instance.currentDate.month
        endYear = Controller.instance.currentDate.year

        selectedStartDate = String.format("%04d-%02d-%02d", startYear, startMonth, startDate)
        selectedEndDate = String.format("%04d-%02d-%02d", endYear, endMonth, endDate)

        binding.edtStartDate.setText(selectedStartDate)
        binding.edtEndDate.setText(selectedEndDate)

        val currentDateTime = Calendar.getInstance()
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)*/

        binding.lnlMain.setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()

            }
        })

        binding.edtStartTime.setText(showTime(18, 0))
        binding.edtEndTime.setText(showTime(19, 0))


        anniversaryId = event?.anniversaryId.takeIf { anniversary == null } ?: anniversary?.id


        binding.edtAnniName.setText(anniversary?.name)

        binding.rvAddPhoto.layoutManager = GridLayoutManager(this, 3)
        photoAdapter = AddPhotoAdapter(this::onItemClick)
        binding.rvAddPhoto.adapter = photoAdapter


        binding.edtAnniName.setOnClickListener(this)
        binding.edtEndTime.setOnClickListener(this)

        binding.checkAllDay.setOnClickListener {
            if (!binding.checkAllDay.isChecked) {
                binding.edtEndTime.setOnClickListener(this)
                binding.edtStartTime.setOnClickListener(this)
            } else {
                binding.edtEndTime.setOnClickListener(null)
                binding.edtStartTime.setOnClickListener(null)
                /*binding.edtEndDate.setText(
                    selectedStartDate
                )*/
                binding.edtStartTime.setText("")
                binding.edtEndTime.setText("")
                selectedStartTime = ""
                selectedEndTime = ""
            }
        }

        /*peopleTagAdapter = PeopleTagAdapter(this::onPeopleClick)
        binding.recyclerViewPeople.adapter = peopleTagAdapter*/

        if (isNetworkAvailable(this)) {
            getAnniversaryList()
        } else {
            noNetWorkAvailable()
        }


        setData(event)
    }

    private fun getAnniversaryList() {
        NetworkCall.getAnniversaryDropDown(
            AppHelper.getDefaultParam(),
            object : BaseResponseListener<ResponseBody<List<PopUpModel>>>() {
                override fun result(response: ResponseBody<List<PopUpModel>>?) {
                    if (success) {
                        list = response?.data as ArrayList<PopUpModel>
                    }
                }
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    @SuppressLint("SetTextI18n")
    override fun onClick(v: View) {
        when (v) {
            binding.edtAnniName -> {
                showPopupMenu(this, binding.edtAnniName, list, true, object :
                    ObjectCallback<PopUpModel> {
                    override fun response(obj: PopUpModel?) {
                        if (obj == null) return
                        /*   if (!obj.parentName.isNullOrEmpty()) {
                               showDefaultAttribute()
                           } else {*/
                        binding.edtAnniName.setText(obj.title)
                        anniversaryId = obj.id.toLong()
//                    }
                    }
                })
            }
            binding.edtStartDate -> {
                hideKeyBoard()
                if (startDate == 0) {
                    openDatePickerDialog(setStartDate())
                } else {
                    openDatePickerDialog(setStartDate(), startDate!!, startMonth!! - 1, startYear!!)
                }
            }
            binding.edtStartTime -> {
                pickDateTime(object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        selectedStartTime = String.format("%02d:%02d", hourOfDay, minute)
                        selectedEndTime = String.format("%02d:%02d", hourOfDay + 1, minute)

                        binding.edtStartTime.setText(showTime(hourOfDay, minute))

//                        if (binding.checkAllDay.isChecked) {
                        binding.edtEndTime.setText(showTime(hourOfDay + 1, minute))
//                        }
                    }
                }, false)
            }
            binding.edtEndDate -> {
                hideKeyBoard()
                if (endDate == 0) {
                    openDatePickerDialog(setEndDate())
                } else {
                    openDatePickerDialog(setEndDate(), endDate!!, endMonth!! - 1, endYear!!)
                }
            }
            binding.edtEndTime -> {
                pickDateTime(object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        selectedEndTime = String.format("%02d:%02d", hourOfDay, minute)
                        binding.edtEndTime.setText(showTime(hourOfDay, minute))
                    }
                }, false)
            }

            binding.addImage -> {
//                uploadPhoto()
                /*val intent = Intent(this, PhotoActivity::class.java)
                startActivityForResult(intent, 1001)*/
                getAllImages()
            }
            binding.eventToolbar.btnSave -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        event()
                    } else {
                        noNetWorkAvailable()
                    }
                }
            }
        }
    }

    private fun setStartDate(): DatePickerDialog.OnDateSetListener {
        return object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker?,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {

                val calendar: Calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                calendar.set(year, month, dayOfMonth)
                val dateString: String = sdf.format(calendar.time)
                selectedStartDate = dateString

                this@CreateEventActivity.startDate = dayOfMonth
                this@CreateEventActivity.startMonth = month + 1
                this@CreateEventActivity.startYear = year

                binding.edtStartDate.setText(dateString)

//                if (binding.checkAllDay.isChecked) {
                /* calendar.set(year, month, dayOfMonth + 1)
                 val endDateString: String = sdf.format(calendar.time)*/
                binding.edtEndDate.setText(dateString)
                selectedEndDate = dateString
//                }

                /* if (!binding.checkAllDay.isChecked) {
                     pickDateTime({ view, hourOfDay, minute ->
                         binding.edtStartTime.text =
                             String.format("%02d:%02d", hourOfDay, minute)
                     }, true)
                 } else binding.edtStartDate.setText(dateString)*/
            }
        }
    }

    private fun setEndDate(): DatePickerDialog.OnDateSetListener {
        return object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker?,
                year: Int,
                month: Int,
                dayOfMonth: Int
            ) {

                val calendar: Calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                calendar.set(year, month, dayOfMonth)
                val dateString: String = sdf.format(calendar.time)
                selectedEndDate = dateString

                this@CreateEventActivity.endDate = dayOfMonth
                this@CreateEventActivity.endMonth = month + 1
                this@CreateEventActivity.endYear = year

                binding.edtEndDate.setText(dateString)
            }
        }
    }

    private fun getAllImages() {
        Album.initialize(
            AlbumConfig.newBuilder(this)
                .setAlbumLoader(MediaLoader())
                .build()
        )
        Album.image(this) // Image selection.
            .multipleChoice()
            .camera(true)
            .selectCount(4)
            .columnCount(3)
            .checkedList(albumList)
            .onResult { result ->
                albumList= ArrayList()
                albumList.addAll(result)
                var tempOldImageList = oldImageList
                if(result.size > 0){
                    oldImageList = ArrayList()
                    imageList = ArrayList()
                }
                for (image in result) {
                    imageList.add(image?.path?.let { Image(it) })
                    oldImageList.add(image?.path?.let { Image(it) })
                }
                photoAdapter?.setData(oldImageList)
            }
            .onCancel { }
            .start()
    }

    private fun event() {
        showProgressDialog()
        val params = HashMap<String, RequestBody?>()
        params["id"] = event?.id.toString().toRequestBody()
        params["name"] = binding.edtName.text.toString().toRequestBody()
        params["note"] = binding.edtNote.text.toString().toRequestBody()
        params["people"] =
            binding.edtPeopleName.convertTagSpanToString(binding.edtPeopleName.tags).toRequestBody()
        params["is_all_day"] = ("1".takeIf { binding.checkAllDay.isChecked } ?: "0").toRequestBody()
        params["anniversary_id"] = anniversaryId.toString().toRequestBody().takeIf { anniversaryId.toString() != "null" } ?: "0".toRequestBody()
        params["lang_code"] = when (AppHelper.getAppLanguage()) {
            Constants.ENGLISH -> {
                "en".toRequestBody()
            }
            Constants.JAPANESE -> {
                "ja".toRequestBody()
            }
            Constants.TRADITIONAL_CHINESE -> {
                "zh-Hant".toRequestBody()
            }
            Constants.SIMPLIFIED_CHINESE -> {
                "zh-Hans".toRequestBody()
            }
            else -> {
                "en".toRequestBody()
            }

        }

        val photoList = arrayOfNulls<MultipartBody.Part>(imageList.size)
        val dateList = arrayOfNulls<RequestBody>(2)
        val timeList = arrayOfNulls<RequestBody>(2)
        val removePhotoList = arrayOfNulls<Long>(removeImageList.size)

        for (index in 0 until imageList.size) {

            val file = File(
                imageList[index]?.image.toString()
            )
            val surveyBody = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                file
            )
            photoList[index] = MultipartBody.Part.createFormData(
                "event_photo[]",
                file.name,
                surveyBody
            )

        }

        for (index in removeImageList.indices) {

            removePhotoList[index] = removeImageList[index]
        }

        dateList[0] = binding.edtStartDate.text.toString().toRequestBody()
        timeList[0] = selectedStartTime.toString().toRequestBody()

        dateList[1] = binding.edtEndDate.text.toString().toRequestBody()
        timeList[1] = selectedEndTime.toString().toRequestBody()


        if (event == null) {
            createEvent(params, photoList, dateList, timeList)
        } else {
            updateEvent(params, photoList, dateList, timeList, removePhotoList)
        }
    }

    private fun createEvent(
        params: HashMap<String, RequestBody?>,
        photoList: Array<MultipartBody.Part?>,
        dateList: Array<RequestBody?>,
        timeList: Array<RequestBody?>
    ) {
        NetworkCall.createEvent(
            params,
            photoList,
            dateList,
            timeList,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {
                        if (anniversary == null) {
                            val intent =
                                Intent(this@CreateEventActivity, EventDetailActivity::class.java)
                            intent.putExtra("event", response?.data?.id)
                            startActivity(intent)
                        } else {
                            intent.putExtra("event", response?.data)
                            setResult(RESULT_OK)
                        }
                        finish()
                    } else DialogHelper.newInstance(message).show(this@CreateEventActivity)
                }
            })
    }

    private fun updateEvent(
        params: HashMap<String, RequestBody?>,
        photoList: Array<MultipartBody.Part?>,
        dateList: Array<RequestBody?>,
        timeList: Array<RequestBody?>,
        removePhotoList: Array<Long?>?
    ) {
        NetworkCall.updateEvent(
            params,
            photoList,
            dateList,
            timeList,
            removePhotoList,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {
                        val intent =
                            Intent(this@CreateEventActivity, EventDetailActivity::class.java)
                        intent.putExtra("event", response?.data?.id)
                        setResult(RESULT_OK)
                        finish()
                    } else DialogHelper.newInstance(message).show(this@CreateEventActivity)
                }
            })
    }

    private fun setData(data: AnniversaryEvent?) {
        if (data != null) {
            showProgressDialog()
            setUpToolBar(getString(R.string.update_an_event), true)
            binding.eventToolbar.btnSave.visibility = View.VISIBLE
            binding.eventToolbar.btnSave.text = getString(R.string.done)

            val month = SimpleDateFormat("MM", Locale.ENGLISH)
            val day = SimpleDateFormat("dd", Locale.ENGLISH)
            val year = SimpleDateFormat("yyyy", Locale.ENGLISH)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            if (data.eventDates?.isNotEmpty() == true) {
                val startDate = sdf.parse(data.eventDates?.get(0)?.eventDate!!)

                this.startMonth = month.format(startDate!!).toInt()
                this.startDate = day.format(startDate).toInt()
                this.startYear = year.format(startDate).toInt()

                val endDate = sdf.parse(data.eventDates?.get(1)?.eventDate!!)

                this.endMonth = month.format(endDate!!).toInt()
                this.endDate = day.format(endDate).toInt()
                this.endYear = year.format(endDate).toInt()


                selectedStartDate = data.date
                selectedEndDate = data.eventEndDate

                selectedStartTime = data.eventFormatTime
                selectedEndTime = data.eventFormatEndTime

                binding.edtStartDate.setText(selectedStartDate)
                binding.edtStartTime.setText(selectedStartTime)

                binding.edtEndDate.setText(selectedEndDate)
                binding.edtEndTime.setText(selectedEndTime)
            }



            anniversaryId = data.anniversaryId
            binding.edtAnniName.setText(data.anniversaryName)
            binding.edtName.setText(data.name)

            binding.edtPeopleName.convertStringToTagSpan(data.people)

            binding.edtNote.setText(data.note)

            binding.checkAllDay.isChecked = true.takeIf { data.isAllDay == "1" } ?: false

            oldImageList.clear()

            data.photos?.let { oldImageList.addAll(it) }

            photoAdapter?.setData(oldImageList)

            if (data.isAllDay == "0") {
                binding.edtEndTime.setOnClickListener(this)
                binding.edtStartTime.setOnClickListener(this)
            } else {
                binding.edtEndTime.setOnClickListener(null)
                binding.edtStartTime.setOnClickListener(null)
                binding.edtStartTime.setText("")
                binding.edtEndTime.setText("")
                selectedStartTime = ""
                selectedEndTime = ""
            }
            hideProgressDialog()
        }
    }

    fun showTime(h: Int, min: Int): String {
        val format: String
        var hour = h
        when {
            hour == 0 -> {
                hour += 12
                format = getString(R.string.am)
            }
            hour == 12 -> {
                format = getString(R.string.pm)
            }
            hour > 12 -> {
                hour -= 12
                format = getString(R.string.pm)
            }
            else -> {
                format = getString(R.string.am)
            }
        }
        return StringBuilder().append(String.format("%02d:%02d", hour, min)).append(" ")
            .append(format).toString()
    }

    private fun validate(): Boolean {

        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtName.text.toString())) {
            Validator.setError(binding.edtName, getString(R.string.please_enter_name))
            isValid = false
        }

        /*if (Validator.isEmptyFieldValidate(binding.edtNote.text.toString())) {
            Validator.setError(binding.edtNote, getString(R.string.please_enter_note))
            isValid = false
        }*/

        if (!isValid) return isValid

        /*if (Validator.isEmptyFieldValidate(binding.edtAnniName.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_select_anniversary)).show(this)
            isValid = false
        }*/

        if (Validator.isEmptyFieldValidate(binding.edtStartDate.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_enter_start_date)).show(this)
            isValid = false
        }

        if (Validator.isEmptyFieldValidate(binding.edtEndDate.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_enter_end_date)).show(this)
            isValid = false
        }
        /*if (!binding.checkAllDay.isChecked) {
            if (Validator.isEmptyFieldValidate(binding.edtStartTime.text.toString())) {
                DialogHelper.newInstance(getString(R.string.please_enter_start_time)).show(this)
                isValid = false
            }
            if (Validator.isEmptyFieldValidate(binding.edtEndTime.text.toString())) {
                DialogHelper.newInstance(getString(R.string.please_enter_end_time)).show(this)
                isValid = false
            }
        }*/
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date1 = sdf.parse(selectedStartDate.toString())
            val date2 = sdf.parse(selectedEndDate.toString())

            if (date2.before(date1)) {
                DialogHelper.newInstance(getString(R.string.please_select_date_which_is_after_start_date))
                    .show(this)
                isValid = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isValid = false
        }

        /*if (Validator.isEmptyFieldValidate(binding.edtPeopleName.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_enter_people)).show(this)
            isValid = false
        }

        if (oldImageList.isEmpty()) {
            DialogHelper.newInstance(getString(R.string.Please_select_an_image)).show(this)
            isValid = false
        }*/

        return isValid
    }

    private fun onItemClick(position: Int) {
        removeImageList.add(oldImageList[position]?.id)
        if (oldImageList[position]?.id == 0.toLong()) {
            imageList.remove(oldImageList[position])
        }
        oldImageList.removeAt(position)
        photoAdapter?.setData(oldImageList)
    }

    /*private fun getPeopleList(str: String?): Int {
        if (str.isNullOrEmpty()) return 0

        val people: List<List<String>> = listOf(str.split(","))

        for (i in people.first()) {
            if (i.isNotEmpty()) {
                peopleList.add(i)
            }
        }
//        peopleTagAdapter!!.setItem(peopleList)

        return peopleList.size

    }

    private fun setPeopleListToString(): String {
        var people = StringBuilder()
        for (i in peopleList) {
            people = people.append("$i,")
        }

        return people.toString()
    }*/

    /*private fun onPeopleClick(position: Int, data: String) {
        peopleList.removeAt(position)
        peopleTagAdapter?.setItem(peopleList)
    }*/

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1001 -> {
                if (data != null) {
//                    if (intent.getSerializableExtra("list")) {
                    val imageList = data.getSerializableExtra("list") as ArrayList<GridViewItem>
                    photoList.addAll(imageList)
//                    }
                }
                if (photoList.isNotEmpty()) {
//                    adapterAdd?.setData(photoList)
                }
            }
        }
    }*/

    /*private fun uploadPhoto() {
        val bottomSheetFilePicker = BottomSheetFilePicker(BuildConfig.APPLICATION_ID)

        bottomSheetFilePicker.actionButtonBg = R.drawable.button_bg_rounded
        bottomSheetFilePicker.cancelButtonBg = R.drawable.button_red_bg_rounded
        bottomSheetFilePicker.actionButtonTextColor = R.color.colorPrimary
        bottomSheetFilePicker.cancelButtonTextColor = R.color.white

        bottomSheetFilePicker.setMediaListenerCallback(
            BottomSheetFilePicker.IMAGE,
            object : MediaPickerCallback {
                override fun onPickedSuccess(media: Media?) {
//                    use media object for get your file information like path, image url, thumb url

                    if (media != null) {
                        media.url?.let { photoList.add(it) }
                        adapterAdd?.setData(photoList)
                    }
                }

                override fun onPickedError(error: String?) {
//                    handle file pick error
                }

                override fun showProgressBar(enable: Boolean) {
//                    show progressbar if you want
                }
            })

        bottomSheetFilePicker.show(supportFragmentManager, "image")
    }*/
}
