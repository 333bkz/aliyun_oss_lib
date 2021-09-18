package com.bkz.oss

import android.content.Context
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.GetObjectRequest
import com.alibaba.sdk.android.oss.model.GetObjectResult
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

abstract class Base(
    private val params: Params,
    private val context: Context,
    private val check: (Params.() -> Boolean)? = null
) {

    private val config by lazy {
        ClientConfiguration().apply {
            connectionTimeout = 15 * 1000 // 连接超时，默认15秒
            socketTimeout = 15 * 1000 // socket超时，默认15秒
            maxErrorRetry = 2 // 失败后最大重试次数，默认2次
            maxConcurrentRequest = 6 // 最大并发请求数，默认5个
        }
    }

    private val client: OSSClient by lazy {
        OSSClient(
            context.applicationContext,
            params.ossSign.oss_domain,
            OSSStsTokenCredentialProvider(
                params.ossSign.ram_AccessKeyId,
                params.ossSign.ram_AccessKeySecret,
                params.ossSign.ram_SecurityToken
            ),
            config
        )
    }

    protected fun put(): Result {
        var code = 200
        var msg: String? = null
        try {
            client.putObject(
                PutObjectRequest(
                    params.ossSign.ram_BucketName,
                    String.format("%s/%s", params.node, params.fileName),
                    params.filePath
                )
            )
        } catch (e: Exception) {
            code = -1
            msg = e.message
            e.printStackTrace()
        }
        return Result(code, msg)
    }

    protected fun get(): Result {
        var result: GetObjectResult? = null
        var code = 200
        var msg: String? = null
        try {
            result = client.getObject(
                GetObjectRequest(
                    params.ossSign.ram_BucketName,
                    String.format("%s/%s", params.node, params.fileName),
                )
            )
        } catch (e: Exception) {
            code = -1
            msg = e.message
            e.printStackTrace()
        }
        return Result(code, msg, result?.objectContent?.inputString())
    }

    fun start(): Params {
        if (check?.invoke(params) != false) {
            next(params)
        }
        return params
    }

    protected abstract fun next(params: Params)
}

fun InputStream.inputString() = BufferedReader(InputStreamReader(this)).useLines { lines ->
    val results = StringBuilder()
    lines.forEach { results.append(it) }
    results.toString()
}
