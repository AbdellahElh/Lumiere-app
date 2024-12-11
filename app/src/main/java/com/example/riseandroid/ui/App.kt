package com.example.riseandroid.ui

import android.app.Application
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.auth0.android.Auth0
import com.example.riseandroid.LumiereApplication
import com.example.riseandroid.navigation.BottomBar
import com.example.riseandroid.navigation.NavHostWrapper
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModel
import com.example.riseandroid.ui.screens.login.ForgotPasswordViewModelFactory
import com.example.riseandroid.ui.screens.account.AuthViewModel


@Composable
fun LumiereApp() {
    val navController = rememberNavController()
    val applicationContext = LocalContext.current.applicationContext as LumiereApplication
    val account: Auth0 = applicationContext.container.authRepo.auth0
    val extras = remember {
        MutableCreationExtras().apply {
            set(AuthViewModel.APPLICATION_KEY, applicationContext)
        }
    }

    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory, extras = extras)
    val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel(
        factory = ForgotPasswordViewModelFactory(applicationContext, account)
    )

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        NavHostWrapper(
            navController = navController, paddingValues = paddingValues, account = account,
            authViewModel = authViewModel,
            forgotPasswordViewModel = forgotPasswordViewModel
        )
    }
}








