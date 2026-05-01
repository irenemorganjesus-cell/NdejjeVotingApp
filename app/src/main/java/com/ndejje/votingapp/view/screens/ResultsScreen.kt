package com.ndejje.votingapp.view.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ndejje.votingapp.R
import com.ndejje.votingapp.ui.theme.*
import com.ndejje.votingapp.view.components.BottomNavigationBar
import com.ndejje.votingapp.viewmodel.CandidateViewModel

data class CandidateResult(val name: String, val votes: Int, val percentage: Float, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, candidateViewModel: CandidateViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val positions = listOf("Guild President", "Guild Speaker", "GRC")
    var selectedPosition by remember { mutableStateOf(positions[0]) }

    val candidates by candidateViewModel.candidates.collectAsState()

    // Fetch candidates whenever position changes
    LaunchedEffect(selectedPosition) {
        candidateViewModel.fetchCandidates(selectedPosition)
    }

    val totalVotesForPosition = candidates.sumOf { it.voteCount }
    val chartColors = listOf(NdejjeBlue, SuccessGreen, WarningOrange, AlertRed, InfoBlue, NdejjeYellow)
    
    val currentResults = candidates.mapIndexed { index, candidate ->
        val percentage = if (totalVotesForPosition > 0) {
            (candidate.voteCount.toFloat() / totalVotesForPosition) * 100
        } else 0f
        CandidateResult(candidate.name, candidate.voteCount, percentage, chartColors[index % chartColors.size])
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.results_header_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.results_back_desc),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Column(modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))) {
                    Text(
                        text = stringResource(R.string.results_title),
                        fontSize = dimensionResource(R.dimen.font_size_title_header).value.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.results_subtitle),
                        fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // Selection and Content Card
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(
                    topStart = dimensionResource(R.dimen.results_card_radius),
                    topEnd = dimensionResource(R.dimen.results_card_radius)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.results_content_padding))
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Position Dropdown
                    Text(
                        text = stringResource(R.string.label_select_position),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_extra_small))
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedPosition,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            positions.forEach { position ->
                                DropdownMenuItem(
                                    text = { Text(position, color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = {
                                        selectedPosition = position
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                    // Candidates Results
                    if (currentResults.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.chart_size)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.error_no_candidates),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        // Advanced Visualization: Doughnut Chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.chart_size)),
                            contentAlignment = Alignment.Center
                        ) {
                            ResultsDoughnutChart(currentResults)
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$totalVotesForPosition",
                                    fontSize = dimensionResource(R.dimen.font_size_h3).value.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Votes",
                                    fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

                        currentResults.forEach { candidate ->
                            CandidateResultItem(candidate)
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.candidate_item_spacing)))
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_medium)),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                    
                    val lastVoteTime by candidateViewModel.lastVoteTime.collectAsState()
                    
                    Text(
                        text = stringResource(R.string.label_last_updated_format, lastVoteTime),
                        fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultsDoughnutChart(results: List<CandidateResult>) {
    val chartSize = dimensionResource(R.dimen.chart_size)
    val thickness = dimensionResource(R.dimen.chart_thickness)
    
    // Animation for the chart segments
    val animatedSegments = results.map { result ->
        animateFloatAsState(
            targetValue = (result.percentage / 100f) * 360f,
            animationSpec = tween(durationMillis = 1000),
            label = "SegmentAnimation"
        )
    }

    Canvas(
        modifier = Modifier
            .size(chartSize)
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        var startAngle = -90f
        animatedSegments.forEachIndexed { index, segmentAngle ->
            drawArc(
                color = results[index].color,
                startAngle = startAngle,
                sweepAngle = segmentAngle.value,
                useCenter = false,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Round)
            )
            startAngle += segmentAngle.value
        }
    }
}

@Composable
fun CandidateResultItem(candidate: CandidateResult) {
    val animatedProgress by animateFloatAsState(
        targetValue = candidate.percentage / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "ProgressAnimation"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.padding_small))
                        .background(candidate.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = candidate.name,
                    fontSize = dimensionResource(R.dimen.font_size_h6).value.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "${candidate.votes} votes",
                fontSize = dimensionResource(R.dimen.font_size_medium).value.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(dimensionResource(R.dimen.progress_bar_height))
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(dimensionResource(R.dimen.progress_bar_radius))
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(
                            candidate.color,
                            RoundedCornerShape(dimensionResource(R.dimen.progress_bar_radius))
                        )
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Text(
                text = "${candidate.percentage.toInt()}%",
                fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                fontWeight = FontWeight.Black,
                color = candidate.color
            )
        }
    }
}
