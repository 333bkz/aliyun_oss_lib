package com.bkz.oss

data class Params(
    val ossSign: OssSign,
    val node: String,
    val fileName: String,
    val filePath: String? = null,
    //result
    var result: String? = null,
    var success: Boolean = false
)