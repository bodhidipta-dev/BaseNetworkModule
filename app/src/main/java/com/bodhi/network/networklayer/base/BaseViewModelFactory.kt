package com.bodhi.network.networklayer.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bodhi.network.networklayer.remoteService.networkCall.RemoteCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseViewModelFactory @Inject constructor(private val remoteCall: RemoteCall) :
    ViewModelProvider.Factory {
    private val viewmodelHashSet: MutableMap<String, ViewModel> =
        mutableMapOf()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            ViewModelsReferencesSingleton.values().any {
                modelClass.isAssignableFrom(it.value)
            } -> {
                /*Check if instance already made or not*/
                if (!viewmodelHashSet.containsKey(modelClass.name)) {
                    val bindedInstance = ViewModelsReferencesSingleton.values().find {
                        modelClass.isAssignableFrom(it.value)
                    }?.value?.getConstructor(RemoteCall::class.java)?.newInstance(
                        remoteCall
                    ) as T
                    bindedInstance?.let {
                        viewmodelHashSet[modelClass.name] = bindedInstance as ViewModel
                    }
                }
                /* Return from hashset */
                viewmodelHashSet[modelClass.name] as T
            }
            ViewModelsReferences.values().any {
                modelClass.isAssignableFrom(it.value)
            } -> {
                ViewModelsReferences.values().find {
                    modelClass.isAssignableFrom(it.value)
                }?.value?.getConstructor(
                    RemoteCall::class.java
                )?.newInstance(remoteCall) as T
            }
            else -> throw ClassNotFoundException("No Viewmodel entry found")
        }
    }
}