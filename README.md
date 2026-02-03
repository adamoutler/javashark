# JavaShark

JavaShark is a robust automation framework designed to orchestrate and control Android applications running within VirtualBox instances. It utilizes a novel combination of ADB (Android Debug Bridge) integration, lightweight image hashing for state detection, and OCR (Optical Character Recognition) to perform complex automated tasks.

## Key Features

*   **Virtual Machine Orchestration:** Manages Android instances running in VirtualBox as if they were physical devices.
*   **High-Performance State Detection:** Uses a unique `HashedPicCoords` system to identify screen states (buttons, dialogs, game screens) by hashing specific sub-regions of screenshots. This is significantly faster than traditional full-screen computer vision techniques.
*   **Command-Driven Architecture:** Features a built-in HTTP server (`CommandDistributor`) that allows for remote control and task assignment via a RESTful API.
*   **Automated OCR:** Integrates with Tesseract to extract text data from the screen for logging and decision-making.
*   **Resilient Task Management:** A central `TaskManager` handles long-running automation sequences, error recovery, and complex decision trees.

## Architecture

The project is structured into several key modules:

*   **JavaShark:** The core application containing the server, task manager, and device control logic.
*   **CatsDataManipulator:** A utility module for processing and analyzing data extracted from the application.
*   **LibMinimalChatExtrem:** A library for handling chat-based notifications and logging.

## Prerequisites

*   **Java 8 or higher:** The project is built using Java.
*   **Android Debug Bridge (ADB):** Must be installed and available in your system's PATH.
*   **VirtualBox:** Required for hosting the Android virtual machines.
*   **Tesseract OCR:** Required for text recognition features.

## Configuration

1.  **Properties:** Copy `JavaShark/properties.prop` and configure your local settings.
    *   `my_secret_password`: Set your secure password.
    *   Other paths can be adjusted as needed.
2.  **Google Cloud:** If using translation or advanced API features, place your Google service account JSON key in `JavaShark/src/main/resources/`.

## Running the Application

Build the project using Maven:

```bash
mvn clean install
```

Run the main application:

```bash
java -jar JavaShark/target/JavaShark-1.0-SNAPSHOT.jar
```

## Disclaimer

This project is for educational and research purposes. It demonstrates advanced techniques in automation, image recognition, and system integration.

## License

[License Information Here]
