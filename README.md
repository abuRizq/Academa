# Academa — JavaFX Desktop Application

Academic assessment platform built with Java 21 and JavaFX 21.

This is a faithful port of the original React + Vite web application to a native Java desktop application.

## Features

- **Login Screen** — Split-panel with brand showcase and sign-in form
- **Learner Dashboard** — Cohort standing, rankings, and assessment availability matrix
- **Focus Execution** — Immersive assessment-taking environment with timer and question navigation
- **Results** — Score breakdown, item-by-item review, and cohort leaderboard
- **Educator Authoring** — Three-column assessment editor with item bank
- **Admin Governance** — Entity management with stats, table, bulk actions, and pagination
- **Role Switching** — Demo switcher between learner, educator, and admin roles

## Requirements

- Java 21+
- Maven 3.9+ (Maven Wrapper included)

## Build & Run

```bash
# Build
./mvnw.cmd compile

# Run
./mvnw.cmd javafx:run
```

## Project Structure

```
src/main/java/com/academa/
├── AcademaApp.java          # Main application + routing
├── AcademaLauncher.java     # Fat JAR launcher
├── components/              # Reusable UI components
│   ├── AcAvatar.java
│   ├── AcBadge.java
│   ├── AcButton.java
│   ├── AcCard.java
│   ├── AcCheckbox.java
│   ├── AcIcon.java
│   ├── AcIconButton.java
│   ├── AcInput.java
│   ├── AcProgressBar.java
│   ├── AcSelect.java
│   ├── AcTabs.java
│   └── AcTag.java
└── screens/                 # Application screens
    ├── AdminGovernance.java
    ├── AppShell.java
    ├── EducatorAuthoring.java
    ├── FocusExecution.java
    ├── LearnerDashboard.java
    ├── LoginScreen.java
    └── ResultsScreen.java

src/main/resources/com/academa/
└── styles/
    └── academa.css           # Complete design system stylesheet
```

## Design System

The application uses a comprehensive JavaFX CSS design system that faithfully ports all design tokens from the original React application:

- **Colors**: Ink scale, brand, gold, green, amber, garnet palettes
- **Typography**: System fonts (Segoe UI) with display/sans/mono variations
- **Spacing**: 4px grid system with consistent padding/margins
- **Radii**: xs through pill border radius tokens
- **Shadows**: xs through xl shadow tokens
- **Components**: Button, Badge, Card, Input, Select, Tabs, Tag, Avatar, ProgressBar, etc.
