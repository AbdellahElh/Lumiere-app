package com.example.riseandroid.ui.screens.signup

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.auth0.android.result.Credentials
import com.example.riseandroid.repository.APIResource
import com.example.riseandroid.ui.screens.signup.validation.ValidateEmail
import com.example.riseandroid.ui.screens.signup.validation.ValidatePassword
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide
import com.example.riseandroid.ui.screens.account.AccountViewModel

@Composable
fun SignUp(signUp: (Credentials) -> Unit,
           modifier: Modifier = Modifier,
           navController: NavController,
           authViewModel: AccountViewModel
) {

    val extras = MutableCreationExtras().apply {
        set(SignUpViewModel.SIGNUP_KEY, signUp)
        set(SignUpViewModel.APPLICATION_KEY, LocalContext.current.applicationContext as Application)
        set(SignUpViewModel.VALIDATE_EMAIL_KEY, ValidateEmail())
        set(SignUpViewModel.VALIDATE_PASSWORD_KEY, ValidatePassword())
    }
    val viewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModel.Factory,
        extras = extras,
    )

    val signUpState by viewModel.uiState.collectAsState()
    val apiResponseState by viewModel.authResponse.collectAsState()

    LaunchedEffect(apiResponseState) {
        if (apiResponseState is APIResource.Success) {
            val credentials = apiResponseState.data
            if (credentials != null) {
                authViewModel.setAuthenticated(credentials)
                navController.navigate("account")}
        } else if (apiResponseState is APIResource.Error) {
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,) {
        Text(
            text = "Registreren",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier=Modifier.semantics { contentDescription = "Register Screen"}
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = signUpState.email,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text(text = "Email") },
            isError = signUpState.signUpError || signUpState.emailError!=null
        )

        Spacer(modifier = Modifier.height(20.dp))

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        PasswordField(
            value = signUpState.password,
            isError = signUpState.signUpError || signUpState.passwordError!=null,
            onValueChange =  { x: String -> viewModel.updatePwd(x)},
            passwordVisible = passwordVisible,
            toggleVisible = { passwordVisible = !passwordVisible }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (signUpState.signUpError && apiResponseState is APIResource.Error) {
            ErrorMessage(apiResponseState)
        }
        else if(signUpState.emailError!=null)
        {
            Text("${signUpState.emailError}",
                fontSize = 18.sp,
                color =  MaterialTheme.colorScheme.error
            )
        }
        else if (signUpState.passwordError!=null)
        {
            Text("${signUpState.passwordError}",
                fontSize = 18.sp,
                color =  MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = { viewModel.onSubmit() },
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .semantics { contentDescription = "Register Button" }
        ) {
            if (apiResponseState is APIResource.Loading)
                Text("Aan het laden...",fontSize = 20.sp)
            else
                Text("Registreer",fontSize = 20.sp)
        }
    }
}

@Composable
fun ErrorMessage(apiResponse: APIResource<Credentials>) {
    Text("Error: ${apiResponse.message}",
        fontSize = 18.sp,
        color =  MaterialTheme.colorScheme.error
        )
}

@Composable
fun PasswordField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    toggleVisible: () -> Unit,
    label: String = "Wachtwoord"
) {
    TextField(
        value = value,
        isError = isError,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { onValueChange(it) },
        label = { Text(text = label) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Lucide.Eye else Lucide.EyeOff
            val description = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { toggleVisible() }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}

