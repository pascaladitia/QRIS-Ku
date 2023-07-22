package com.pascal.qris_ku

import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.SaldoEntity
import com.pascal.qris_ku.data.repository.LocalRepository
import com.pascal.qris_ku.presentation.main.viewModel.saldo.SaldoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SaldoViewModelTest {

    private lateinit var saldoViewModel: SaldoViewModel

    @Mock
    private lateinit var mockLocalRepository: LocalRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        saldoViewModel = SaldoViewModel(mockLocalRepository)

        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetSaldoSuccess() = runBlockingTest {
        val saldoEntity = SaldoEntity("1", "1000")
        val expectedResource = Resource.Success(saldoEntity)
        Mockito.`when`(mockLocalRepository.getSaldo()).thenReturn(flowOf(expectedResource))

        saldoViewModel.getSaldo()

        assertEquals(expectedResource::class, saldoViewModel.getSaldoResult.value::class)
        assertEquals(expectedResource.data, saldoViewModel.getSaldoResult.value.data)
    }
}