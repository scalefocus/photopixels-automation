# PhotoPixels Automation Framework

## Table of Contents

- [PhotoPixels Automation Framework](#photopixels-automation-framework)
    - [Tech Stack](#tech-stack)
    - [Structure](#structure)
        - [Project Tree](#project-tree)
        - [Main Functionality](#main-functionality)
        - [Tests](#tests)
    - [Project Setup](#project-setup)
    - [Running Tests](#running-tests)
    - [Reports](#reports)
    - [Nightly Runs](#nightly-runs)

---

This repository contains the **automation framework** for [PhotoPixels](https://github.com/scalefocus/photopixels) – a photo management and storage platform designed to provide independence, openness, and a strong focus on security.

The **PhotoPixels Automation Framework** is a robust testing solution for the platform, enabling automated validation of backend APIs, frontend UI, and mobile applications.  
It is built with a focus on **modularity, scalability, and detailed reporting**.

The framework provides automated test coverage for:
- **Backend (API)** tests with RestAssured
- **Frontend (UI)** tests with Selenium + TestNG
- **Mobile** tests using Appium + uiautomator2

All tests are integrated with **Allure** for detailed reporting and analysis.

---

## Tech Stack

- **Java 17+**
- **Maven 3.8+**
- **TestNG**
- **Selenium WebDriver**
- **RestAssured**
- **Appium + uiautomator2**
- **Allure Reports**
- **GitHub Actions** (CI/CD)

---

## Structure

The framework is developed using Page Object and Page Factory patterns.  
It follows the Maven convention for project structure.

### Project Tree

```text
src/
├─ main/
│ ├─ java/com/photopixels/
│ │ ├─ api/ # API layer: DTOs, factories, steps for API interactions
│ │ │ ├─ dtos/ # Data Transfer Objects for API requests/responses
│ │ │ ├─ factories/ # Factory classes for building DTOs/test data
│ │ │ └─ steps/ # Step definitions wrapping API calls
│ │ ├─ constants/ # Project-wide constants
│ │ ├─ enums/ # Enumerations for consistent values
│ │ ├─ helpers/ # Helper utilities (common logic for tests)
│ │ ├─ mobile/pages/ # Page Objects for mobile application
│ │ └─ web/pages/ # Page Objects for web application (UI)
│ │ └─ email/ # Specialized page objects for email flows
│ └─ resources/ # Configuration and property files
│
└─ test/
├─ java/com/photopixels/
│ ├─ api/ # API test classes grouped by domain (admin, users, etc.)
│ ├─ base/ # Base test classes
│ ├─ listeners/ # TestNG listeners (reporting, logging, etc.)
│ ├─ mobile/ # Mobile test classes
│ └─ web/ # Web/UI test classes
└─ resources/
├─ META-INF/services # Service loader configs
└─ testing_suites/ # TestNG XML suite definitions
```


### Main functionality

There are several important packages:

- **setup** – contains all functionality for bootstrapping the tests (driver management, environment-specific data, properties, listeners, base test class).
- **helpers** – helper classes used by tests and page objects (element interactions, waiting strategies, assertions, etc.).
- **enums** – enums for consistent and maintainable usage across the framework.
- **pages** – Page Object classes; a dedicated PO exists for each distinct page/screen.

### Tests

- Test classes are located under the `tests` package and grouped by feature/module.
- TestNG suites are located under the `testing_suites` package.
- Tests for a given functionality should be grouped in the same test class.
- The list of available suite XML files can be found under `src/test/resources/testing_suites`.


## Project Setup

Before running the tests, make sure you have the following installed:

- **Java 17+** (set `JAVA_HOME`)
- **Maven 3.8+**
- **Git**

For running reports:
- **Allure Commandline** (optional, for serving reports locally)

For mobile tests (required additionally):
- **Android Studio** (SDK, Emulator, Build Tools)
- **Node.js LTS** (for Appium Server)
- **Appium Server** (installed globally via npm)
- **uiautomator2** Appium driver


## Running Tests

Navigate to the project root where the pom.xml file is located.

### Build project without executing tests

`mvn clean install -DskipTests=true`

### Run tests

Backend (API) tests: `mvn clean test -DsuiteXmlFile=api.xml`

Frontend (UI) tests: `mvn clean test -DsuiteXmlFile=web.xml`

Mobile tests: `mvn clean test -DsuiteXmlFile=mobile.xml`

Note: For mobile tests, a connected device or emulator must be available.
Project-specific mobile configuration is located in mobile.properties (under src/main/resources).
Ensure that the application APK is placed in the application folder.

## Reports
The framework uses Allure for reporting.

- Generate a report after a test run: `mvn allure:report`
- Serve the report locally (interactive HTML): `allure serve target/allure-results`
- Generate a report and save history between runs: `mvn allure:report validate`

Reports provide:

- Test execution overview
- Passed/failed/skipped breakdown
- Step-by-step details
- Screenshots for failed UI tests

## Nightly Runs

- Automated nightly test runs are executed via GitHub Actions.

- Currently, nightly runs are enabled for Backend (API) tests.

- Frontend (UI) and Mobile test runs will be added soon.

- The pipeline uses a self-hosted runner to spin up a local environment and execute the tests.

- Results are published and stored for analysis.


