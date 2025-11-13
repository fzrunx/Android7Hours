package com.sesac.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sesac.auth.nav_graph.AuthNavigationRoute
import com.sesac.auth.presentation.AuthViewModel
import com.sesac.auth.presentation.JoinUiState
import com.sesac.common.ui.theme.paddingMedium

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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.loginEmail.value = it },
            maxLines = 1,
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.loginPassword.value = it },
            maxLines = 1,
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            when (uiState) {
                is JoinUiState.Loading -> CircularProgressIndicator()
                is JoinUiState.Error -> {
                    Column {
                        Button(onClick = { viewModel.onLoginClick() }) {
                            Text("Log In")
                        }
                        Text(
                            (uiState as JoinUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> Button(onClick = { viewModel.onLoginClick() }) {
                    Text("Log In")
                }
            }

            Button(
                modifier = Modifier.padding(horizontal = paddingMedium),
                onClick = { navController.navigate(AuthNavigationRoute.JoinTab) },
            ) {
                Text("회원가입")
            }
        }
    }
}