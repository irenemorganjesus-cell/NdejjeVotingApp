package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.ui.theme.NdejjeDarkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var regNo by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAgreed by remember { mutableStateOf(false) }

    // Dropdown state
    var expanded by remember { mutableStateOf(false) }
    val courses = listOf("B.IT", "B.Education", "B.Law", "B.Engineering", "B.Business Administration")

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(dimensionResource(R.dimen.padding_standard_size))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_padding)))
        // University Logo
        Image(
            painter = painterResource(id = R.drawable.university_logo),
            contentDescription = stringResource(R.string.logo_description),
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size_small))
        )

        // Header Text
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

        // Form Section
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

            // Course Dropdown Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.label_course),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = course,
                        onValueChange = {},
                        placeholder = { Text(stringResource(R.string.placeholder_course)) },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(dimensionResource(R.dimen.dropdown_arrow_size))
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        courses.forEach { selectedCourse ->
                            DropdownMenuItem(
                                text = { Text(selectedCourse) },
                                onClick = {
                                    course = selectedCourse
                                    expanded = false
                                }
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
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
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
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_field_spacing)))

        // Terms and Conditions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAgreed,
                onCheckedChange = { termsAgreed = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = Color.Gray
                )
            )
            Text(
                text = stringResource(R.string.agree_terms),
                fontSize = dimensionResource(R.dimen.font_size_body_1).value.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Action Button
        Button(
            onClick = { /* Logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_font_size)),
            colors = ButtonDefaults.buttonColors(containerColor = NdejjeDarkBlue)
        ) {
            Text(
                text = stringResource(R.string.btn_sign_up),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.form_elements_margin_top)))

        // Footer
        val footerText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append(stringResource(R.string.footer_have_account) + " ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))) {
                append(stringResource(R.string.btn_login))
            }
        }

        Text(
            text = footerText,
            fontSize = dimensionResource(R.dimen.font_size_body_1).value.sp,
            modifier = Modifier.clickable { navController.navigate("login") }
        )

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
    val placeholder: @Composable (() -> Unit)? = if (placeholderRes != null) {
        { Text(stringResource(placeholderRes)) }
    } else {
        null
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(labelRes),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}