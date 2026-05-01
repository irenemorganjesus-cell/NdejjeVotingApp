package com.ndejje.votingapp.view.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ndejje.votingapp.R
import com.ndejje.votingapp.model.CandidateEntity
import com.ndejje.votingapp.model.UserEntity
import com.ndejje.votingapp.view.components.NdejjePrimaryButton
import com.ndejje.votingapp.viewmodel.CandidateViewModel
import com.ndejje.votingapp.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
    navController: NavController,
    viewModel: CandidateViewModel,
    notificationViewModel: NotificationViewModel,
    user: UserEntity?
) {
    val candidates by viewModel.candidates.collectAsState()
    val context = LocalContext.current

    val guildPresident = stringResource(R.string.vote_pos_guild_president)
    val guildSpeaker = stringResource(R.string.vote_pos_guild_speaker)
    val grc = stringResource(R.string.vote_pos_grc)

    var selectedPosition by remember { mutableStateOf(guildPresident) }
    
    // Track selections for all three positions
    var selectedCandidates by remember { mutableStateOf(mapOf<String, Int?>()) }
    
    var showDialog by remember { mutableStateOf(false) }

    val positions = listOf(guildPresident, guildSpeaker, grc)

    // Check if user has already voted
    val alreadyVotedMsg = stringResource(R.string.vote_already_voted)
    LaunchedEffect(user) {
        if (user?.hasVoted == true) {
            Toast.makeText(context, alreadyVotedMsg, Toast.LENGTH_LONG).show()
            navController.navigate("home") {
                popUpTo("vote") { inclusive = true }
            }
        }
    }

    if (user?.hasVoted == true) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    // --- CONFIRMATION DIALOG LOGIC ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.vote_confirm_title), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = { 
                Column {
                    Text(stringResource(R.string.vote_confirm_message), color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                    positions.forEach { pos ->
                        Text(stringResource(R.string.vote_confirm_section_completed, pos), fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val ids = selectedCandidates.values.filterNotNull()
                    viewModel.confirmVotes(ids, user?.registrationNumber ?: "") {
                        notificationViewModel.addNotification(
                            title = "Vote Successful",
                            message = "Your vote has been recorded successfully. Thank you!",
                            type = "SUCCESS"
                        )
                        showDialog = false
                        navController.navigate("vote_success")
                    }
                }) {
                    Text(stringResource(R.string.vote_btn_submit_all), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.vote_btn_cancel), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.vote_title), fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.font_size_h3).value.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text(stringResource(R.string.vote_subtitle), fontSize = dimensionResource(R.dimen.font_size_medium).value.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back_button_desc), 
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background).padding(horizontal = dimensionResource(R.dimen.padding_medium))) {

            // 1. Position Navigation Tabs
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = dimensionResource(R.dimen.form_elements_margin_top)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.vote_tab_spacing))
            ) {
                positions.forEach { pos ->
                    val isSelected = selectedPosition == pos
                    val isCompleted = selectedCandidates[pos] != null
                    
                    Button(
                        onClick = {
                            selectedPosition = pos
                            viewModel.fetchCandidates(pos)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else if (isCompleted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else if (isCompleted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f).height(dimensionResource(R.dimen.vote_tab_height)),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
                        border = if (isCompleted && !isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Text(pos, fontSize = dimensionResource(R.dimen.vote_tab_font_size).value.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            }

            // 2. Instructions Divider
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = dimensionResource(R.dimen.form_elements_margin_top)), contentAlignment = Alignment.Center) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Text(
                    stringResource(R.string.vote_current_position, selectedPosition),
                    modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(horizontal = dimensionResource(R.dimen.form_elements_margin_top)),
                    fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // 3. Candidate Selection List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.padding_medium))
            ) {
                items(candidates) { candidate ->
                    CandidateVoteCard(
                        candidate = candidate,
                        isSelected = selectedCandidates[selectedPosition] == candidate.id,
                        onSelect = {
                            selectedCandidates = selectedCandidates.toMutableMap().apply {
                                put(selectedPosition, candidate.id)
                            }
                        }
                    )
                }
            }

            // 4. Submit Button
            val allSelected = positions.all { selectedCandidates[it] != null }
            
            NdejjePrimaryButton(
                text = if (allSelected) stringResource(R.string.vote_btn_submit_final) else stringResource(R.string.vote_btn_complete_selections, selectedCandidates.size),
                onClick = { showDialog = true },
                enabled = allSelected,
                modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.vote_submit_button_height))
            )

            Text(
                stringResource(R.string.vote_selections_summary, selectedCandidates.size),
                modifier = Modifier.fillMaxWidth().padding(vertical = dimensionResource(R.dimen.form_elements_margin_top)),
                textAlign = TextAlign.Center,
                fontSize = dimensionResource(R.dimen.font_size_caption).value.sp,
                color = if (allSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = if (allSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun CandidateVoteCard(candidate: CandidateEntity, isSelected: Boolean, onSelect: () -> Unit) {
    val context = LocalContext.current

    val imageResId = remember(candidate.imageUrl) {
        val resourceId = context.resources.getIdentifier(
            candidate.imageUrl.lowercase().trim(),
            "drawable",
            context.packageName
        )
        if (resourceId != 0) resourceId else R.drawable.male_candidate
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.vote_candidate_card_elevation)),
        border = if (isSelected) BorderStroke(3.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)), verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = painterResource(id = imageResId),
                contentDescription = stringResource(R.string.vote_candidate_image_desc, candidate.name),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.vote_candidate_image_size))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f).padding(horizontal = dimensionResource(R.dimen.padding_medium))) {
                Text(candidate.name, fontWeight = FontWeight.Bold, fontSize = dimensionResource(R.dimen.font_size_h5).value.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(candidate.course, fontSize = dimensionResource(R.dimen.font_size_large).value.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text("\"${candidate.motto}\"", fontSize = dimensionResource(R.dimen.font_size_medium).value.sp, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurface)
            }

            Box(
                modifier = Modifier.size(dimensionResource(R.dimen.vote_selection_circle_size)).clip(CircleShape)
                    .border(2.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(modifier = Modifier.size(dimensionResource(R.dimen.vote_selection_inner_circle_size)).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                }
            }
        }
    }
}
