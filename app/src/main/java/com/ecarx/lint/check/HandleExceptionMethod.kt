package com.ecarx.lint.check

import android.graphics.Color

/**
 * Created by rocketzly on 2020/8/9.
 */
class HandleExceptionMethod {

    fun test() {
        Color.parseColor("aaa")
    }
}