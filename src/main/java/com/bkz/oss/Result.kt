package com.bkz.oss

data class Result(
    val code: Int,
    val message: String?,
    val get: String? = null
)