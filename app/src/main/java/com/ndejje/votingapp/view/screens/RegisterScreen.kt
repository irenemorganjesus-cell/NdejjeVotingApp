package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.model.UserEntity
import com.ndejje.votingapp.ui.theme.NdejjeDarkBlue
import com.ndejje.votingapp.viewmodel.AuthViewModel
import com.ndejje.votingapp.viewmodel.AuthResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var regNo by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAgreed by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()
    val courses = listOf("B.IT", "B.Education", "B.Law", "B.Engineering", "B.Business Administration")
    var expanded by remember { mutableStateOf(false) }

    // Handle Navigation Side Effects
    LaunchedEffect(authState) {
        if (authState is AuthResult.Success) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(dimensionResource(R.dimen.padding_standard_size))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_padding)))

        Image(
            painter = painterResource(id = R.drawable.university_logo),
            contentDescription = stringResource(R.string.logo_description),
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size_small))
        )

        Text(
            text = stringResource(R.string.register_title),
            fontSize = dimensionResource(R.dimen.font_size_title_header).value.sp,
            fontWeight = FontWeight.Bold,
            color = NdejjeDarkBlue
        )

        Text(
            text = stringResource(R.string.register_subtitle),
            color = Color.Gray,
            fontSize = dimensionResource(R.dimen.font_size_body_1).value.sp
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_field_spacing)))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.login_field_spacing))
        ) {
            StandardFormInput(
                value = regNo,
                onValueChange = { regNo = it },
                labelRes = R.string.label_reg_no,
                placeholderRes = R.string.placeholder_reg_no
            )

            StandardFormInput(
                value = fullName,
                onValueChange = { fullName = it },
                labelRes = R.string.label_full_name,
                placeholderRes = R.string.placeholder_full_name
            )

            // Course Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.label_course),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = course,
                        onValueChange = {},
                        placeholder = { Text(stringResource(R.string.placeholder_course)) },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        courses.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection) },
                                onClick = { course = selection; expanded = false }
                            )
                        }
                    }
                }
            }

            StandardFormInput(
                value = password,
                onValueChange = { password = it },
                labelRes = R.string.label_password,
                placeholderRes = R.string.placeholder_pass,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                }
            )

            StandardFormInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                labelRes = R.string.label_confirm_password,
                placeholderRes = R.string.placeholder_pass,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                    }
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = termsAgreed, onCheckedChange = { termsAgreed = it })
            Text(stringResource(R.string.agree_terms), fontSize = dimensionResource(R.dimen.font_size_body_1).value.sp)
        }

        if (authState is AuthResult.Error) {
            Text((authState as AuthResult.Error).message, color = Color.Red, fontSize = 12.sp)
        }

        Button(
            onClick = {
                if (termsAgreed && password == confirmPassword && regNo.isNotEmpty()) {
                    viewModel.registerUser(UserEntity(regNo, fullName, course, password))
                }
            },
            modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            colors = ButtonDefaults.buttonColors(containerColor = NdejjeDarkBlue),
            enabled = authState !is AuthResult.Loading
        ) {
            if (authState is AuthResult.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.btn_sign_up), fontWeight = FontWeight.Bold)
            }
        }

        val footerText = buildAnnotatedString {
            withStyle(SpanStyle(color = Color.Gray)) { append(stringResource(R.string.footer_have_account) + " ") }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))) { append(stringResource(R.string.btn_login)) }
        }
        Text(footerText, modifier = Modifier.clickable { navController.navigate("login") }.padding(top = 16.dp))

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_padding)))
    }
}

@Composable
private fun StandardFormInput(
    value: String,
    onValueChange: (String) -> Unit,
    labelRes: Int,
    placeholderRes: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(labelRes), fontWeight = FontWeight.Medium, color = Color.DarkGray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholderRes?.let { { Text(stringResource(it)) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}