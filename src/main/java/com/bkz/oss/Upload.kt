package com.bkz.oss

import android.content.Context
import com.bkz.oss.Params

class Upload(params: Params, context: Context) : Base(params, context) {

    override fun next(params: Params) {
        val result = put()
        if (result.code == 200) {
            params.success = true
            params.result = "${params.ossSign.oss_domain}/${params.node}/${params.fileName}"
        } else {
            params.success = false
        }
    }
}
