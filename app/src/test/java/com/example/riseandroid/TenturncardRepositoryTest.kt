package com.example.riseandroid

import com.auth0.android.result.Credentials
import com.example.riseandroid.data.entitys.TenturncardDao
import com.example.riseandroid.data.entitys.TenturncardEntity
import com.example.riseandroid.fake.FakeAuth0Repo
import com.example.riseandroid.fake.FakeTenturncardApi
import com.example.riseandroid.fake.FakeTenturncardDao
import com.example.riseandroid.network.TenturncardApi
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.repository.Auth0Repo
import com.example.riseandroid.repository.IAuthRepo
import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.flow.flowOf
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
}