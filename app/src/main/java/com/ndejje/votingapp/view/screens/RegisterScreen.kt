package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
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
import com.ndejje.votingapp.ui.theme.*
import com.ndejje.votingapp.view.components.NdejjePrimaryButton
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
    var email by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAgreed by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()
    val courses = listOf("B.IT", "B.Education", "B.Law", "B.Engineering", "B.Business Administration")
    var expanded by remember { mutableStateOf(false) }

    // Password criteria logic
    val hasMinLength = password.length >= 8
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }
    
    val strengthScore = listOf(hasMinLength, hasUpper, hasLower, hasDigit, hasSymbol).count { it }
    val (strengthText, strengthColor) = when {
        password.isEmpty() -> "" to Color.Transparent
        strengthScore <= 2 -> "Weak" to MaterialTheme.colorScheme.error
        strengthScore <= 4 -> "Good" to WarningOrange
        else -> "Strong" to SuccessGreen
    }

    // Clear errors when typing
    fun onFieldChange() {
        validationError = null
        viewModel.resetState()
    }

    LaunchedEffect(authState) {
        if (authState is AuthResult.Success) {
            navController.navigate("home") {
                popUpTo("register") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(R.string.register_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StandardFormInput(
                value = regNo,
                onValueChange = { regNo = it; onFieldChange() },
                labelRes = R.string.label_reg_no,
                placeholderRes = R.string.placeholder_reg_no
            )

            StandardFormInput(
                value = fullName,
                onValueChange = { fullName = it; onFieldChange() },
                labelRes = R.string.label_full_name,
                placeholderRes = R.string.placeholder_full_name
            )

            StandardFormInput(
                value = email,
                onValueChange = { email = it; onFieldChange() },
                labelRes = R.string.label_email,
                placeholderRes = R.string.placeholder_email,
                keyboardType = KeyboardType.Email
            )

            // Course Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.label_course),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
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
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(32.dp)) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        courses.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection, fontSize = 18.sp) },
                                onClick = { 
                                    course = selection
                                    expanded = false
                                    onFieldChange()
                                }
                            )
                        }
                    }
                }
            }

            // Password Field
            Column {
                StandardFormInput(
                    value = password,
                    onValueChange = { password = it; onFieldChange() },
                    labelRes = R.string.label_password,
                    placeholderRes = R.string.placeholder_pass,
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
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
                    }
                )
                if (password.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                        Text("Strength: ", fontSize = 14.sp, color = Color.Gray)
                        Text(strengthText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = strengthColor)
                    }
                }
            }

            StandardFormInput(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; onFieldChange() },
                labelRes = R.string.label_confirm_password,
                placeholderRes = R.string.placeholder_pass,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        confirmPasswordVisible = true
                                        try {
                                            awaitRelease()
                                        } finally {
                                            confirmPasswordVisible = false
                                        }
                                    }
                                )
                            }
                    )
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Checkbox(
                checked = termsAgreed, 
                onCheckedChange = { termsAgreed = it; onFieldChange() },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Text("I agree to the terms and conditions", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
        }

        // Error display
        val errorToDisplay = validationError ?: (authState as? AuthResult.Error)?.message
        if (errorToDisplay != null) {
            Text(
                text = errorToDisplay,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        NdejjePrimaryButton(
            text = stringResource(R.string.btn_sign_up),
            onClick = {
                when {
                    regNo.isEmpty() || fullName.isEmpty() || email.isEmpty() || course.isEmpty() || password.isEmpty() -> {
                        validationError = "Please fill in all fields"
                    }
                    !email.contains("@") -> {
                        validationError = "Please enter a valid email"
                    }
                    !termsAgreed -> {
                        validationError = "You must agree to the terms"
                    }
                    password != confirmPassword -> {
                        validationError = "Passwords do not match"
                    }
                    strengthScore < 5 -> {
                        validationError = "Password does not meet complexity requirements"
                    }
                    else -> {
                        viewModel.registerUser(
                            UserEntity(
                                registrationNumber = regNo,
                                fullName = fullName,
                                course = course,
                                email = email,
                                password = password
                            )
                        )
                    }
                }
            },
            modifier = Modifier.height(64.dp),
            enabled = authState !is AuthResult.Loading
        )

        val footerText = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 16.sp)) { append(stringResource(R.string.footer_have_account) + " ") }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)) { append(stringResource(R.string.btn_login)) }
        }
        Text(footerText, modifier = Modifier.clickable { navController.navigate("login") }.padding(top = 24.dp))

        Spacer(modifier = Modifier.height(40.dp))
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
        Text(stringResource(labelRes), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholderRes?.let { { Text(stringResource(it)) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}
