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
    var yearOfStudy by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var campus by remember { mutableStateOf("Main Campus") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAgreed by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()
    val courses = listOf("B.IT", "B.Education", "B.Law", "B.Engineering", "B.Business Administration")
    val years = listOf("1", "2", "3", "4")
    val campuses = listOf("Main Campus", "Kampala Campus")
    var courseExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var campusExpanded by remember { mutableStateOf(false) }
    
    // ... rest of logic stays same ...

    // Handle Navigation Side Effects - Now goes straight to Home
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
            fontSize = 32.sp, // Made bigger
            fontWeight = FontWeight.Black,
            color = NdejjeDarkBlue
        )

        Text(
            text = stringResource(R.string.register_subtitle),
            color = Color.Gray,
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
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
                ExposedDropdownMenuBox(
                    expanded = courseExpanded,
                    onExpandedChange = { courseExpanded = !courseExpanded }
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
                    ExposedDropdownMenu(expanded = courseExpanded, onDismissRequest = { courseExpanded = false }) {
                        courses.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection, fontSize = 18.sp) },
                                onClick = { course = selection; courseExpanded = false }
                            )
                        }
                    }
                }
            }

            // Year of Study Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Year of Study",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = !yearExpanded }
                ) {
                    OutlinedTextField(
                        value = yearOfStudy,
                        onValueChange = {},
                        placeholder = { Text("Select Year") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(32.dp)) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                    )
                    ExposedDropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
                        years.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection, fontSize = 18.sp) },
                                onClick = { yearOfStudy = selection; yearExpanded = false }
                            )
                        }
                    }
                }
            }

            // Campus Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Campus",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
                ExposedDropdownMenuBox(
                    expanded = campusExpanded,
                    onExpandedChange = { campusExpanded = !campusExpanded }
                ) {
                    OutlinedTextField(
                        value = campus,
                        onValueChange = {},
                        placeholder = { Text("Select Campus") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(32.dp)) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                    )
                    ExposedDropdownMenu(expanded = campusExpanded, onDismissRequest = { campusExpanded = false }) {
                        campuses.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection, fontSize = 18.sp) },
                                onClick = { campus = selection; campusExpanded = false }
                            )
                        }
                    }
                }
            }

            StandardFormInput(
                value = email,
                onValueChange = { email = it },
                labelRes = R.string.app_name, // Placeholder for label as I don't want to edit strings.xml right now if not needed, but better use literal
                placeholderRes = null,
                keyboardType = KeyboardType.Email,
                customLabel = "University Email"
            )

            StandardFormInput(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                labelRes = R.string.app_name,
                placeholderRes = null,
                keyboardType = KeyboardType.Phone,
                customLabel = "Phone Number"
            )

            StandardFormInput(
                value = password,
                onValueChange = { password = it },
                labelRes = R.string.label_password,
                placeholderRes = R.string.placeholder_pass,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, modifier = Modifier.size(28.dp))
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
                        Icon(if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, modifier = Modifier.size(28.dp))
                    }
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Checkbox(checked = termsAgreed, onCheckedChange = { termsAgreed = it })
            Text("I agree to the terms and conditions", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }

        if (authState is AuthResult.Error) {
            Text((authState as AuthResult.Error).message, color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = {
                if (termsAgreed && password == confirmPassword && regNo.isNotEmpty()) {
                    viewModel.registerUser(
                        UserEntity(
                            registrationNumber = regNo,
                            fullName = fullName,
                            course = course,
                            yearOfStudy = yearOfStudy,
                            phoneNumber = phoneNumber,
                            email = email,
                            campus = campus,
                            password = password
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NdejjeDarkBlue),
            enabled = authState !is AuthResult.Loading
        ) {
            if (authState is AuthResult.Loading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp))
            } else {
                Text(stringResource(R.string.btn_sign_up), fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
        }

        val footerText = buildAnnotatedString {
            withStyle(SpanStyle(color = Color.Gray, fontSize = 16.sp)) { append(stringResource(R.string.footer_have_account) + " ") }
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 16.sp)) { append(stringResource(R.string.btn_login)) }
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
    trailingIcon: @Composable (() -> Unit)? = null,
    customLabel: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = customLabel ?: stringResource(labelRes),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
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
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )
    }
}
