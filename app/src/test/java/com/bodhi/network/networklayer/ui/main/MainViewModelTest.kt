package com.bodhi.network.networklayer.ui.main

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.PowerMockRunnerDelegate
import org.robolectric.RobolectricTestRunner

@RunWith(PowerMockRunner::class)
@PowerMockRunnerDelegate(RobolectricTestRunner::class)
@PowerMockIgnore("org.powermock.*", "org.robolectric.*", "android.*", "androidx.*")
class MainViewModelTest {
    lateinit var viewModel: MainViewModel
    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getRemoteCall() {
        Assert.assertTrue(viewModel.remoteCall.getPersistenceDAO() != null)
    }

    @Test
    fun setRemoteCall() {
    }

    @Test
    fun getPendingLow() {
    }

    @Test
    fun deletePendingOrders() {
    }

    @Test
    fun getPendingOrder() {
    }

    @Test
    fun getPendingPagination() {
    }
}