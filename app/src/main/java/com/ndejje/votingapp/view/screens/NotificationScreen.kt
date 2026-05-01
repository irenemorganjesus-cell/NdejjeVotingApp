package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.votingapp.R
import com.ndejje.votingapp.model.NotificationEntity
import com.ndejje.votingapp.ui.theme.*
import com.ndejje.votingapp.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController, viewModel: NotificationViewModel) {
    val notifications by viewModel.notifications.collectAsState()

    // Mark all as read when opening the screen
    LaunchedEffect(Unit) {
        viewModel.markAllAsRead()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.notifications_title),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(R.dimen.font_size_h5).value.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button_desc),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings? */ }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = stringResource(R.string.notifications_title),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = dimensionResource(R.dimen.padding_small)),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = dimensionResource(R.dimen.notification_surface_radius), topEnd = dimensionResource(R.dimen.notification_surface_radius))
        ) {
            if (notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.notifications_empty), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.home_header_padding_vertical)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.notification_item_spacing))
                ) {
                    items(notifications) { notification ->
                        NotificationItem(notification)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: NotificationEntity) {
    val (icon, iconColor, lightBgColor) = when (notification.type) {
        "SUCCESS" -> Triple(Icons.Default.CheckCircle, SuccessGreen, SuccessGreenLight)
        "ALERT" -> Triple(Icons.Default.Warning, MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.errorContainer)
        "INFO" -> Triple(Icons.Default.Info, InfoBlue, InfoBlueLight)
        "REMINDER" -> Triple(Icons.Default.Notifications, WarningOrange, WarningOrangeLight)
        else -> Triple(Icons.Default.Notifications, MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.surfaceVariant)
    }
    
    val semanticBgColor = if (isSystemInDarkTheme()) {
        if (notification.type == "ALERT") MaterialTheme.colorScheme.errorContainer else iconColor.copy(alpha = 0.2f)
    } else {
        lightBgColor
    }
    
    val semanticIconColor = if (isSystemInDarkTheme() && notification.type == "ALERT") {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        iconColor
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.notification_item_elevation))
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.notification_icon_box_size))
                    .background(semanticBgColor, RoundedCornerShape(dimensionResource(R.dimen.notification_icon_box_radius))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = semanticIconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
                Text(
                    text = notification.message,
                    fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = formatTimestamp(notification.timestamp),
                    fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun formatTimestamp(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> stringResource(if (days > 1) R.string.notification_time_days else R.string.notification_time_day, days.toInt())
        hours > 0 -> stringResource(if (hours > 1) R.string.notification_time_hours else R.string.notification_time_hour, hours.toInt())
        minutes > 0 -> stringResource(if (minutes > 1) R.string.notification_time_minutes else R.string.notification_time_minute, minutes.toInt())
        else -> stringResource(R.string.notification_time_just_now)
    }
}
