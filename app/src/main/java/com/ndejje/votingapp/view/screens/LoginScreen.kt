package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.ui.theme.LayoutWeights
import com.ndejje.votingapp.ui.theme.NdejjeDarkBlue

@Composable
fun LoginScreen(navController: NavController) {
    var regNo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(dimensionResource(R.dimen.padding_standard_size)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_padding)))

        //Small University Logo
        Image(
            painter = painterResource(id = R.drawable.university_logo),
            contentDescription = stringResource(R.string.logo_description),
            modifier = Modifier.size(dimensionResource(R.dimen.logo_size_small))
        )

        //Welcome Header
        Text(
            text = stringResource(R.string.login_welcome),
            fontSize = dimensionResource(R.dimen.font_size_title).value.sp,
            fontWeight = FontWeight.Bold,
            color = NdejjeDarkBlue
        )

        Text(
            text = stringResource(R.string.login_subtitle),
            color = Color.Gray,
            fontSize = dimensionResource(R.dimen.together_message_size).value.sp
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        //Registration Number Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.label_reg_no), fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = regNo,
                onValueChange = { regNo = it },
                placeholder = { Text(stringResource(R.string.placeholder_reg_no)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.login_field_spacing)))

        //Password Field
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.label_password), fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text(stringResource(R.string.placeholder_pass)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }

        //Forgot Password link
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                text = stringResource(R.string.link_forgot_password),
                color = Color(0xFF2E7D32), // Dark Green like the image
                fontSize = dimensionResource(R.dimen.pass_holder_size).value.sp,
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_forgot))
                    .clickable { /* Handle Forgot Password */ }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))

        //Login Button
        Button(
            onClick = { /* Logic for DB check later */ },
            modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            colors = ButtonDefaults.buttonColors(containerColor = NdejjeDarkBlue)
        ) {
            Text(stringResource(R.string.btn_login), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_height)))

        //Divider
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(LayoutWeights.StandardWeight), thickness = 1.dp, color = Color.LightGray)
            Text(
                text = stringResource(R.string.divider_text),
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_forgot)),
                color = Color.Gray,
                fontSize = dimensionResource(R.dimen.continue_size).value.sp
            )
            HorizontalDivider(modifier = Modifier.weight(LayoutWeights.StandardWeight), thickness = dimensionResource(R.dimen.border_width), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_height)))

        //Google Login Button
        OutlinedButton(
            onClick = { /* Handle Google Login */ },
            modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
            border = androidx.compose.foundation.BorderStroke(dimensionResource(R.dimen.border_width), Color.LightGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google), // Ensure you have a google icon in drawables
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(R.dimen.google_icon_size))
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_forgot)))
                Text(stringResource(R.string.btn_google_login), color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.weight(LayoutWeights.StandardWeight))

        //Footer: Link to Register
        Row {
            Text(text = stringResource(R.string.footer_no_account), color = Color.Gray)
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.indicator_stroke)))
            Text(
                text = stringResource(R.string.btn_register),
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate("register") }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.footer_space)))
    }
}