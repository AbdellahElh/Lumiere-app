package com.example.riseandroid.ui.screens.login

import android.app.Application
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.auth0.android.result.Credentials
import com.example.riseandroid.repository.ApiResource
import com.example.riseandroid.ui.screens.account.AuthViewModel


@Composable
fun LoginScreen(
    navController: NavHostController,
    login: (Credentials) -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel

) {
    val extras = MutableCreationExtras().apply {
        set(LoginViewModel.LOGIN_KEY, login)
        set(LoginViewModel.APPLICATION_KEY, LocalContext.current.applicationContext as Application)
        set(LoginViewModel.AUTH_VIEWMODEL_KEY, authViewModel)
    }

    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory,
        extras = extras,
    )

    val loginState by viewModel.uiState.collectAsState()
    val apiResponseState by viewModel.authResponse.collectAsState()


    LaunchedEffect(Unit) {

        viewModel.navigateToAccount.collect { shouldNavigate ->
            if (shouldNavigate) {
                navController.navigate("account")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = loginState.username,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onValueChange = { viewModel.updateUserName(it) },
            label = { Text(text = "Email") },
            isError = loginState.loginError
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        PasswordField(
            value = loginState.password,
            isError = loginState.loginError,
            onValueChange = { viewModel.updatePwd(it) },
            passwordVisible = passwordVisible,
            toggleVisible = { passwordVisible = !passwordVisible }
        )

        if (loginState.loginError && apiResponseState is ApiResource.Error) {
            ErrorMessage(apiResponseState)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    viewModel.onSubmit()
                },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                if (apiResponseState is ApiResource.Loading)
                    Text("Loading...")
                else {
                    Text("Login")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Heeft u nog geen account? Meld u hier aan",
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navController.navigate("account/signup")
                },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Wachtwoord vergeten?",
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navController.navigate("forgotPassword")
                },
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}



@Composable
fun ErrorMessage(apiResponse: ApiResource<Credentials>) {
    Text(
        "The combination of username and password is not found. " + apiResponse.message,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.padding(top = 8.dp).testTag("errorText")
            .semantics { contentDescription = "LoginError" },
        textAlign = TextAlign.Center
    )
}

@Composable
fun PasswordField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    toggleVisible: () -> Unit
) {
    TextField(
        value = value,
        isError = isError,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("passwordTextField"),
        onValueChange = { onValueChange(it) },
        label = { Text(text = "Password") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { toggleVisible() }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun LoginPreview() {
//    RiseAndroidTheme {
//        LoginScreen(
//            login = {},
//            isLoggedIn = false // Simuleer niet-ingelogde toestand
//        )
//    }
//}