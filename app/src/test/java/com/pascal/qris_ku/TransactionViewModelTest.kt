package com.pascal.qris_ku

import com.pascal.qris_ku.common.wrapper.Resource
import com.pascal.qris_ku.data.entity.TransactionEntity
import com.pascal.qris_ku.data.repository.LocalRepository
import com.pascal.qris_ku.presentation.main.viewModel.transaction.TransactionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TransactionViewModelTest {

    private lateinit var transactionViewModel: TransactionViewModel

    @Mock
    private lateinit var mockLocalRepository: LocalRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        transactionViewModel = TransactionViewModel(mockLocalRepository)

        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGetTransactionSuccess() = runBlockingTest {
        val transactionEntity = TransactionEntity(0, "Id1234", "BNI", "Test Mercant", "50000")
        val expectedResource = Resource.Success(listOf(transactionEntity))
        Mockito.`when`(mockLocalRepository.getTransaction()).thenReturn(flowOf(expectedResource))

        transactionViewModel.getTransaction()

        assertEquals(expectedResource::class, transactionViewModel.getTransactionResult.value::class)
        assertEquals(expectedResource.data, transactionViewModel.getTransactionResult.value.data)
    }
}