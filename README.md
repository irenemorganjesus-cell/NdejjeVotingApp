# Ndejje Voting App 🗳️

A modern, secure, and inclusive mobile voting application designed specifically for Ndejje University students. This app transitions the student guild elections from traditional paper-based ballots to a reliable digital platform using Android's latest technologies.

## 🌟 Key Features

- **Secure Login & Registration**: Students register using their university credentials (Registration Number, full name, university email, course, password).
- **Candidate Profiles**: View detailed information about candidates, including their photos, mottos, and party affiliations.
- **Atomic Voting System**: A robust voting mechanism that ensures one student, one vote, with immediate confirmation.
- **Real-Time Notifications**: Stay updated with election news and reminders via the in-app notification center.
- **Inclusive Design**: Featuring gender-neutral avatars and Material 3 design principles for a modern, accessible user experience.
- **Privacy First**: Decouples user identity from ballot choices to maintain a strict secret ballot principle.

## 🛠️ Tech Stack

- **UI Framework**: Jetpack Compose (Modern Declarative UI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library (Offline-first data management)
- **Language**: Kotlin
- **Dependency Management**: Version Catalogs (libs.versions.toml)
- **Testing**: 
    - **Unit Testing**: MockK & Turbine
    - **UI Testing**: ComposeTestRule
    - **Integration Testing**: Room In-Memory Database tests
      ###📊 Test Summary (QA Report)

As the Testing and QA Engineer (Muyanja), I have executed a comprehensive test suite to ensure the application's reliability and integrity.

- **Unit Tests (`AuthViewModelTest`, `CandidateViewModelTest`)**:
    - Verified authentication logic, registration input validation, and ViewModel state transitions.
    - Confirmed that vote counting logic is atomic and accurate in the business layer.
    - **Outcome**: 100% Pass.
- **Instrumented UI Tests (`VotingFlowTest`)**:
    - Simulated the complete student journey: App Launch -> Login -> Candidate Selection -> Vote Submission -> Success Feedback.
    - Verified UI responsiveness and accessibility across different screen states.
    - **Outcome**: Successful end-to-end execution on physical and emulator devices.
- **Integration Tests (`DatabaseIntegrationTest`)**:
    - Tested the Room database in isolation using an in-memory instance to protect real data.
    - Validated user persistence, "one-vote-per-student" enforcement, and notification read/write states.
    - **Outcome**: Data integrity and persistence logic verified.

**Overall Status**: ✅ **VERIFIED & READY FOR DEPLOYMENT**


## 🧪 Quality Assurance

We have implemented a multi-layered QA framework to ensure election integrity:
- **Unit Tests**: Validate business logic in ViewModels.
- **Instrumented UI Tests**: Simulate the entire student journey from login to voting.
- **Database Tests**: Ensure data persistence and atomic vote counting.

## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/irenemorganjesus-cell/NdejjeVotingApp.git
   ```
2. **Open in Android Studio**:
   - Ensure you are using Android Studio Ladybug or newer.
3. **Build the Project**:
   - Run `./gradlew assembleDebug` to generate the APK.
4. **Run Tests**:
   - Execute `./gradlew test` for unit tests.
   - Execute `./gradlew connectedAndroidTest` for UI and Integration tests.

## 👥 Contributors

- **Lead Developer and Project Cordinator**: [Ssenono Francis Xavier]
- **Git and Quality Assurance Manager**: [Namanda Irene]
- **UI/UX Designer**: [Sharifah]
- **Testing and Quality Assurance Engineer**: [Muyanja Muhammad](https://github.com/muyanjamuhammad5)
- **Documentation and Research Lead**: [Aksam Kalungi](mailto:aksamkalungi250@gmail.com)

---
*Developed for Ndejje University Guild Elections 2026/2027.*
