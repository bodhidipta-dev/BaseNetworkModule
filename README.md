# BaseNetworkModule

Base Network Module returns retrofit object with taking configuration data.
Just to get everything in one place, for simple implementation.
      
      NetworkManager(
            application = application,
            mockKye = "Authorisation", // Use your mock header key might be accesstoken or username etc.
            baseurl = "https://someapi.endpont/Apis/",
            networkBuilder = NetworkBuilder(
                isMock = true, // default false
                shouldUseInterceptor = true,  // To use for BODY HttpLoggingInterceptor
                shouldUseChuckInterceptor = true, // To use Chuck Interceptor
                interceptor = {
                    // do your stuff and return your custom Response
                    it // replace with your response
                },
                requestHeaders = mapOf("yourKey" to "yourHeader"),
                authenticator = {
                 if (it.request.header("Authorization") != null) {
                        return@NetworkBuilder null // Failed to authenticate.
                    }
                    // replace with your Auth request or null in case of invalid session
                    // for more info got https://square.github.io/okhttp/4.x/okhttp/okhttp3/-authenticator/
                    it.request.newBuilder()
                        .header("Authorization", "your stuff - refresh token maybe or credentials")
                        .build()
                },
                retryPolicy = true, // Whether it should retry in case of error
                timeoutInMillis = 2000, // When to timeout
                cachePolicy = true // Cache policy
                    /*
                    internally what it will do
                    if (httpconfig.getNetworkBuilder().cachePolicy) {
                    val cacheControl = CacheControl.Builder()
                      .maxAge(15, TimeUnit.MINUTES) // 15 minutes cache
                      .build()
                    responseBuilder
                      .removeHeader("Pragma")
                      .removeHeader("Cache-Control")
                      .addHeader("Cache-Control", cacheControl.toString())
                      }
                    */
                      )
                    )
                    
Thats all, you are set.

For Simple use without any config you dont need to send the NetworkBuilder at all
=

      NetworkManager(
            application = application,
            baseurl = "https://someapi.endpont/Apis/"
        )
       
       
For use Mock structure use Asset folder on the project as such
=

        --assets--/
        --mock-/
             -- mockekey--/
                        /--> serviceEndpointName.json
            authorisationServiceEndPointName.json

For call Service Use 
=
 
 
            remoteCall.getProxyTask<SomeDataType>(
                mapOf(PROXY_DATA to RequestDataClass),
                "IdentifierName"
            ).executeFlow(corroutineContextwithLifecycle)
                .catch {
                    // Do error stuff
                }
                .collectLatest {
                   // Collect latest flow
                } 
                // Or change it to live data
                
Do implement your class implementation of remoteCall
=  
    class NetworkImplementation(
    private val serviceCall: ServiceCall, // NetworkManager().retrofitclient 
    private val network: NetworkEndpoint // Your endpoint where all retrofit service call resides should be passed                              
    NetworkManager().retrofitclient.serviceEndpoints(NetworkEndpoint::class.java)) : RemoteCall {
            /* Mapping for all service call */
            override fun <T : Any> getProxyTask(params: Map<*, *>?, identifier: String): Task<T> {
            return when (identifier) {
            "identifeir1" -> IdentifierService1<T>(network, params) // IdentifierService1 extends  ProxyTask<T>() 
            "identifier2" -> IdentifierService2<T>(network, params)
            else -> throw ClassNotFoundException("No Such service found")
            }.provideTaskAsync(identifier = identifier, serviceCall = serviceCall)
            }
            /*return database for save information */
            override fun getPersistenceDAO(): PersistenceDao? {
            return serviceCall.getPersistenceDao()
            } 
            }

References
=  
    https://square.github.io/okhttp/
    https://square.github.io/retrofit/
    https://github.com/JakeWharton/timber
    https://github.com/scottyab/secure-preferences
    https://github.com/jgilfelt/chuck
    https://github.com/greenrobot/EventBus
    https://bumptech.github.io/glide/
    https://github.com/Karumi/Dexter
    https://github.com/intuit/sdp
    https://github.com/intuit/ssp