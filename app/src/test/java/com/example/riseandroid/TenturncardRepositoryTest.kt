package com.example.riseandroid

import com.example.riseandroid.data.entitys.tenturncard.TenturncardEntity
import com.example.riseandroid.fake.FakeAuth0Repo
import com.example.riseandroid.fake.FakeTenturncardApi
import com.example.riseandroid.fake.FakeTenturncardDao
import com.example.riseandroid.model.Tenturncard
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals


class TenturncardRepositoryTest {
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    private val tenturncardApi = FakeTenturncardApi()
    private val tenturncardDao = FakeTenturncardDao()
    private val authrepo = FakeAuth0Repo()
    private lateinit var tenturncardRepo: TenturncardRepository

    private val activationCode = "testCode"
    private val userId = 1

    private var toAddTenturncard = TenturncardEntity(
        id = 0,
        amountLeft = 10,
        purchaseDate = null,
        expirationDate = null,
        ActivationCode = activationCode,
        IsActivated = false,
        UserTenturncardId = userId
    )


    private var toUpdateCardSucces = Tenturncard(
        id = 0,
        amountLeft = 5,
        purchaseDate = " ",
        expirationDate = " ",
        ActivationCode = activationCode,
        IsActivated = false,
    )

    private var toUpdateCardError = Tenturncard(
        id = 0,
        amountLeft = 7,
        purchaseDate = " ",
        expirationDate = " ",
        ActivationCode = "errorTest",
        IsActivated = false,
    )

    @Before
    fun setUp() {
        tenturncardRepo = TenturncardRepository(tenturncardApi, tenturncardDao, authrepo)
    }

    @Test
    fun tenturncardRepository_addTenturncard_emits_succesTest() =
        //using StandardTestDispatcher
        runTest {
            // Execute to be tested code
            val flow = tenturncardRepo.addTenturncard(activationCode)

            // Assert
            val emissions = flow.toList()
            println(emissions[1].message)
            assert(emissions[0] is ApiResource.Loading)
            assert(emissions[1] is ApiResource.Success)
            val successData = (emissions[1] as ApiResource.Success).data
            if (successData != null) {
                assertEquals(successData.id, toAddTenturncard.id)
            }
        }

    @Test
    fun tenturncardRepository_addTenturncard_emits_errorTest() =
        runTest {
            // Execute to be tested code
            val flow = tenturncardRepo.addTenturncard("fakeActivationCode")

            // Assert
            val emissions = flow.toList()
            println(emissions)
            assert(emissions[0] is ApiResource.Loading)
            assert(emissions[1] is ApiResource.Error)
        }

    @Test
    fun tenturncardRepository_updateCard_emits_succesTest() =
        runTest {
            // Setup
            tenturncardDao.addTenturncard(toAddTenturncard)
            // Execute to be tested code
            val flow = tenturncardRepo.editTenturncard(toUpdateCardSucces)
            // Assert
            val emissions = flow.toList()
            assert(emissions[0] is ApiResource.Loading)
            assert(emissions[1] is ApiResource.Success)
            val updatedCard = tenturncardDao.getTenturncardById(toUpdateCardSucces.id)
            assertEquals(toUpdateCardSucces.amountLeft, updatedCard?.amountLeft ?: 0 )
        }

    @Test
    fun tenturncardRepository_updateCard_emits_errorTest() =
        runTest {
            // Setup
            tenturncardDao.addTenturncard(toAddTenturncard)
            // Execute to be tested code
            val flow = tenturncardRepo.editTenturncard(toUpdateCardError)
            // Assert
            val emissions = flow.toList()
            assert(emissions[0] is ApiResource.Loading)
            assert(emissions[1] is ApiResource.Error)
            // Check if the action was correctly reversed in case of error
            val updatedCard = tenturncardDao.getTenturncardById(toUpdateCardError.id)
            if (updatedCard != null) {
                assertEquals(toAddTenturncard.amountLeft, updatedCard.amountLeft)
            }
        }
    @Test
    fun tenturncardRepository_minOneUpdateCardById_emits_succesTest() =
        runTest {
            // Setup
            tenturncardDao.addTenturncard(toAddTenturncard)
            // Execute to be tested code
            val flow = tenturncardRepo.minOneUpdateCardById(toAddTenturncard.ActivationCode)
            // Assert
            val emissions = flow.toList()
            assert(emissions[0] is ApiResource.Loading)
            assert(emissions[1] is ApiResource.Success)
            val updatedCard = tenturncardDao.getTenturncardById(toAddTenturncard.id)
            if (updatedCard != null) {
                assertEquals(updatedCard.amountLeft, 9)
            }
        }

    @Test
    fun tenturncardRepository_minOneUpdateCardById_emits_errorTest() =
        runTest {
            // Setup
            tenturncardDao.addTenturncard(toAddTenturncard)
            // Execute to be tested code
            val flow = tenturncardRepo.minOneUpdateCardById(toUpdateCardError.ActivationCode)
            // Assert
            val emissions = flow.toList()
            assert(emissions[0] is ApiResource.Loading)
            assert(emissions[1] is ApiResource.Error)
        }
}