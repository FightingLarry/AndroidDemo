package com.larry.sampleflowlayout.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 *@author larryycliu on 11/22/21.
 *
 */
class FlowLayout : ViewGroup {

    private val horizontalSpacing: Int = DpUtil.dp2px(10f)
    private val verticalSpacing: Int = DpUtil.dp2px(4f)

    private val allLines: MutableList<MutableList<View>> = mutableListOf()
    private val linesHeight: MutableList<Int> = mutableListOf()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun initLayoutParams() {
        allLines.clear()
        linesHeight.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        initLayoutParams()
        //先测量孩子
        val childCount = childCount
        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        val selfHeight = MeasureSpec.getSize(heightMeasureSpec)

        var parentNeedWidth = 0
        var parentNeedHeight = 0

        var lineViews: MutableList<View> = mutableListOf()//记录折行的所有View
        var lineWidthUsed = 0//记录这行使用的宽
        var lineHeight = 0//行高


        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            //将LayoutParams转换为MeasureSpec
            val childLayoutParams = childView.layoutParams
            val childWidthMeasureSpec =
                getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingLeft + paddingRight,
                    childLayoutParams.width
                )
            val childHeightMeasureSpec =
                getChildMeasureSpec(
                    heightMeasureSpec,
                    paddingTop + paddingBottom,
                    childLayoutParams.height
                )
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            //获得子view的宽高
            val childMeasureWidth = childView.measuredWidth
            val childMeasureHeight = childView.measuredHeight

            //换行
            if (lineWidthUsed + childMeasureWidth + horizontalSpacing > selfWidth) {
                parentNeedWidth = Math.max(parentNeedWidth, lineWidthUsed + horizontalSpacing)
                parentNeedHeight += lineHeight + verticalSpacing
                allLines.add(lineViews)
                linesHeight.add(lineHeight)

                lineViews = mutableListOf()
                lineWidthUsed = 0
                lineHeight = 0
            }

            lineViews.add(childView)
            lineWidthUsed += childMeasureWidth + horizontalSpacing
            lineHeight = Math.max(lineHeight, childMeasureHeight)

            if (i == childCount - 1) {
                parentNeedWidth = Math.max(parentNeedWidth, lineWidthUsed + horizontalSpacing)
                parentNeedHeight += lineHeight + verticalSpacing
                allLines.add(lineViews)
                linesHeight.add(lineHeight)
            }
        }

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val realWidth = if (widthMode == MeasureSpec.EXACTLY) selfWidth else parentNeedWidth
        val realHeight = if (heightMode == MeasureSpec.EXACTLY) selfHeight else parentNeedHeight

        setMeasuredDimension(realWidth, realHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val lineCount = allLines.size
        var curLeft = paddingLeft
        var curTop = paddingTop
        for (i in 0 until lineCount) {
            val lineViews = allLines[i]
            for (j in 0 until lineViews.size) {
                val childView = lineViews[j]
                val left = curLeft
                val top = curTop
                val right = left + childView.measuredWidth
                val bottom = top + childView.measuredHeight
                childView.layout(left, top, right, bottom)
                curLeft = right + horizontalSpacing
            }
            curLeft = paddingLeft
            curTop += linesHeight[i] + verticalSpacing
        }
    }
}