package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.model.UserEntity
import com.ndejje.votingapp.view.components.BottomNavigationBar

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.votingapp.R

@Composable
fun ProfileScreen(navController: NavController, user: UserEntity?) {
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.profile_top_spacer)))

            // Profile Picture Circle
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.profile_pic_size))
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (user?.profilePictureUri != null) {
                    AsyncImage(
                        model = user.profilePictureUri,
                        contentDescription = stringResource(R.string.profile_picture_desc),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(dimensionResource(R.dimen.profile_pic_icon_size))
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.form_elements_margin_top)))

            Text(
                text = user?.fullName ?: stringResource(R.string.default_user_name),
                fontSize = dimensionResource(R.dimen.font_size_h4).value.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = user?.registrationNumber ?: stringResource(R.string.default_reg_no),
                fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

            // Profile Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_standard)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.border_width))
            ) {
                Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                    ProfileInfoRow(stringResource(R.string.label_course), user?.course ?: stringResource(R.string.default_course))
                    HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.profile_info_spacing)), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    
                    ProfileInfoRow(stringResource(R.string.label_year_of_study), stringResource(R.string.label_year_format, user?.yearOfStudy ?: "1"))
                    HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.profile_info_spacing)), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    
                    ProfileInfoRow(stringResource(R.string.label_campus), user?.campus ?: stringResource(R.string.default_campus))
                    HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.profile_info_spacing)), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    
                    val emailDisplay = user?.email?.let { if (it.isEmpty()) stringResource(R.string.text_not_set) else it } ?: stringResource(R.string.text_not_set)
                    ProfileInfoRow(stringResource(R.string.label_email), emailDisplay)
                    HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.profile_info_spacing)), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    
                    val phoneDisplay = user?.phoneNumber?.let { if (it.isEmpty()) stringResource(R.string.text_not_set) else it } ?: stringResource(R.string.text_not_set)
                    ProfileInfoRow(stringResource(R.string.label_phone), phoneDisplay)
                    HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.profile_info_spacing)), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    ProfileInfoRow(stringResource(R.string.label_voting_status), if (user?.hasVoted == true) stringResource(R.string.status_voted) else stringResource(R.string.status_not_voted))
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

            // Action Buttons
            OutlinedButton(
                onClick = { navController.navigate("edit_profile") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_standard))
                    .height(dimensionResource(R.dimen.button_height)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                border = androidx.compose.foundation.BorderStroke(dimensionResource(R.dimen.border_width), MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(dimensionResource(R.dimen.edit_icon_size)), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(stringResource(R.string.btn_edit_profile), fontSize = dimensionResource(R.dimen.font_size_large).value.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.form_elements_margin_top)))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_standard))
                    .height(dimensionResource(R.dimen.button_height)),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius))
            ) {
                Text(stringResource(R.string.btn_logout), fontSize = dimensionResource(R.dimen.font_size_large).value.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
            }
            
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = dimensionResource(R.dimen.font_size_large).value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = dimensionResource(R.dimen.font_size_large).value.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
    }
}
