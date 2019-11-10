# BaseNetworkModule
Base Network Module returns retrofit object with taking configuration data.
Just to get everything in one place, for simple implementation.
      
      RetrofitClient(
            baseurl = "your base url goes here",
            networkBuilder = NetworkBuilder(
                 shouldUseInterceptor = true,
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

       RetrofitClient(
            baseurl = "your Url"
        ){
        retrofitClientt->
         retrofitClientt.create(YourInterfaceClass::class.java)
        }
        
