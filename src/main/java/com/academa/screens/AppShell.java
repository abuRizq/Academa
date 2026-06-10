package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.util.List;
import java.util.function.Consumer;

/**
 * AppShell — sidebar navigation + top bar + content area.
 * Mirrors the React AppShell component exactly.
 */
public final class AppShell extends HBox {

    // Nav configuration per role
    public record NavItem(String id, String label, String icon) {}
    public record NavGroup(String caption, List<NavItem> items) {}
    public record UserInfo(String name, String role, String status) {}
    public record RoleNav(UserInfo user, List<NavGroup> groups) {}

    private static final RoleNav LEARNER_NAV = new RoleNav(
        new UserInfo("Mara Quinn", "Learner · Grade 11", "online"),
        List.of(new NavGroup("Study", List.of(
            new NavItem("dashboard", "Dashboard", "layout-dashboard"),
            new NavItem("assessments", "My assessments", "file-pen-line"),
            new NavItem("results", "Results", "circle-check-big"),
            new NavItem("rankings", "Rankings", "trophy")
        )))
    );

    private static final RoleNav EDUCATOR_NAV = new RoleNav(
        new UserInfo("Dr. Elaine Voss", "Educator · Mathematics", "online"),
        List.of(new NavGroup("Workspace", List.of(
            new NavItem("dashboard", "Dashboard", "layout-dashboard"),
            new NavItem("assessments", "Assessments", "file-pen-line"),
            new NavItem("items", "Item bank", "list-checks"),
            new NavItem("analytics", "Analytics", "chart-no-axes-column"),
            new NavItem("rankings", "Rankings", "trophy"),
            new NavItem("roster", "Roster", "users")
        )))
    );

    private static final RoleNav ADMIN_NAV = new RoleNav(
        new UserInfo("R. Castellanos", "Administrator", "busy"),
        List.of(new NavGroup("Institution", List.of(
            new NavItem("dashboard", "Overview", "layout-dashboard"),
            new NavItem("governance", "Governance", "shield-check"),
            new NavItem("analytics", "Analytics", "chart-no-axes-column"),
            new NavItem("rankings", "Cohorts", "trophy"),
            new NavItem("settings", "Settings", "settings")
        )))
    );

    private final VBox sidebarNav;
    private final VBox sidebarFoot;
    private final Label topEyebrow;
    private final Label topTitle;
    private final HBox topActions;
    private final StackPane contentArea;
    private final VBox roleSwitchContainer;

    public AppShell(String role, String active, Consumer<String> onNav,
                    String eyebrow, String title, Node actions, Node roleSwitch, Node content) {
        getStyleClass().add("shell-root");

        RoleNav cfg = switch (role) {
            case "educator" -> EDUCATOR_NAV;
            case "admin" -> ADMIN_NAV;
            default -> LEARNER_NAV;
        };

        // === SIDEBAR ===
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("shell-side");

        // Brand
        HBox brand = new HBox(10);
        brand.getStyleClass().add("shell-brand");
        brand.setAlignment(Pos.CENTER_LEFT);

        StackPane logo = new StackPane();
        Rectangle rect = new Rectangle(30, 30);
        rect.setArcWidth(7); rect.setArcHeight(7);
        rect.setFill(Color.web("#2c40a0"));
        SVGPath aPath = new SVGPath();
        aPath.setContent("M15 7L24 26.6H21L19.8 22.4H14.2L12.9 26.6H10L15 7ZM15 15L13.4 19H16.6L15 15Z");
        aPath.setFill(Color.WHITE);
        aPath.setScaleX(0.85); aPath.setScaleY(0.85);
        logo.getChildren().addAll(rect, aPath);

        Label brandWord = new Label("Academa");
        brandWord.getStyleClass().add("shell-brand-word");
        brand.getChildren().addAll(logo, brandWord);

        // Nav
        sidebarNav = new VBox(2);
        sidebarNav.getStyleClass().add("shell-nav");
        VBox.setVgrow(sidebarNav, Priority.ALWAYS);

        for (int gi = 0; gi < cfg.groups().size(); gi++) {
            NavGroup g = cfg.groups().get(gi);
            Label cap = new Label(g.caption().toUpperCase());
            cap.getStyleClass().add("shell-nav-cap");
            if (gi > 0) VBox.setMargin(cap, new Insets(18, 0, 0, 0));
            sidebarNav.getChildren().add(cap);

            for (NavItem item : g.items()) {
                HBox navItem = new HBox(11);
                navItem.getStyleClass().add("shell-nav-item");
                navItem.setAlignment(Pos.CENTER_LEFT);
                navItem.setCursor(javafx.scene.Cursor.HAND);
                if (item.id().equals(active)) navItem.getStyleClass().add("active");

                Node icon = AcIcon.create(item.icon(), 18, Color.web("#565a68"));
                Label lbl = new Label(item.label());
                navItem.getChildren().addAll(icon, lbl);

                navItem.setOnMouseClicked(e -> {
                    if (onNav != null) onNav.accept(item.id());
                });

                sidebarNav.getChildren().add(navItem);
            }
        }

        // Role switch container
        roleSwitchContainer = new VBox();
        if (roleSwitch != null) roleSwitchContainer.getChildren().add(roleSwitch);

        // Footer
        sidebarFoot = new VBox();
        HBox footRow = new HBox(10);
        footRow.getStyleClass().add("shell-foot");
        footRow.setAlignment(Pos.CENTER_LEFT);

        AcAvatar userAvatar = new AcAvatar(cfg.user().name(), AcAvatar.Size.SM, false, cfg.user().status());
        VBox userInfo = new VBox();
        VBox.setVgrow(userInfo, Priority.SOMETIMES);
        Label userName = new Label(cfg.user().name());
        userName.getStyleClass().add("shell-user-name");
        Label userRole = new Label(cfg.user().role());
        userRole.getStyleClass().add("shell-user-role");
        userInfo.getChildren().addAll(userName, userRole);
        HBox.setHgrow(userInfo, Priority.ALWAYS);

        AcIconButton chevronBtn = new AcIconButton(
            AcIcon.create("chevrons-up-down", 16, Color.web("#565a68")),
            "Account", AcIconButton.Variant.GHOST, AcIconButton.Size.SM
        );

        footRow.getChildren().addAll(userAvatar, userInfo, chevronBtn);
        sidebarFoot.getChildren().add(footRow);

        sidebar.getChildren().addAll(brand, sidebarNav, roleSwitchContainer, sidebarFoot);

        // === MAIN AREA ===
        VBox mainArea = new VBox();
        HBox.setHgrow(mainArea, Priority.ALWAYS);

        // Top bar
        HBox topBar = new HBox(20);
        topBar.getStyleClass().add("shell-top");
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox titles = new VBox(1);
        topEyebrow = new Label(eyebrow != null ? eyebrow.toUpperCase() : "");
        topEyebrow.getStyleClass().add("shell-top-eyebrow");
        topTitle = new Label(title != null ? title : "");
        topTitle.getStyleClass().add("shell-top-title");
        titles.getChildren().addAll(topEyebrow, topTitle);
        HBox.setHgrow(titles, Priority.ALWAYS);

        topActions = new HBox(10);
        topActions.setAlignment(Pos.CENTER_RIGHT);

        // Search bar
        HBox searchBox = new HBox(8);
        searchBox.getStyleClass().add("shell-search");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        Node searchIcon = AcIcon.create("search", 16, Color.web("#8c909e"));
        TextField searchField = new TextField();
        searchField.setPromptText("Search assessments, items, learners…");
        searchField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0; -fx-font-size: 13.5px;");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        Label kbd = new Label("⌘K");
        kbd.getStyleClass().add("shell-search-kbd");
        searchBox.getChildren().addAll(searchIcon, searchField, kbd);

        topActions.getChildren().add(searchBox);
        if (actions != null) topActions.getChildren().add(actions);

        AcIconButton bellBtn = new AcIconButton(
            AcIcon.create("bell", 18, Color.web("#565a68")),
            "Notifications", AcIconButton.Variant.OUTLINE, AcIconButton.Size.MD
        );
        topActions.getChildren().add(bellBtn);

        topBar.getChildren().addAll(titles, topActions);

        // Content
        contentArea = new StackPane();
        contentArea.getStyleClass().add("shell-content");
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        if (content != null) {
            scrollPane.setContent(content);
        }
        contentArea.getChildren().add(scrollPane);

        mainArea.getChildren().addAll(topBar, contentArea);
        getChildren().addAll(sidebar, mainArea);
    }

    public void setContent(Node content) {
        if (!contentArea.getChildren().isEmpty()) {
            Node existing = contentArea.getChildren().get(0);
            if (existing instanceof ScrollPane sp) {
                sp.setContent(content);
                return;
            }
        }
        contentArea.getChildren().clear();
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent;");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentArea.getChildren().add(sp);
    }

    public void setTopTitle(String eyebrow, String title) {
        topEyebrow.setText(eyebrow != null ? eyebrow.toUpperCase() : "");
        topTitle.setText(title != null ? title : "");
    }

    public void setActions(Node actions) {
        // Remove old actions (keep search + bell)
        while (topActions.getChildren().size() > 2) {
            topActions.getChildren().remove(1);
        }
        if (actions != null) {
            topActions.getChildren().add(1, actions);
        }
    }
}
