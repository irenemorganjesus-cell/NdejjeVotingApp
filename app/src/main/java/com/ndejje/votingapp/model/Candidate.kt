package com.ndejje.votingapp.model

import androidx.annotation.DrawableRes

data class Candidate(
    val id: Int,
    val fullName: String,
    val position: String, // e.g., "Guild President" or "GRC"
    val manifestoSummary: String,
    @DrawableRes val imageRes: Int, // Link to the photo in res/drawable
    var votes: Int = 0 //votes keep hanging
)