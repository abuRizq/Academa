package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * AdminGovernance — admin overview with stats, entity table, and governance tools.
 * Mirrors the React AdminGovernance component exactly.
 */
public final class AdminGovernance extends VBox {

    private record StatDef(String key, String value, String desc, String icon, boolean live) {}
    private record EntityRow(int id, String name, String email, String role,
                             String cohort, String status, String lastActive) {}

    private static final List<StatDef> STATS = List.of(
        new StatDef("Learners", "12,840", "+128 this term", "graduation-cap", false),
        new StatDef("Educators", "642", "+9 this term", "users", false),
        new StatDef("Active sessions", "1,204", "live now", "activity", true),
        new StatDef("Pending invites", "37", "awaiting", "mail", false)
    );

    private static final List<EntityRow> ROWS = List.of(
        new EntityRow(1, "Dr. Elaine Voss", "e.voss@northbridge.edu", "Educator", "Mathematics", "active", "2m ago"),
        new EntityRow(2, "Mara Quinn", "m.quinn@northbridge.edu", "Learner", "Grade 11 · A", "active", "5m ago"),
        new EntityRow(3, "Prof. Idris Khan", "i.khan@northbridge.edu", "Educator", "Sciences", "active", "1h ago"),
        new EntityRow(4, "Leo Park", "l.park@northbridge.edu", "Learner", "Grade 11 · A", "suspended", "3d ago"),
        new EntityRow(5, "Ada Okafor", "a.okafor@northbridge.edu", "Learner", "Grade 11 · B", "active", "12m ago"),
        new EntityRow(6, "Sun Yi", "s.yi@northbridge.edu", "Learner", "Grade 10 · C", "invited", "—"),
        new EntityRow(7, "R. Castellanos", "r.cast@northbridge.edu", "Admin", "Institution", "active", "now")
    );

    private String currentTab = "all";
    private final Map<Integer, Boolean> selected = new HashMap<>();
    private final VBox panelContainer;

    public AdminGovernance() {
        setSpacing(22);
        setPadding(new Insets(0));
        selected.put(2, true);
        selected.put(4, true);

        // === Stats ===
        getChildren().add(createStats());

        // === Panel ===
        panelContainer = new VBox();
        refreshPanel();
        getChildren().add(panelContainer);
    }

    private void refreshPanel() {
        panelContainer.getChildren().clear();

        VBox panel = new VBox();
        panel.getStyleClass().add("gov-panel");

        // Toolbar
        panel.getChildren().add(createToolbar());

        // Bulk bar (only if items selected)
        long selCount = selected.values().stream().filter(Boolean::booleanValue).count();
        if (selCount > 0) {
            panel.getChildren().add(createBulkBar(selCount));
        }

        // Table
        panel.getChildren().add(createTable());

        // Footer
        panel.getChildren().add(createFooter());

        panelContainer.getChildren().add(panel);
    }

    private Node createStats() {
        HBox stats = new HBox(16);
        for (StatDef s : STATS) {
            HBox stat = new HBox(13);
            stat.getStyleClass().add("gov-stat");
            stat.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(stat, Priority.ALWAYS);

            StackPane iconBox = new StackPane();
            iconBox.getStyleClass().add("gov-stat-icon");
            iconBox.getChildren().add(AcIcon.create(s.icon, 19, Color.web("#2c40a0")));

            VBox body = new VBox(1);
            Label v = new Label(s.value);
            v.getStyleClass().add("gov-stat-v");
            Label k = new Label(s.key);
            k.getStyleClass().add("gov-stat-k");

            HBox descRow = new HBox(5);
            descRow.setAlignment(Pos.CENTER_LEFT);
            if (s.live) {
                Region liveDot = new Region();
                liveDot.getStyleClass().add("gov-livedot");
                liveDot.setMinSize(7, 7); liveDot.setPrefSize(7, 7); liveDot.setMaxSize(7, 7);
                descRow.getChildren().add(liveDot);
            }
            Label d = new Label(s.desc);
            d.getStyleClass().addAll("gov-stat-d");
            if (s.live) d.getStyleClass().add("gov-stat-d-live");
            descRow.getChildren().add(d);

            body.getChildren().addAll(v, k, descRow);
            stat.getChildren().addAll(iconBox, body);
            stats.getChildren().add(stat);
        }
        return stats;
    }

    private Node createToolbar() {
        HBox toolbar = new HBox(16);
        toolbar.getStyleClass().add("gov-toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);

        AcTabs tabs = new AcTabs(List.of(
            new AcTabs.TabDef("all", "All", 7),
            new AcTabs.TabDef("educators", "Educators", 3),
            new AcTabs.TabDef("learners", "Learners", 4)
        ), currentTab, id -> {
            currentTab = id;
            // Clear selection when switching tabs
            selected.clear();
            refreshPanel();
        }, AcTabs.Variant.PILL);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox right = new HBox(10);
        right.setAlignment(Pos.CENTER_RIGHT);

        AcInput filterInput = new AcInput("Filter entities…",
            AcIcon.create("search", 14, Color.web("#8c909e")), "sm");
        filterInput.setMaxWidth(200);

        AcSelect sortSelect = new AcSelect(
            new String[]{"Sort: Last active", "Sort: Name", "Sort: Role"},
            "Sort: Last active", "sm"
        );

        AcButton exportBtn = new AcButton("Export", AcButton.Variant.SECONDARY, AcButton.Size.SM,
            AcIcon.create("download", 14, Color.web("#1b1d25")), null);

        AcButton addBtn = new AcButton("Add entity", AcButton.Variant.PRIMARY, AcButton.Size.SM,
            AcIcon.create("user-plus", 14, Color.WHITE), null);

        right.getChildren().addAll(filterInput, sortSelect, exportBtn, addBtn);
        toolbar.getChildren().addAll(tabs, spacer, right);
        return toolbar;
    }

    private Node createBulkBar(long selCount) {
        HBox bar = new HBox(10);
        bar.getStyleClass().add("gov-bulkbar");
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(10, 18, 10, 18));

        Label text = new Label(selCount + " selected");
        text.getStyleClass().add("gov-bulkbar-text");
        HBox.setHgrow(text, Priority.ALWAYS);

        HBox actions = new HBox(4);
        AcButton msgBtn = new AcButton("Message", AcButton.Variant.GHOST, AcButton.Size.SM,
            AcIcon.create("mail", 14, Color.web("#3b3f4c")), null);
        AcButton suspendBtn = new AcButton("Suspend", AcButton.Variant.GHOST, AcButton.Size.SM,
            AcIcon.create("shield-off", 14, Color.web("#3b3f4c")), null);
        AcButton removeBtn = new AcButton("Remove", AcButton.Variant.GHOST, AcButton.Size.SM,
            AcIcon.create("trash-2", 14, Color.web("#3b3f4c")), null);

        // Clear selection on action
        removeBtn.setOnAction(e -> {
            selected.clear();
            refreshPanel();
        });

        actions.getChildren().addAll(msgBtn, suspendBtn, removeBtn);
        bar.getChildren().addAll(text, actions);
        return bar;
    }

    private Node createTable() {
        Predicate<EntityRow> filter = switch (currentTab) {
            case "educators" -> r -> "Educator".equals(r.role) || "Admin".equals(r.role);
            case "learners" -> r -> "Learner".equals(r.role);
            default -> r -> true;
        };

        VBox table = new VBox();

        // Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #f2f1ea; -fx-padding: 11 16;");
        header.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().addAll(
            tableHeader("", 44),
            tableHeader("ENTITY", 220),
            tableHeader("ROLE", 100),
            tableHeader("COHORT", 130),
            tableHeader("STATUS", 100),
            tableHeader("LAST ACTIVE", 90),
            tableHeader("", 40)
        );
        table.getChildren().add(header);

        // Rows
        ROWS.stream().filter(filter).forEach(r -> {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");

            boolean isSel = Boolean.TRUE.equals(selected.get(r.id));
            if (isSel) row.setStyle(row.getStyle() + " -fx-background-color: #eef1fb;");

            // Checkbox
            AcCheckbox cb = new AcCheckbox();
            cb.setSelected(isSel);
            cb.getCheckBox().selectedProperty().addListener((obs, o, n) -> {
                selected.put(r.id, n);
                refreshPanel();
            });
            HBox cbWrap = new HBox(cb);
            cbWrap.setMinWidth(44);
            cbWrap.setPrefWidth(44);
            cbWrap.setAlignment(Pos.CENTER);

            // Entity
            HBox entity = new HBox(11);
            entity.setMinWidth(220);
            entity.setPrefWidth(220);
            entity.setAlignment(Pos.CENTER_LEFT);
            AcAvatar avatar = new AcAvatar(r.name, AcAvatar.Size.SM);
            VBox entityText = new VBox();
            Label entityName = new Label(r.name);
            entityName.getStyleClass().add("gov-entity-name");
            Label entityEmail = new Label(r.email);
            entityEmail.getStyleClass().add("gov-entity-email");
            entityText.getChildren().addAll(entityName, entityEmail);
            entity.getChildren().addAll(avatar, entityText);

            // Role badge
            HBox roleWrap = new HBox();
            roleWrap.setMinWidth(100);
            roleWrap.setPrefWidth(100);
            roleWrap.setAlignment(Pos.CENTER_LEFT);
            AcBadge roleBadge = switch (r.role) {
                case "Admin" -> new AcBadge("Admin", AcBadge.Variant.BRAND, AcBadge.Appearance.SOLID);
                case "Educator" -> new AcBadge("Educator", AcBadge.Variant.BRAND);
                default -> new AcBadge("Learner", AcBadge.Variant.NEUTRAL);
            };
            roleWrap.getChildren().add(roleBadge);

            // Cohort
            Label cohort = new Label(r.cohort);
            cohort.getStyleClass().add("gov-cohort");
            cohort.setMinWidth(130);
            cohort.setPrefWidth(130);

            // Status
            HBox statusWrap = new HBox();
            statusWrap.setMinWidth(100);
            statusWrap.setPrefWidth(100);
            statusWrap.setAlignment(Pos.CENTER_LEFT);
            AcBadge statusBadge = switch (r.status) {
                case "active" -> new AcBadge("Active", AcBadge.Variant.SUCCESS, true);
                case "suspended" -> new AcBadge("Suspended", AcBadge.Variant.DANGER, true);
                default -> new AcBadge("Invited", AcBadge.Variant.WARNING, true);
            };
            statusWrap.getChildren().add(statusBadge);

            // Last active
            Label last = new Label(r.lastActive);
            last.getStyleClass().add("gov-last");
            last.setMinWidth(90);
            last.setPrefWidth(90);

            // Actions
            AcIconButton moreBtn = new AcIconButton(
                AcIcon.create("more-horizontal", 16, Color.web("#565a68")),
                "Manage", AcIconButton.Variant.GHOST, AcIconButton.Size.SM
            );
            HBox moreWrap = new HBox(moreBtn);
            moreWrap.setMinWidth(40);
            moreWrap.setPrefWidth(40);
            moreWrap.setAlignment(Pos.CENTER);

            row.getChildren().addAll(cbWrap, entity, roleWrap, cohort, statusWrap, last, moreWrap);
            table.getChildren().add(row);
        });

        return table;
    }

    private Label tableHeader(String text, double width) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 10.5px; -fx-font-weight: 500; -fx-text-fill: #8c909e;");
        l.setMinWidth(width);
        l.setPrefWidth(width);
        return l;
    }

    private Node createFooter() {
        HBox foot = new HBox(10);
        foot.getStyleClass().add("gov-foot");
        foot.setAlignment(Pos.CENTER_LEFT);
        foot.setPadding(new Insets(14, 18, 14, 18));

        Label showingLabel = new Label("Showing 7 of 13,482 entities");
        showingLabel.getStyleClass().add("gov-foot-text");
        HBox.setHgrow(showingLabel, Priority.ALWAYS);

        HBox pager = new HBox(10);
        pager.getStyleClass().add("gov-pager");
        pager.setAlignment(Pos.CENTER);

        AcIconButton prevBtn = new AcIconButton(
            AcIcon.create("chevron-left", 15, Color.web("#565a68")),
            "Previous", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM
        );
        Label pageLabel = new Label("1 / 964");
        pageLabel.getStyleClass().add("gov-pager-text");
        AcIconButton nextBtn = new AcIconButton(
            AcIcon.create("chevron-right", 15, Color.web("#565a68")),
            "Next", AcIconButton.Variant.OUTLINE, AcIconButton.Size.SM
        );
        pager.getChildren().addAll(prevBtn, pageLabel, nextBtn);

        foot.getChildren().addAll(showingLabel, pager);
        return foot;
    }
}
