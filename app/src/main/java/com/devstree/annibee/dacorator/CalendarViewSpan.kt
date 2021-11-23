package com.devstree.annibee.dacorator

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.LineBackgroundSpan
import com.devstree.annibee.model.response_model.CalendarData
import java.text.ParseException
import java.util.*


internal class CalendarViewSpan(
    var textList: ArrayList<CalendarData>/*, val color: Int*/
) :
    LineBackgroundSpan {

    override fun drawBackground(
        c: Canvas, p: Paint, left: Int, right: Int, top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int, lnum: Int
    ) {
        var marginBottom = bottom + 20
        for (dateEventModel in textList) {
            try {
                if (dateEventModel.date.toString() == text.toString()) {

                    for (event in dateEventModel.items!!) {
                        val mPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
                        if (event.type == "1") mPaint.color = Color.BLUE
                        else if (event.type == "2") mPaint.color = Color.parseColor("#964B00")
                        else mPaint.color = Color.parseColor("#467C24")
                        mPaint.strokeWidth = 0F
                        mPaint.style = Paint.Style.FILL
                        mPaint.textSize = 22F

                        val xPos: Float = c.width.toFloat()

                        val eventName = event.name
                        val textPaint = TextPaint(mPaint)

                        /*val name = if (eventName?.length!! >= 5) {
                            val textName = eventName.subSequence(0, 5)
                            TextUtils.ellipsize(
                                textName, textPaint,
                                xPos - 7, TextUtils.TruncateAt.END, true, null
                            )
                        }else { TextUtils.ellipsize(
                            eventName, textPaint,
                            xPos - 7, TextUtils.TruncateAt.END
                        )
                        }*/

                        val name = TextUtils.ellipsize(
                            event.name, textPaint,
                            xPos - 10, TextUtils.TruncateAt.END
                        )

//                        textPaint.textAlign = Paint.Align.LEFT
//                        val xPos: Int = c.width / 2

                        c.drawText(name.toString(), 10F, marginBottom.toFloat(), textPaint)
                        marginBottom += 25
                    }
                    if (dateEventModel.items!!.size >= 5) {
                        val paint = Paint()
                        paint.color = Color.BLUE
                        c.drawCircle(
                            ((left + right) / 2) + 40.toFloat(),
                            (bottom / 2).toFloat(),
                            5f,
                            paint
                        )
                    }
                }

            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }
}