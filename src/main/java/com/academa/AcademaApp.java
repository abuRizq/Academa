package com.academa;

import com.academa.components.*;
import com.academa.screens.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Main Academa Application — JavaFX port.
 * Manages routing, role switching, and screen composition.
 * Mirrors the React App component exactly.
 */
public class AcademaApp extends Application {

    public AcademaApp() {
    }

    private String route = "login";
    private String role = "learner";
    private String nav = "dashboard";

    private StackPane rootPane;

    private static final Map<String, Map<String, String[]>> TITLES = Map.of(
        "learner", Map.of(
            "dashboard", new String[]{"My workspace", "Dashboard"},
            "assessments", new String[]{"My workspace", "Assessments"},
            "results", new String[]{"Performance", "Results — Linear Algebra"},
            "rankings", new String[]{"Performance", "Cohort rankings"}
        ),
        "educator", Map.ofEntries(
            Map.entry("dashboard", new String[]{"Mathematics", "Dashboard"}),
            Map.entry("assessments", new String[]{"Mathematics", "Assessments"}),
            Map.entry("items", new String[]{"Repository", "Item bank"}),
            Map.entry("analytics", new String[]{"Performance", "Analytics"}),
            Map.entry("rankings", new String[]{"Performance", "Cohort rankings"}),
            Map.entry("roster", new String[]{"Mathematics", "Roster"}),
            Map.entry("authoring", new String[]{"Mathematics", "Assessment authoring"})
        ),
        "admin", Map.of(
            "dashboard", new String[]{"Northbridge Institute", "Governance overview"},
            "governance", new String[]{"Northbridge Institute", "Global entity directives"},
            "analytics", new String[]{"Institution", "Analytics"},
            "rankings", new String[]{"Institution", "Cohorts"},
            "settings", new String[]{"Institution", "Settings"}
        )
    );

    @Override
    public void start(Stage stage) {
        rootPane = new StackPane();
        rootPane.setStyle("-fx-background-color: #faf9f5;");

        // Adapt to actual screen size
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double winW = Math.min(1440, screenBounds.getWidth() * 0.92);
        double winH = Math.min(900, screenBounds.getHeight() * 0.90);

        Scene scene = new Scene(rootPane, winW, winH);

        // Load CSS
        String css = getClass().getResource("/com/academa/styles/academa.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Academa");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        // Center on screen
        stage.setX(screenBounds.getMinX() + (screenBounds.getWidth() - winW) / 2);
        stage.setY(screenBounds.getMinY() + (screenBounds.getHeight() - winH) / 2);
        stage.show();

        render();
    }

    private void render() {
        rootPane.getChildren().clear();

        if ("login".equals(route)) {
            LoginScreen login = new LoginScreen(() -> {
                role = "learner";
                nav = "dashboard";
                route = "app";
                render();
            });
            rootPane.getChildren().add(login);
            return;
        }

        if ("focus".equals(route)) {
            FocusExecution focus = new FocusExecution(
                () -> { route = "app"; render(); },
                () -> { nav = "results"; route = "app"; render(); }
            );
            rootPane.getChildren().add(focus);
            return;
        }

        // App shell
        Node screen = buildScreen();
        String[] titleMap = TITLES.getOrDefault(role, Map.of())
            .getOrDefault(nav, TITLES.getOrDefault(role, Map.of())
                .getOrDefault("dashboard", new String[]{"Academa", "Dashboard"}));

        Node actions = buildActions();
        Node roleSwitch = buildRoleSwitch();

        AppShell shell = new AppShell(
            role, nav,
            id -> { nav = id; render(); },
            titleMap[0], titleMap[1],
            actions, roleSwitch, screen
        );

        rootPane.getChildren().add(shell);
    }

    private Node buildScreen() {
        return switch (role) {
            case "learner" -> switch (nav) {
                case "dashboard" -> new LearnerDashboard(() -> { route = "focus"; render(); });
                case "assessments" -> new LearnerAssessments(() -> { route = "focus"; render(); });
                case "results" -> new ResultsScreen();
                case "rankings" -> new RankingsScreen("learner");
                default -> new LearnerDashboard(() -> { route = "focus"; render(); });
            };
            case "educator" -> switch (nav) {
                case "dashboard" -> new EducatorDashboard();
                case "assessments" -> new EducatorAssessments(() -> { nav = "authoring"; render(); });
                case "items" -> new ItemBankScreen();
                case "analytics" -> new AnalyticsScreen("educator");
                case "rankings" -> new RankingsScreen("educator");
                case "roster" -> new RosterScreen();
                case "authoring" -> new EducatorAuthoring();
                default -> new EducatorDashboard();
            };
            case "admin" -> switch (nav) {
                case "dashboard" -> new AdminOverview();
                case "governance" -> new AdminGovernance();
                case "analytics" -> new AnalyticsScreen("institution");
                case "rankings" -> new CohortsScreen();
                case "settings" -> new SettingsScreen();
                default -> new AdminOverview();
            };
            default -> new LearnerDashboard(() -> { route = "focus"; render(); });
        };
    }

    private Node buildActions() {
        if ("educator".equals(role)) {
            return new AcButton("Publish", AcButton.Variant.PRIMARY, AcButton.Size.SM,
                AcIcon.create("check", 14, Color.WHITE), null);
        }
        if ("learner".equals(role)) {
            AcButton btn = new AcButton("Resume focus", AcButton.Variant.SECONDARY, AcButton.Size.SM,
                AcIcon.create("play", 14, Color.web("#1b1d25")), null);
            btn.setOnAction(e -> { route = "focus"; render(); });
            return btn;
        }
        return null;
    }

    private Node buildRoleSwitch() {
        VBox container = new VBox();
        container.setPadding(new Insets(0, 2, 8, 2));

        Label cap = new Label("PREVIEW AS");
        cap.getStyleClass().add("demo-rolesw-cap");

        HBox sw = new HBox(2);
        sw.getStyleClass().add("demo-rolesw");

        for (String r : new String[]{"learner", "educator", "admin"}) {
            Label btn = new Label(r.substring(0, 1).toUpperCase() + r.substring(1));
            btn.getStyleClass().add("demo-rolesw-btn");
            if (r.equals(role)) btn.getStyleClass().add("active");
            btn.setCursor(javafx.scene.Cursor.HAND);
            btn.setAlignment(Pos.CENTER);
            btn.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(btn, Priority.ALWAYS);

            btn.setOnMouseClicked(e -> {
                this.role = r;
                this.nav = "dashboard";
                render();
            });

            sw.getChildren().add(btn);
        }

        container.getChildren().addAll(cap, sw);
        return container;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
