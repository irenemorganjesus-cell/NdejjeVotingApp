package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.ndejje.votingapp.ui.theme.AlertRed
import com.ndejje.votingapp.view.components.NdejjePrimaryButton
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
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(dimensionResource(R.dimen.padding_standard_size)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Navigation Arrow
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_desc),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Image(
            painter = painterResource(id = R.drawable.university_logo),
            contentDescription = null,
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size_small))
        )

        Text(
            stringResource(R.string.login_welcome),
            fontSize = dimensionResource(R.dimen.font_size_h3).value.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            stringResource(R.string.login_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

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

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                viewModel.resetState() // Clear error on type
            },
            label = { Text(stringResource(R.string.label_password)) },
            placeholder = { Text(stringResource(R.string.password_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = { Icon(Icons.Default.Lock, null) },
            trailingIcon = {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.dropdown_arrow_size))
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
            Text(
                text = (authState as AuthResult.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_extra_small)),
                fontWeight = FontWeight.Medium
            )
        }

        // Forgot Password Link
        Text(
            text = stringResource(R.string.link_forgot_password),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = dimensionResource(R.dimen.padding_extra_small))
                .clickable { navController.navigate("forgot_password") }
        )

        NdejjePrimaryButton(
            text = stringResource(R.string.btn_login),
            onClick = { viewModel.loginUser(regNo, password) },
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_standard)),
            enabled = authState !is AuthResult.Loading
        )

        // Visible rectangular Register button
        OutlinedButton(
            onClick = { 
                viewModel.resetState()
                navController.navigate("register") 
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(R.dimen.padding_medium))
                .height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            border = BorderStroke(dimensionResource(R.dimen.border_width), MaterialTheme.colorScheme.primary)
        ) {
            Text(
                stringResource(R.string.btn_register),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

    }
}
