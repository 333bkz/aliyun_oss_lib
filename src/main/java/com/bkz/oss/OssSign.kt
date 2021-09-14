package com.bkz.oss

data class OssSign(
    var expiration: String?,
    var ram_AccessKeyId: String?,
    var ram_AccessKeySecret: String?,
    var ram_BucketName: String?,
    var ram_SecurityToken: String?,
    var oss_domain: String?
)