package com.bodhi.network.networklayer.config

import okhttp3.Request
import okhttp3.Response

data class NetworkBuilder(
    val isMock: Boolean = false, /* For Mock network to be configured with environment selector */
    /* Mock key header to looked
    upon most of cases it will be userID / authorisation
    for example if header contains "userType" : "user1"
    Asset structure mock file should resides under mock/user1/servicename.json
    */
    val mockKye: String = "",
    /* It will use interceptor to read every network call */
    val shouldUseInterceptor: Boolean = true,
    /* Should Enable Chuck interceptor for network call stack notification
    * N.B to be disabled in production with configurable build variant */
    val shouldUseChuckInterceptor: Boolean = true,
    /* Use your interceptor override */
    val interceptor: (response: Response) -> Response = { it },
    /* Retry policy for failure */
    val retryPolicy: Boolean = true,
    /* Timeout after retry policy */
    val timeoutInMillis: Long = 10L,
    /* Cache policy for service call */
    val cachePolicy: Boolean = false,
    /* Request headers added to service call before service call take place */
    val requestHeaders: Map<String, String> = mapOf(),
    /* Override request call for authorisation call to take necessary steps */
    val authenticator: (response: Response) -> Request? = { it.request.newBuilder().build() }
)