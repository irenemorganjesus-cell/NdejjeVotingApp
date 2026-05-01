package com.ndejje.votingapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.ndejje.votingapp.ui.theme.NdejjeVotingAppTheme
import com.ndejje.votingapp.view.navigation.VotingNavGraph
import com.ndejje.votingapp.model.AppDatabase
import com.ndejje.votingapp.model.UserEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VotingFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() = runBlocking {
        // Clear database and seed a test user for the login flow
        val db = AppDatabase.getDatabase(context)
        db.clearAllTables()
        
        val testUser = UserEntity(
            registrationNumber = "25/2/303/D/001",
            fullName = "Test User",
            password = "password123",
            email = "test@student.ndejje.ac.ug",
            course = "B.IT",
            yearOfStudy = "3",
            campus = "Main Campus"
        )
        db.userDao().registerUser(testUser)
    }

    @Test
    fun testFullVotingFlow() {
        // Start the app
        composeTestRule.setContent {
            NdejjeVotingAppTheme {
                VotingNavGraph()
            }
        }

        // 1. Welcome Screen - Click Login
        val loginBtnText = context.getString(R.string.btn_login)
        composeTestRule.onNodeWithText(loginBtnText).performClick()

        // 2. Login Screen
        val regNoLabel = context.getString(R.string.label_reg_no)
        val passwordLabel = context.getString(R.string.label_password)
        
        composeTestRule.onNodeWithText(regNoLabel).performTextInput("25/2/303/D/001")
        composeTestRule.onNodeWithText(passwordLabel).performTextInput("password123")
        
        // Find the login button - it's an NdejjePrimaryButton with btn_login text
        composeTestRule.onNodeWithText(loginBtnText).performClick()

        // 3. Home Screen - Verify greeting and click "Vote"
        val greetingPrefix = context.getString(R.string.home_greeting, "").trim()
        composeTestRule.onNodeWithText(greetingPrefix, substring = true).assertIsDisplayed()
        
        val voteActionTitle = context.getString(R.string.home_action_vote_title)
        composeTestRule.onNodeWithText(voteActionTitle).performClick()

        // 4. Vote Screen - Select candidates for all 3 positions
        
        // Position 1: Guild President (Selected by default)
        // Click on the first candidate card (we know "Kato Brian" is seeded)
        composeTestRule.onNodeWithText("Kato Brian").performClick()
        
        // Position 2: Guild Speaker
        val speakerPos = context.getString(R.string.vote_pos_guild_speaker)
        composeTestRule.onNodeWithText(speakerPos).performClick()
        composeTestRule.onNodeWithText("Aksam Kalungi").performClick()
        
        // Position 3: GRC
        val grcPos = context.getString(R.string.vote_pos_grc)
        composeTestRule.onNodeWithText(grcPos).performClick()
        composeTestRule.onNodeWithText("Sharifah Namaganda").performClick()
        
        // 5. Submit Final Votes
        val submitBtnText = context.getString(R.string.vote_btn_submit_final)
        composeTestRule.onNodeWithText(submitBtnText).assertIsEnabled().performClick()
        
        // 6. Confirm Dialog
        val confirmAllBtnText = context.getString(R.string.vote_btn_submit_all)
        composeTestRule.onNodeWithText(confirmAllBtnText).performClick()

        // 7. Verify Success Screen
        val successMsg = context.getString(R.string.success_title)
        composeTestRule.onNodeWithText(successMsg).assertIsDisplayed()
        
        val backToHomeBtn = context.getString(R.string.success_btn_home)
        composeTestRule.onNodeWithText(backToHomeBtn).performClick()
        
        // 8. Back on Home - Verify user is marked as voted (or at least back on Home)
        composeTestRule.onNodeWithText(greetingPrefix, substring = true).assertIsDisplayed()
    }
}
