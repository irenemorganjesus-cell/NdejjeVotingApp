package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.ui.theme.NdejjeDarkBlue
import com.ndejje.votingapp.viewmodel.AuthViewModel
import com.ndejje.votingapp.viewmodel.AuthResult

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var regNo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthResult.Success) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(dimensionResource(R.dimen.padding_standard_size)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_padding)))

        Image(
            painter = painterResource(id = R.drawable.university_logo),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size_small))
        )

        Text(stringResource(R.string.login_welcome), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NdejjeDarkBlue)
        Text(stringResource(R.string.login_subtitle), color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = regNo,
            onValueChange = { 
                regNo = it
                viewModel.resetState() // Clear error on type
            },
            label = { Text(stringResource(R.string.label_reg_no)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                viewModel.resetState() // Clear error on type
            },
            label = { Text(stringResource(R.string.label_password)) },
            placeholder = { Text("********") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    passwordVisible = true
                                    try {
                                        awaitRelease()
                                    } finally {
                                        passwordVisible = false
                                    }
                                }
                            )
                        }
                )
            },
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))
        )

        if (authState is AuthResult.Error) {
            Text((authState as AuthResult.Error).message, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        // Forgot Password Link
        Text(
            text = "Forgot Password?",
            color = NdejjeDarkBlue,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
                .clickable { navController.navigate("forgot_password") }
        )

        Button(
            onClick = { viewModel.loginUser(regNo, password) },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NdejjeDarkBlue),
            enabled = authState !is AuthResult.Loading
        ) {
            if (authState is AuthResult.Loading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text(stringResource(R.string.btn_login))
        }

        // Visible rectangular Register button
        OutlinedButton(
            onClick = { 
                viewModel.resetState()
                navController.navigate("register") 
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(56.dp),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            border = BorderStroke(1.dp, NdejjeDarkBlue)
        ) {
            Text(stringResource(R.string.btn_register), color = NdejjeDarkBlue, fontWeight = FontWeight.Bold)
        }
    }
}
