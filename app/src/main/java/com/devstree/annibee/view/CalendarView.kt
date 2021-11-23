package com.devstree.annibee.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.google.android.material.internal.ViewUtils.dpToPx


open class CalendarView(context: Context) : ViewGroup(context) {

   override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val specHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        val specHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        //We need to disregard padding for a while. This will be added back later
        val desiredWidth: Int = specWidthSize - getPaddingLeft() - getPaddingRight()
        val desiredHeight: Int = specHeightSize  /*getPaddingTop()*/ - getPaddingBottom()
//        val weekCount: Int = getWeekCountBasedOnMode()
        val viewTileHeight = 7

        //Calculate independent tile sizes for later
        val desiredTileWidth = desiredWidth / DEFAULT_DAYS_IN_WEEK
        val desiredTileHeight = desiredHeight / viewTileHeight
        var measureTileSize = -1
        var measureTileWidth = -1
        var measureTileHeight = -1
       /* if (this.tileWidth != INVALID_TILE_DIMENSION || this.tileHeight != INVALID_TILE_DIMENSION) {
            if (this.tileWidth > 0) {
                //We have a tileWidth set, we should use that
                measureTileWidth = this.tileWidth
            } else {
                measureTileWidth = desiredTileWidth
            }
            if (this.tileHeight > 0) {
                //We have a tileHeight set, we should use that
                measureTileHeight = this.tileHeight
            } else {
                measureTileHeight = desiredTileHeight
            }
        } else if (specWidthMode == MeasureSpec.EXACTLY || specWidthMode == MeasureSpec.AT_MOST) {
            measureTileSize = if (specHeightMode == MeasureSpec.EXACTLY) {
                //Pick the smaller of the two explicit sizes
                Math.min(desiredTileWidth, desiredTileHeight)
            } else {
                //Be the width size the user wants
                desiredTileWidth
            }
        } else if (specHeightMode == MeasureSpec.EXACTLY || specHeightMode == MeasureSpec.AT_MOST) {
            //Be the height size the user wants
            measureTileSize = desiredTileHeight
        }
        if (measureTileSize > 0) {
            //Use measureTileSize if set
            measureTileHeight = measureTileSize
            measureTileWidth = measureTileSize
        } else if (measureTileSize <= 0) {
            if (measureTileWidth <= 0) {
                //Set width to default if no value were set
                measureTileWidth = dpToPx(DEFAULT_TILE_SIZE_DP).toInt()
            }
            if (measureTileHeight <= 0) {
                //Set height to default if no value were set
                measureTileHeight = dpToPx(DEFAULT_TILE_SIZE_DP)
            }
        }*/

        //Calculate our size based off our measured tile size
        var measuredWidth = measureTileWidth * DEFAULT_DAYS_IN_WEEK
        var measuredHeight = measureTileHeight * viewTileHeight

        //Put padding back in from when we took it away
        measuredWidth += getPaddingLeft() + getPaddingRight()
        measuredHeight += getPaddingTop() + getPaddingBottom()

        //Contract fulfilled, setting out measurements
        setMeasuredDimension( //We clamp inline because we want to use un-clamped versions on the children
            clampSize(measuredWidth, widthMeasureSpec),
            clampSize(measuredHeight, heightMeasureSpec)
        )
        val count: Int = getChildCount()
        for (i in 0 until count) {
            val child: View = getChildAt(i)
            val p = child.layoutParams as LayoutParams
           /* val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                DEFAULT_DAYS_IN_WEEK * measureTileWidth,
                MeasureSpec.EXACTLY
            )
            val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                p.height * measureTileHeight,
                MeasureSpec.EXACTLY
            )
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)*/
        }
    }

//    open fun getWeekCountBasedOnMode(): Int {
//        var weekCount: Int = calendarMode.visibleWeeksCount

//        return weekCount
//    }

    /**
     * Clamp the size to the measure spec.
     *
     * @param size Size we want to be
     * @param spec Measure spec to clamp against
     * @return the appropriate size to pass to [View.setMeasuredDimension]
     */
    open fun clampSize(size: Int, spec: Int): Int {
        val specMode = MeasureSpec.getMode(spec)
        val specSize = MeasureSpec.getSize(spec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> {
                specSize
            }
            MeasureSpec.AT_MOST -> {
                Math.min(size, specSize)
            }
            MeasureSpec.UNSPECIFIED -> {
                size
            }
            else -> {
                size
            }
        }
    }


    companion object {
        const val DEFAULT_TILE_SIZE_DP = 44
        const val DEFAULT_DAYS_IN_WEEK = 7
        const val DEFAULT_MAX_WEEKS = 6
        const val DAY_NAMES_ROW = 1

        const val INVALID_TILE_DIMENSION = -10
    }
}