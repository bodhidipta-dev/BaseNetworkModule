package com.bodhi.network.networklayer.proxy

import com.bodhi.network.networklayer.ServiceCall
import com.bodhi.network.networklayer.local.NetworkCallCache
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.reflect.Type

const val PROXY_DATA = "Data"

/**
 * Created for com.bodhi.network.networklayer on 13-11-2019
 * Project ExampleNetworkLibrary
 */
abstract class ProxyTask<T> :
    RemoteProxy<T> {
    abstract fun serviceCallType(): ServiceCallType
    abstract fun conversionType(): Type
    override fun preferenceUniqueId(): String = ""

    /* Provide observable class */
    override fun provideTask(
        identifier: String,
        serviceCall: ServiceCall
    ): Task<T> {
        return ExecuteTask(
            identifier,
            preferenceUniqueId(),
            serviceCallType(),
            getServiceCall(),
            serviceCall,
            conversionType()
        )
    }

    abstract fun getServiceCall(): Observable<T>
}

private class ExecuteTask<T>(
    private val identifier: String,
    private val uniqueIdentity: String,
    private val serviceCallType: ServiceCallType,
    private val requestCall: Observable<T>,
    private val serviceCall: ServiceCall,
    private val returnType: Type
) :
    Task<T> {
    override fun execute(
        onsuccess: (T) -> Unit,
        onerror: (ex: Throwable) -> Unit
    ): Disposable {
        return when (serviceCallType) {
            ServiceCallType.PERSISTENCE -> {
                serviceCall.getPersistenceDao()?.getResponseByIdentifier(identifier)?.toObservable()
                    ?.map {
                        Gson().fromJson<T>(it.responseSaved, returnType)
                    }?.onErrorResumeNext(requestCall.doAfterNext {
                        serviceCall.getPersistenceDao()?.updateResponse(
                            NetworkCallCache(
                                identifier + uniqueIdentity,
                                Gson().toJson(it),
                                System.currentTimeMillis().toString()
                            )
                        )?.subscribeOn(Schedulers.io())?.subscribe(
                            {
                                Timber.i("Update Success:-$it")
                            }
                            , {
                                Timber.i("Error while update-$it")
                            })
                    })
            }
            ServiceCallType.NETWORK -> requestCall
            ServiceCallType.CACHE -> Observable.just(
                serviceCall.getSecurePreferences()
                    .getString(identifier, "")
            ).map {
                Gson().fromJson(it, returnType) as T
            }
        }?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
            onsuccess(it)
        }, {
            onerror(it)
        }) ?: throw RequestError("Something went wrong while placing request!")
    }
}