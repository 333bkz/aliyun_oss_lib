package com.bkz.oss

import android.content.Context
import com.bkz.oss.Params

class Download(params: Params, context: Context) : Base(params, context) {

    override fun next(params: Params) {
        val result = get()
        if (result.code == 200) {
            params.success = true
            params.result = result.get
        } else {
            params.success = false
        }
    }
}
