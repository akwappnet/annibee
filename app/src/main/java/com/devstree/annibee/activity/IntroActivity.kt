package com.devstree.annibee.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.devstree.annibee.R
import com.devstree.annibee.common.NavigationActivity
import com.devstree.annibee.common.ViewPagerAdapter
import com.devstree.annibee.databinding.ActivityIntroBinding
import com.devstree.annibee.fragment.IntroFragment1
import com.devstree.annibee.fragment.IntroFragment2
import com.devstree.annibee.fragment.IntroFragment3
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

class IntroActivity : NavigationActivity() {

    lateinit var binding: ActivityIntroBinding
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenActivity()
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initUi() {
        setPagerIndicatorWithViewpager()
    }

    private fun setPagerIndicatorWithViewpager() {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.add(IntroFragment1())
        pagerAdapter.add(IntroFragment2())
        pagerAdapter.add(IntroFragment3())
        binding.viewPager.adapter = pagerAdapter

        binding.pageIndicator.apply {
            setSliderColor(
                ContextCompat.getColor(mContext, R.color.light_pink),
                ContextCompat.getColor(mContext, R.color.colorPrimary)
            )
            setSliderWidth(resources.getDimension(R.dimen._8sdp))
            setSliderHeight(resources.getDimension(R.dimen._8sdp))
            setSlideMode(IndicatorSlideMode.SCALE)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setupWithViewPager(binding.viewPager)
            setPageSize(binding.viewPager.adapter!!.count)
            notifyDataChanged()
        }

        binding.viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 0) {

                }
                else if (position == 1) {

                }
                else if (position == 2) {

                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    fun onClick(v: View) {
        when (v) {
            binding.btnSignUp -> {
                openSignUpActivity()
            }
            binding.btnLogin -> {
                openLoginActivity()
            }
        }
    }
}