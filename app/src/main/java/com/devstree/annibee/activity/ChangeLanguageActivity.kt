package com.devstree.annibee.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityChangeLanguageBinding
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants

class ChangeLanguageActivity : NavigationActivity() {

    lateinit var binding: ActivityChangeLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {
        setUpToolBar(getString(R.string.change_language), true)

        binding.container.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })

        val language = AppHelper.getAppLanguage()
        when (language) {
            Constants.ENGLISH -> {
                binding.rbEnglish.isChecked = true
            }
            Constants.JAPANESE -> {
                binding.rbJapanese.isChecked = true
            }
            Constants.TRADITIONAL_CHINESE -> {
                binding.rbTraditionalChinese.isChecked = true
            }
            Constants.SIMPLIFIED_CHINESE -> {
                binding.rbSimplifiedChinese.isChecked = true
            }
        }

        binding.rbEnglish.setOnClickListener {
/*            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("language", Constants.ENGLISH)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)*/
            val intent = Intent()
            intent.putExtra("language", Constants.ENGLISH)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.rbJapanese.setOnClickListener {
            /*val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("language", Constants.JAPANESE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)*/
            val intent = Intent()
            intent.putExtra("language", Constants.JAPANESE)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.rbTraditionalChinese.setOnClickListener {
            /*val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("language", Constants.JAPANESE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)*/
            val intent = Intent()
            intent.putExtra("language", Constants.TRADITIONAL_CHINESE)
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.rbSimplifiedChinese.setOnClickListener {
            /*val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("language", Constants.JAPANESE)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)*/
            val intent = Intent()
            intent.putExtra("language", Constants.SIMPLIFIED_CHINESE)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}