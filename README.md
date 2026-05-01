# Ndejje University Digital Ballot 🗳️

A modern, secure, and inclusive mobile voting application designed specifically for Ndejje University students. This app transitions the student guild elections from traditional paper-based ballots to a reliable digital platform using Android's latest technologies.

## 🌟 Key Features

- **Secure Login & Registration**: Students register using their university credentials (Registration Number and Email).
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

## 🧪 Quality Assurance

We have implemented a multi-layered QA framework to ensure election integrity:
- **Unit Tests**: Validate business logic in ViewModels.
- **Instrumented UI Tests**: Simulate the entire student journey from login to voting.
- **Database Tests**: Ensure data persistence and atomic vote counting.

## 🚀 Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/aksamkalungi2500/NdejjeVotingApp.git
   ```
2. **Open in Android Studio**:
   - Ensure you are using Android Studio Ladybug or newer.
3. **Build the Project**:
   - Run `./gradlew assembleDebug` to generate the APK.
4. **Run Tests**:
   - Execute `./gradlew test` for unit tests.
   - Execute `./gradlew connectedAndroidTest` for UI and Integration tests.

## 👥 Contributors

- **Lead Developer**: [Ssenono Francis]
- **UI/UX Designer**: [Sharifah]
- **QA Engineer**: [Muyanja Muhammad](https://github.com/muyanjamuhammad5)
- **Project Coordinator**: [Aksam Kalungi](mailto:aksamkalungi250@gmail.com)

---
*Developed for Ndejje University Guild Elections 2024/2025.*
