package com.larry.sampleflowlayout.ui

import android.content.res.Resources
import android.util.TypedValue

/**
 *@author larryycliu on 11/22/21.
 *
 */
class DpUtil {

    companion object {

        fun dp2px(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                Resources.getSystem().displayMetrics
            ).toInt()
        }

    }

}