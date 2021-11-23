package com.devstree.annibee.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.devstree.annibee.R
import com.devstree.annibee.adapter.MyGridAdapter
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.common.ObjectCallback
import com.devstree.annibee.common.PopUpModel
import com.devstree.annibee.databinding.ActivityPhotoBinding
import com.devstree.annibee.model.GridViewItem
import java.io.File


class PhotoActivity : NavigationActivity() {

    private var photoDirectory: String? = null
    lateinit var binding: ActivityPhotoBinding
    val PERMISSION_CODE = 1001
    var projection = arrayOf(MediaStore.MediaColumns.DATA)

    var gridItems: ArrayList<GridViewItem>? = null
    var adapter: MyGridAdapter? = null
    val list = ArrayList<GridViewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initUi() {

        setUpToolBar(getString(R.string.gallery), true)
//        binding.photoToolbar.txtToolbarTitle.visibility = View.GONE
//        binding.photoToolbar.spinner.visibility = View.VISIBLE
        binding.photoToolbar.imgBack.setImageResource(R.drawable.close)
        binding.photoToolbar.imgUpload.visibility = View.VISIBLE
        binding.photoToolbar.imgCamera.visibility = View.VISIBLE
        binding.photoToolbar.txtToolbarTitle.text = getString(R.string.gallery)
        photoDirectory = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera/"
//        photoDirectory = Environment.getExternalStorageDirectory().absolutePath + "/Bluetooth/"

        if (getPermission()) {
//            setPopUpMenu()
            getAllImages()
            setGridAdapter(photoDirectory!!)

        } else {
            requestPermission()
        }

        binding.photoToolbar.imgCamera.setOnClickListener {
            val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Start the activity with camera_intent and request pic id
            startActivityForResult(camera_intent, 1001)
        }

        binding.photoToolbar.imgUpload.setOnClickListener {
            for (image in adapter?.items!!) {
                if (image.isSelected) {
                    list.add(image)
                }
            }
            if (list.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("list", list)
                setResult(RESULT_OK, intent)
                finish()
//                toast(adapter!!.selectedImageList.toString())
            } else {
                toast("Select at least one image")
            }
        }
    }

    private fun getAllImages() {
        gridItems?.clear()
        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val absolutePathOfImage: String =
                    cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
                val item = GridViewItem(null, absolutePathOfImage)
                gridItems?.add(item)
            }
            cursor.close()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val photo = data?.extras?.get("data") as Bitmap?

            val tempUri: Uri? = getImageUri(applicationContext, photo)

            if (tempUri != null) {
                list.add(GridViewItem("1", tempUri.toString(), true))
                val intent = Intent()
                intent.putExtra("list", list)
                setResult(RESULT_OK, intent)
                finish()
//                toast(adapter!!.selectedImageList.toString())
            } /*else {
                toast("Select at least one image")
            }*/
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
        val OutImage = Bitmap.createScaledBitmap(inImage!!, 1000, 1000, true)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            OutImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun setPopUpMenu() {
        binding.photoToolbar.spinner.setOnClickListener {
            showPopupMenu(
                this,
                binding.photoToolbar.spinner,
                photoNameList(),
                true,
                object :
                    ObjectCallback<PopUpModel> {
                    override fun response(obj: PopUpModel?) {
                        if (obj == null) return
                        binding.photoToolbar.spinner.text = obj.title

                        when (obj.title) {
                            getString(R.string.gallery) -> {
                                photoDirectory =
                                    Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera/"
                                setGridAdapter(photoDirectory!!)
                            }
                            getString(R.string.whats_app_images) -> {
                                photoDirectory =
                                    Environment.getExternalStorageDirectory().absolutePath + "/WhatsApp/Media/WhatsApp Images/"
                                setGridAdapter(photoDirectory!!)
                            }
                            getString(R.string.screenshot) -> {
                                photoDirectory =
                                    Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Screenshots/"
                                setGridAdapter(photoDirectory!!)
                            }
                        }
                    }

                })
        }
    }


    private fun getPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestPermission() {
        try {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), PERMISSION_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    photoDirectory?.let { setGridAdapter(it) }
//                    setPopUpMenu()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.permission_required))
                        .setMessage(getString(R.string.permission_required_n_you_can_grant_permission_in_setting))
                        .setPositiveButton(getString(R.string.setting)) { dialog, which ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton(getString(R.string.cancel)) { _, _ -> finish() }
                        .create()
                        .show()
                }
            }
        }
    }


    private fun setGridAdapter(photoDirectory: String) {
        gridItems = createGridItems(photoDirectory) as ArrayList<GridViewItem>

        binding.rvPhotos.layoutManager = GridLayoutManager(this, 3)
        val items = prepareImages()
        if (gridItems != null) {
            adapter = MyGridAdapter(
                this, items /*object : MyGridAdapter.PhotoListener {
                override fun onPhotoClick(path: String) {
                    toast(path)
                }
            }*/
            )
        }

        binding.rvPhotos.adapter = adapter
    }

    fun prepareImages(): ArrayList<GridViewItem> {
        val allImages = ArrayList<GridViewItem>()
        for (i in gridItems!!) {
            allImages.add(GridViewItem(i.title, i.path))
        }
        return allImages
    }

    private fun createGridItems(directoryPath: String): List<GridViewItem> {
        val items: ArrayList<GridViewItem> = ArrayList()

        Log.e("createGridItems: ", File(directoryPath).absolutePath)

        // List all the items within the folder.
        val files: Array<File>? = File(directoryPath).listFiles()

        if (files != null) {
            for (file in files) {

                if (file.absolutePath.endsWith(".jpg") || file.absolutePath.endsWith(".png") ||
                    file.absolutePath.endsWith(".jpeg")
                ) {

                    items.add(GridViewItem(file.name, file.absolutePath))
                }
            }
        }
        return items
    }
}