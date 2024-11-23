package com.example.riseandroid

import com.example.riseandroid.repository.TenturncardRepository
import com.example.riseandroid.rules.TestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TenturncardRepositoryTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun tenturncardRepository_addTenturncardTest() =
        runTest {

        }
}