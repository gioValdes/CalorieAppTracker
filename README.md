# Calorie Tracker App

## Overview

Calorie Tracker is an educational Android application example designed to showcase **successful release strategies** for Android apps. While the app does not provide actual functionality, it serves as a clean, structured reference for implementing features and release workflows. The app uses the **MVVM (Model-View-ViewModel)** design pattern to demonstrate best practices in architecture and scalability.

---

## Features

- Example of clean architecture using MVVM.
- Demonstrates release strategies and workflows.
- Illustrates modular project structure.

---

## Architecture

The project adheres to the **MVVM** design pattern and is structured as follows:

### Project Structure

- **Models**: Encapsulates the app's core data structures and business logic.
- **Views**: Handles the UI and user interaction, implemented using XML layouts and Jetpack Compose.
- **ViewModels**: Acts as a bridge between the View and Model, exposing data in a format suitable for the View.
- **Repositories**: Manages data sources, such as APIs or local databases.
- **Services**: Contains reusable components for additional features like notifications or analytics.

### Folder Structure

```plaintext
CalorieTracker/
├── data/
│   └── FoodItem
├── domain/
│   └── FoodRepository
├── presentation/
│   ├── FoodViewModel
│   └── MainActivity
├── resources/
│   ├── values
│   └── values-es
└── tests/

```

## Installation

To explore the project locally, follow these steps:

### Prerequisites

Android Studio: Ensure you have the latest version of Android Studio installed.

Gradle: Pre-installed with Android Studio.

### Steps

1. Clone the repository:

```plaintext
git clone git@github.com:gioValdes/CalorieAppTracker.git
cd CalorieAppTracker
```

- Open the project:

2. Open CalorieAppTracker in Android Studio.

- Build and explore the project:

3. Select a target emulator or physical device.

- Click the Run button or press Shift + F10 to simulate a build.


## Testing

To explore the test suite:

1. Open the project in Android Studio.

2. Select a testing configuration.

3. Run the tests using Shift + F10 or the dedicated testing button.

The test cases are located in the tests/ directory and demonstrate strategies for testing core functionalities.
