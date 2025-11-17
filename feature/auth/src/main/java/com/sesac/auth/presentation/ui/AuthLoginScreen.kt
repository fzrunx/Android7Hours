package com.sesac.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.sesac.auth.nav_graph.AuthNavigationRoute
import com.sesac.auth.presentation.AuthViewModel
import com.sesac.domain.result.JoinUiState
import com.sesac.common.R
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall

@Composable
fun AuthLoginScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    val email by remember { viewModel.loginEmail }
    val password by remember { viewModel.loginPassword }
    val uiState by viewModel.joinUiState.collectAsState()

    // When the screen is shown, reset the previous UI state
    // When the composable is disposed, reset it again to not affect other screens
    DisposableEffect(Unit) {
        viewModel.resetUiState()
        onDispose {
            viewModel.resetUiState()
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is JoinUiState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingLarge),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(paddingLarge))
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.loginEmail.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            maxLines = 1,
            label = { Text(stringResource(R.string.auth_join_email_label)) }
        )
        Spacer(modifier = Modifier.height(paddingSmall))
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.loginPassword.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            maxLines = 1,
            label = { Text(stringResource(R.string.auth_join_password_label)) }
        )
        Spacer(modifier = Modifier.height(paddingLarge))

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            when (uiState) {
                is JoinUiState.Loading -> CircularProgressIndicator()
                is JoinUiState.Error -> {
                    Column {
                        Button(onClick = { viewModel.onLoginClick() }) {
                            Text(stringResource(R.string.auth_login_button))
                        }
                        Text(
                            (uiState as JoinUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> Button(onClick = { viewModel.onLoginClick() }) {
                    Text(stringResource(R.string.auth_login_button))
                }
            }

            Button(
                modifier = Modifier.padding(horizontal = paddingMedium),
                onClick = { navController.navigate(AuthNavigationRoute.JoinTab) },
            ) {
                Text(stringResource(R.string.auth_signup_button))
            }
        }
    }
}