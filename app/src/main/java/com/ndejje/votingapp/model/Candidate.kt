package com.ndejje.votingapp.model

import androidx.annotation.DrawableRes

data class Candidate(
    val id: Int,
    val name: String,
    val position: String, // e.g., "Guild President"
    @DrawableRes val image: Int, // The photo of the candidate
    val voteCount: Int = 0
)