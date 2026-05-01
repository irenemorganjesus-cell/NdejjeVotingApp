package com.ndejje.votingapp.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.view.components.BottomNavigationBar
import com.ndejje.votingapp.view.components.NdejjeQuickActionCard
import kotlinx.coroutines.delay
import java.util.Calendar

@Composable
fun HomeScreen(
    navController: NavController,
    userName: String,
    candidateViewModel: com.ndejje.votingapp.viewmodel.CandidateViewModel,
    notificationViewModel: com.ndejje.votingapp.viewmodel.NotificationViewModel
) {
    val totalVotes by candidateViewModel.totalVotes.collectAsState()
    val lastVoteTime by candidateViewModel.lastVoteTime.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    // Countdown logic: Target is 7 days from now
    var daysLeft by remember { mutableStateOf(0L) }
    var hoursLeft by remember { mutableStateOf(0L) }
    var isElectionEnded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val targetTime = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 7)
        }.timeInMillis
        
        while (true) {
            val currentTime = System.currentTimeMillis()
            val diff = targetTime - currentTime
            
            if (diff > 0) {
                daysLeft = diff / (24 * 60 * 60 * 1000)
                hoursLeft = (diff / (60 * 60 * 1000)) % 24
                isElectionEnded = false
            } else {
                isElectionEnded = true
            }
            delay(60000) // Update every minute
        }
    }

    val timeLeft = if (isElectionEnded) {
        stringResource(R.string.home_election_ended)
    } else {
        stringResource(R.string.home_time_left_format, daysLeft, hoursLeft)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // --- HEADER SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(
                        horizontal = dimensionResource(R.dimen.home_header_padding_horizontal),
                        vertical = dimensionResource(R.dimen.home_header_padding_vertical)
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.home_greeting, userName),
                        fontSize = dimensionResource(R.dimen.font_size_h2).value.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Box {
                        IconButton(
                            onClick = { 
                                notificationViewModel.markAllAsRead()
                                navController.navigate("notifications") 
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .size(dimensionResource(R.dimen.notification_icon_button_size))
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = stringResource(R.string.notifications_title),
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(dimensionResource(R.dimen.notification_icon_size))
                            )
                        }
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.notification_badge_size))
                                    .background(MaterialTheme.colorScheme.error, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .border(
                                        dimensionResource(R.dimen.notification_badge_border),
                                        MaterialTheme.colorScheme.primaryContainer,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(R.string.home_ready_question),
                    fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_extra_small))
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard_size)))

                // Election Banner (Dark Blue)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation_small))
                ) {
                    Column(
                        modifier = Modifier.padding(dimensionResource(R.dimen.home_card_inner_padding)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.home_election_banner_title),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = dimensionResource(R.dimen.font_size_h5).value.sp
                        )
                        Text(
                            stringResource(R.string.home_election_banner_subtitle1),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            stringResource(R.string.home_election_banner_subtitle2),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.form_elements_margin_top)))
                        Text(
                            stringResource(R.string.home_election_banner_motto),
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            fontSize = dimensionResource(R.dimen.font_size_caption).value.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // --- QUICK ACTIONS SECTION ---
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.home_card_inner_padding))) {
                Text(
                    stringResource(R.string.home_quick_actions_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.font_size_h6).value.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.home_action_grid_spacing))
                ) {
                    NdejjeQuickActionCard(
                        stringResource(R.string.home_action_vote_title),
                        stringResource(R.string.home_action_vote_desc),
                        Icons.Default.HowToVote,
                        Modifier.weight(1f)
                    ) {
                        navController.navigate("vote")
                    }
                    NdejjeQuickActionCard(
                        stringResource(R.string.home_action_results_title),
                        stringResource(R.string.home_action_results_desc),
                        Icons.Default.BarChart,
                        Modifier.weight(1f)
                    ) {
                        navController.navigate("results")
                    }
                    NdejjeQuickActionCard(
                        stringResource(R.string.home_action_profile_title),
                        stringResource(R.string.home_action_profile_desc),
                        Icons.Default.Person,
                        Modifier.weight(1f)
                    ) {
                        navController.navigate("profile")
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))

                // Election Status Box
                Text(
                    text = stringResource(R.string.home_election_status_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(R.dimen.font_size_h6).value.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    border = BorderStroke(dimensionResource(R.dimen.border_width), MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_medium))) {
                        Text(
                            text = stringResource(R.string.home_election_active),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = dimensionResource(R.dimen.font_size_large).value.sp
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
                        Text(
                            text = stringResource(R.string.home_election_ends_in, timeLeft),
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                            fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_large)))
                HorizontalDivider(thickness = dimensionResource(R.dimen.border_width), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard_size)))

                Text(
                    text = stringResource(R.string.home_total_votes, totalVotes),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = dimensionResource(R.dimen.font_size_h4).value.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.home_last_updated, lastVoteTime),
                    fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_extra_small))
                )
            }
        }
    }
}

