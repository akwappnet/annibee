package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.system.Os.remove
import android.view.View
import android.widget.DatePicker
import androidx.recyclerview.widget.GridLayoutManager
import com.devstree.annibee.Controller
import com.devstree.annibee.R
import com.devstree.annibee.adapter.AddPhotoAdapter
import com.devstree.annibee.adapter.AlertNotificationAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.databinding.ActivityCreateAnniversaryBinding
import com.devstree.annibee.dialog.DialogHelper
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.model.response_model.AlertNotification
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
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CreateAnniversaryActivity : NavigationActivity(), DatePickerDialog.OnDateSetListener,
    View.OnClickListener {


    lateinit var binding: ActivityCreateAnniversaryBinding
    private lateinit var adapter: AlertNotificationAdapter
    var photoAdapter: AddPhotoAdapter? = null
//    var peopleTagAdapter: PeopleTagAdapter? = null

    var date: Int? = 0
    var month: Int? = 0
    var year: Int? = 0

    var list = ArrayList<Attribute>()
    var imageList = ArrayList<Image?>()
    var oldImageList = ArrayList<Image?>()
    var removeImageList = ArrayList<Long?>()
    var notificationList = ArrayList<AlertNotification>()
    var peopleList = ArrayList<String>()

    private var attributeId: Long? = 0

    private var anniversary: AnniversaryEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anniversary = intent.getParcelableExtra("anniversary")

        binding = ActivityCreateAnniversaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(getString(R.string.create_an_anniversary), true)

        binding.lnlMain.setOnTouchListener(object : OnSwipeTouchListener(this) {

            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()

            }
        })

        binding.anniToolbar.btnSave.visibility = View.VISIBLE
        binding.anniToolbar.btnSave.setOnClickListener(this)
        binding.addImage.setOnClickListener(this)

        date = Controller.instance.currentDate.day
        month = Controller.instance.currentDate.month
        year = Controller.instance.currentDate.year

        val dateOfAnniversary = String.format("%04d-%02d-%02d", year, month, date)

        binding.edtDate.setText(dateOfAnniversary)

        adapter = AlertNotificationAdapter()
        adapter.setOnItemClickListener(object : AlertNotificationAdapter.ClickListener {
            override fun onItemClick(notification: AlertNotification, checked: Boolean) {
                if (checked) notificationList.add(notification)
                else notificationList.remove(notification)
            }

        })
        binding.rvNotification.adapter = adapter

        binding.rvAddPhoto.layoutManager = GridLayoutManager(this, 3)
        photoAdapter = AddPhotoAdapter(this::onItemClick)
        binding.rvAddPhoto.adapter = photoAdapter


        /* peopleTagAdapter = PeopleTagAdapter(this::onPeopleClick)
         binding.recyclerViewPeople.adapter = peopleTagAdapter*/

        binding.edtDate.setOnClickListener {
            /*if (date == 0) {
                openDatePickerDialog(this)
            } else */
            openDatePickerDialog(this, date!!, month!! - 1, year!!)
        }
        binding.edtAttribute.setOnClickListener(this)


        if (isNetworkAvailable(this)) {
            getAttribute()
            getAlertNotification()
        } else {
            noNetWorkAvailable()
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun getAlertNotification() {
        /* NetworkCall.alertNotification(
             AppHelper.getDefaultParam(this),
             object : BaseResponseListener<ResponseBody<List<AlertNotification>>>() {
                 override fun result(response: ResponseBody<List<AlertNotification>>?) {
                     hideProgressDialog()
                     if (success) {
                         adapter.setData(response?.data)

                         setData(anniversary, response?.data)
                     }
                 }
             }
         )*/
        val alertNotification = ArrayList<AlertNotification>()
        alertNotification.add(
            AlertNotification(
                1.toLong(),
                getString(R.string._4_weeks_ago),
                28,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                2.toLong(),
                getString(R.string._3_weeks_ago),
                21,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                3.toLong(),
                getString(R.string._2_weeks_ago),
                14,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                4.toLong(),
                getString(R.string._1_weeks_ago),
                7,
                false
            )
        )
        alertNotification.add(
            AlertNotification(
                5.toLong(),
                getString(R.string.the_day_before),
                1,
                false
            )
        )
        alertNotification.add(AlertNotification(6.toLong(), getString(R.string.that_day), 0, false))

        adapter.setData(alertNotification)
        setData(anniversary, alertNotification)
    }

    private fun getAttribute() {
        NetworkCall.attribute(
            AppHelper.getDefaultParam(),
            object : BaseResponseListener<ResponseBody<List<Attribute>>>() {
                override fun result(response: ResponseBody<List<Attribute>>?) {
                    if (success) {
                        list = response?.data as ArrayList<Attribute>

//                        for (item in response?.data as ArrayList<Attribute>){
//                            if(item.enName.toString() != "Child's Growth"){
//                                list.add(item)
//                            }
//                        }
                    }
                }

            }
        )
    }

    override fun onClick(v: View) {
        when (v) {
            binding.edtAttribute -> {
                showAttribute(this, binding.edtAttribute, list, true, object :
                    ObjectCallback<Attribute> {
                    override fun response(obj: Attribute?) {
                        Log.e(" showAlertMessage(list.toString())",obj.toString())
                        if (obj == null) return
                        /*   if (!obj.parentName.isNullOrEmpty()) {
                               showDefaultAttribute()
                           } else {*/
                        when (AppHelper.getAppLanguage()) {
                            Constants.ENGLISH -> {
                                binding.edtAttribute.setText(obj.enName)
                            }
                            Constants.JAPANESE -> {
                                binding.edtAttribute.setText(obj.jaName)
                            }
                            Constants.SIMPLIFIED_CHINESE -> {
                                binding.edtAttribute.setText(obj.ch1Name)
                            }
                            Constants.TRADITIONAL_CHINESE -> {
                                binding.edtAttribute.setText(obj.ch2Name)
                            }
                        }
                        attributeId = obj.id

                        if (attributeId == 1.toLong()) {
                            binding.txtDefaultAttribute.visibility = View.VISIBLE
                            binding.edtDefaultAttribute.visibility = View.VISIBLE
                        } else {
                            binding.txtDefaultAttribute.visibility = View.GONE
                            binding.edtDefaultAttribute.visibility = View.GONE
                            binding.edtDefaultAttribute.text = null
                        }
                    }
                })
            }
            binding.addImage -> {
                /*val intent = Intent(this, ImageActivity::class.java)
                startActivityForResult(intent, 1001)*/
                getAllImages()
            }
            binding.anniToolbar.btnSave -> {
                if (validate()) {
                    if (isNetworkAvailable(this)) {
                        anniversary()
                    } else {
                        noNetWorkAvailable()
                    }
                }
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
            .selectCount(1)
            .columnCount(3)
            .onResult(object : Action<ArrayList<AlbumFile?>?> {
                override fun onAction(result: ArrayList<AlbumFile?>) {
                    imageList.clear()
                    oldImageList.clear()
                    for (image in result) {
                        imageList.add(image?.path?.let { Image(it) })
                        oldImageList.add(image?.path?.let { Image(it) })
                    }
                    photoAdapter?.setData(oldImageList)
                }
            })
            .onCancel(object : Action<String?> {
                override fun onAction(result: String) {

                }
            })
            .start()
    }

    private fun anniversary() {
        showProgressDialog()
        val params = HashMap<String, RequestBody?>()
        params["id"] = anniversary?.id.toString().toRequestBody()
        params["name"] = binding.edtName.text.toString().toRequestBody()
        params["date"] = binding.edtDate.text.toString().toRequestBody()
        params["attribute_id"] = attributeId.toString().toRequestBody()
        params["note"] = binding.edtNote.text.toString().toRequestBody()
        params["people"] = binding.edtPeopleName.convertTagSpanToString(binding.edtPeopleName.tags).toRequestBody()
        params["attribute_name"] = binding.edtAttribute.text.toString().toRequestBody()
        params["default_attribute_name"] =
            binding.edtDefaultAttribute.text.toString().toRequestBody()
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
        val list = arrayOfNulls<Long>(notificationList.size)
        val removePhotoList = arrayOfNulls<Long>(removeImageList.size)

        for (index in 0 until imageList.size) {

            val file = File(
                imageList[index]?.image.toString()
            )
            val surveyBody = file
                .asRequestBody("*/*".toMediaTypeOrNull())
            photoList[index] = MultipartBody.Part.createFormData(
                "anniversary_photo[]",
                file.name,
                surveyBody
            )

        }

        for (index in notificationList.indices) {
            list[index] = notificationList[index].id
        }

        for (index in removeImageList.indices) {
            removePhotoList[index] = removeImageList[index]
        }

        if (anniversary == null) {
            createAnniversary(params, photoList, list)
        } else {
            updateAnniversary(params, photoList, list, removePhotoList)
        }


    }

    private fun createAnniversary(
        params: java.util.HashMap<String, RequestBody?>,
        photoList: Array<MultipartBody.Part?>,
        list: Array<Long?>
    ) {
        NetworkCall.createAnniversary(
            params,
            photoList,
            list,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {
                        val intent = Intent(
                            this@CreateAnniversaryActivity,
                            AnniversaryDetailActivity::class.java
                        )
                        intent.putExtra("anniversary", response?.data?.id)
                        startActivity(intent)
                        finish()
                    } else DialogHelper.newInstance(message).show(this@CreateAnniversaryActivity)
                }

            })
    }

    private fun updateAnniversary(
        params: java.util.HashMap<String, RequestBody?>,
        photoList: Array<MultipartBody.Part?>,
        notificationList: Array<Long?>,
        removeImageList: Array<Long?>?
    ) {
        NetworkCall.updateAnniversary(
            params,
            photoList,
            notificationList,
            removeImageList,
            object : BaseResponseListener<ResponseBody<AnniversaryEvent>>() {
                override fun result(response: ResponseBody<AnniversaryEvent>?) {
                    hideProgressDialog()
                    if (success) {

                        val intent = Intent(
                            this@CreateAnniversaryActivity,
                            AnniversaryDetailActivity::class.java
                        )
                        Log.e(response?.data.toString())
                        intent.putExtra("anniversary", response?.data?.id)
                        intent.putExtra("anniversary_obj", response?.data)
                        setResult(RESULT_OK)
                        finish()
                    } else DialogHelper.newInstance(message).show(this@CreateAnniversaryActivity)
                }

            })
    }

    private fun setData(data: AnniversaryEvent?, notifications: List<AlertNotification>?) {
        if (data != null) {
            showProgressDialog()
            setUpToolBar(getString(R.string.update_an_anniversary), true)
            binding.anniToolbar.btnSave.visibility = View.VISIBLE
            binding.anniToolbar.btnSave.text = getString(R.string.done)

            val month = SimpleDateFormat("MM", Locale.ENGLISH)
            val day = SimpleDateFormat("dd", Locale.ENGLISH)
            val year = SimpleDateFormat("yyyy", Locale.ENGLISH)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            val date = sdf.parse(data.date!!)

            this.month = month.format(date!!).toInt()
            this.date = day.format(date).toInt()
            this.year = year.format(date).toInt()

            binding.edtName.setText(data.name)
            binding.edtDate.setText(data.date)
            binding.edtAttribute.setText(data.attributeName)

            if (data.defaultAttributeName?.isNotEmpty() == true) {
                binding.txtDefaultAttribute.visibility = View.VISIBLE
                binding.edtDefaultAttribute.visibility = View.VISIBLE

                binding.edtDefaultAttribute.setText(data.defaultAttributeName)
            }

            binding.edtNote.setText(data.note)

            getPeopleList(data.people)

            binding.edtPeopleName.convertStringToTagSpan(data.people)

            attributeId = data.attributeId?.toLong()

            oldImageList.clear()

            data.photos?.let { oldImageList.addAll(it) }

            photoAdapter?.setData(oldImageList)

            for (item in data.notifications!!) {
                if (notifications != null) {
                    for (notification in notifications)
                        if (item.id == notification.id) {
                            notification.isChecked = true
                            notificationList.add(notification)
                        }
                }
            }
            adapter.notifyDataSetChanged()

            hideProgressDialog()
        }
    }

    private fun getPeopleList(str: String?): Int {
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
    }

    private fun validate(): Boolean {

        var isValid = true

        if (Validator.isEmptyFieldValidate(binding.edtName.text.toString())) {
            Validator.setError(
                binding.edtName,
                getString(R.string.please_enter_name_of_anniversary)
            )
            isValid = false
        }
        /*if (Validator.isEmptyFieldValidate(binding.edtNote.text.toString())) {
            Validator.setError(binding.edtNote, getString(R.string.please_enter_note))
            isValid = false
        }*/

        if (!isValid) return isValid


        if (Validator.isEmptyFieldValidate(binding.edtDate.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_enter_date_of_anniversary))
                .show(this)
            isValid = false
        }
        /*if (Validator.isEmptyFieldValidate(binding.edtAttribute.text.toString())) {
            DialogHelper.newInstance(getString(R.string.please_enter_anniversary_attribute))
                .show(this)
            isValid = false
        }

        if (oldImageList.isEmpty()) {
            DialogHelper.newInstance(getString(R.string.Please_select_an_image)).show(this)
            isValid = false
        }*/

        return isValid
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val calendar: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        calendar.set(year, month, dayOfMonth)
        val dateString: String = sdf.format(calendar.time)
        binding.edtDate.setText(dateString)

        date = dayOfMonth
        this.month = month + 1
        this.year = year
    }

    private fun onItemClick(position: Int) {
        removeImageList.add(oldImageList[position]?.id)
        if (oldImageList[position]?.id == 0.toLong()) {
            imageList.remove(oldImageList[position])
        }
        oldImageList.removeAt(position)
        photoAdapter?.setData(oldImageList)
    }

    /*private fun onPeopleClick(position: Int, data: String) {
        peopleList.removeAt(position)
        peopleTagAdapter?.setItem(peopleList)
    }*/

    /*private fun addAttribute(): ArrayList<PopUpModel> {
        list.add(PopUpModel("1", "Any"))
        list.add(PopUpModel("2", "Birthday"))
        list.add(PopUpModel("3", "Default Day"))
        list.add(PopUpModel("4", "Wedding Anniversary"))
        list.add(PopUpModel("5", "Anniversary of Establishment"))
        list.add(PopUpModel("6", "Engagement Anniversary"))
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1001 -> {
                if (data != null) {
                    val imageList = data.getParcelableArrayListExtra<GridViewItem>("list")
                    if (imageList != null) {
                        photoList.addAll(imageList)
                    }
                    if (photoList.isNotEmpty()) {
//                        photoAdapter?.setData(photoList)
                    }
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
    }
        private fun showDefaultAttribute() {

        val attributes = ArrayList<PopUpModel>()

        for (item in list) {
            if (!item.parentName.isNullOrEmpty()) {
                attributes.add(PopUpModel(item.id.toString(), item.name.toString()))
            }
        }

        showPopupMenu(
            this@CreateAnniversaryActivity,
            binding.edtAttribute,
            attributes,
            true,
            object : ObjectCallback<PopUpModel> {
                override fun response(obj: PopUpModel?) {
                    if (obj == null) return
                    binding.edtAttribute.setText(obj.title)
                    attributeId = obj.id.toLong()
                }

            }
        )
    }
     */
}