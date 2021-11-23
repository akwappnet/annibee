package com.devstree.annibee.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.databinding.ActivityWebBinding
import com.devstree.annibee.listener.OnSwipeTouchListener
import com.devstree.annibee.utility.AppHelper
import com.devstree.annibee.utility.Constants
import com.devstree.annibee.utility.Utils

class WebActivity : NavigationActivity() {

    lateinit var binding: ActivityWebBinding
    var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initUi() {

        url = intent.getStringExtra("url")

        binding.webView.setOnTouchListener(object : OnSwipeTouchListener(this) {
            override fun onSwipeRight() {
                super.onSwipeRight()
                onBackPressed()
            }
        })

        if (url == Constants.TERMS || url == Constants.TERMS_JAPANESE) {
            setUpToolBar(getString(R.string.user_policy), true)
            binding.webToolbar.imgBack.setImageResource(R.drawable.close)
        }
        else {
            setUpToolBar(getString(R.string.privacy_policy), true)
            binding.webToolbar.imgBack.setImageResource(R.drawable.close)
        }

        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.overScrollMode = WebView.OVER_SCROLL_NEVER

        openPage()

        binding.webToolbar.imgBack.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun openPage() {

        if (!isNetworkAvailable(this)) {
            binding.progressBar.visibility = View.GONE
            showAlertMessage(getString(R.string.no_internet_connection))
            return
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
            override fun onPageFinished(view: WebView, url: String) {
                binding.progressBar.visibility = View.GONE
            }

        }
        binding.webView.loadUrl(url.toString())
    }
}