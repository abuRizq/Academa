package com.academa.screens;

import com.academa.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * RosterScreen — learner roster table with tabs and stats.
 * Mirrors React Roster.jsx.
 */
public final class RosterScreen extends VBox {

    private record Learner(String name, String email, double avg, int comp, String last, String flag) {}

    private static final List<Learner> ROWS = List.of(
        new Learner("Ada Okafor", "a.okafor@northbridge.edu", 96.2, 100, "12m ago", "honors"),
        new Learner("Leo Park", "l.park@northbridge.edu", 95.1, 100, "5m ago", "honors"),
        new Learner("Mara Quinn", "m.quinn@northbridge.edu", 94.6, 73, "now", null),
        new Learner("Sun Yi", "s.yi@northbridge.edu", 92.0, 91, "1h ago", null),
        new Learner("Iris Bauer", "i.bauer@northbridge.edu", 90.8, 100, "3h ago", null),
        new Learner("Tomas Reyes", "t.reyes@northbridge.edu", 71.3, 64, "2d ago", "risk"),
        new Learner("Nadia Haaf", "n.haaf@northbridge.edu", 88.1, 91, "30m ago", null),
        new Learner("Owen Cole", "o.cole@northbridge.edu", 63.7, 55, "4d ago", "risk")
    );

    private String activeTab = "all";

    public RosterScreen() {
        setSpacing(16);
        buildUI();
    }

    private void buildUI() {
        getChildren().clear();

        // Stats
        getChildren().add(PageKit.statRow(
            PageKit.stat("users", "brand", "32", "Enrolled", "Grade 11 · A"),
            PageKit.stat("target", "gold", "81.6%", "Class mean", "+4.0 vs cohort", "up", false),
            PageKit.stat("award", "success", "5", "Honor roll", "≥ 90% avg"),
            PageKit.stat("triangle-alert", "danger", "2", "At risk", "< 72% avg", "down", false)
        ));

        // Toolbar
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 8, 0));

        // Tabs
        HBox tabs = new HBox(4);
        tabs.setStyle("-fx-background-color: #f2f1ea; -fx-background-radius: 9; -fx-padding: 3;");
        for (String[] tab : new String[][]{{"all","All","32"},{"honors","Honor roll","5"},{"risk","At risk","2"}}) {
            HBox pill = new HBox(6);
            pill.setAlignment(Pos.CENTER);
            pill.setPadding(new Insets(6, 12, 6, 12));
            pill.setCursor(javafx.scene.Cursor.HAND);
            boolean active = tab[0].equals(activeTab);
            pill.setStyle("-fx-background-radius: 7;" + (active ? " -fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 2, 0, 0, 1);" : ""));
            Label l = new Label(tab[1]);
            l.setStyle("-fx-font-size: 12px; -fx-font-weight: " + (active ? "bold" : "500") +
                "; -fx-text-fill: " + (active ? "#2c40a0" : "#565a68") + ";");
            Label c = new Label(tab[2]);
            c.setStyle("-fx-font-size: 10px; -fx-text-fill: " + (active ? "#2c40a0" : "#8c909e") +
                "; -fx-background-color: " + (active ? "#eaedfa" : "#e8e7df") + "; -fx-background-radius: 999; -fx-padding: 1 5 1 5;");
            pill.getChildren().addAll(l, c);
            pill.setOnMouseClicked(e -> { activeTab = tab[0]; buildUI(); });
            tabs.getChildren().add(pill);
        }

        toolbar.getChildren().addAll(tabs, PageKit.spacer(),
            new AcInput(null, "text", null, "Find learner…",
                AcIcon.create("search", 14, Color.web("#8c909e")), null, "sm", false),
            new AcSelect(new String[]{"Sort: Avg score", "Sort: Name", "Sort: Last active", "Sort: Completion"}, "Sort: Avg score", "sm"),
            new AcButton("Message all", AcButton.Variant.SECONDARY, AcButton.Size.SM,
                AcIcon.create("mail", 14, Color.web("#1b1d25")), null)
        );
        getChildren().add(toolbar);

        // Table
        AcCard tableCard = new AcCard(null, null, "sm", false);
        VBox tbody = tableCard.getBody();

        // Header
        HBox thead = new HBox();
        thead.setStyle("-fx-background-color: #f2f1ea; -fx-padding: 10 16 10 16; -fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");
        thead.setAlignment(Pos.CENTER_LEFT);
        for (String[] h : new String[][]{{"Learner","0"},{"Avg score","70"},{"Term completion","140"},{"Standing","90"},{"Last active","80"},{"","40"}}) {
            Label hl = new Label(h[0]);
            hl.setStyle("-fx-font-size: 10.5px; -fx-font-weight: bold; -fx-text-fill: #8c909e;");
            if ("0".equals(h[1])) HBox.setHgrow(hl, Priority.ALWAYS);
            else hl.setMinWidth(Integer.parseInt(h[1]));
            thead.getChildren().add(hl);
        }
        tbody.getChildren().add(thead);

        // Filter
        List<Learner> visible = ROWS.stream()
            .filter(r -> "all".equals(activeTab)
                || ("honors".equals(activeTab) && "honors".equals(r.flag))
                || ("risk".equals(activeTab) && "risk".equals(r.flag)))
            .toList();

        for (Learner r : visible) {
            HBox row = new HBox(10);
            row.setPadding(new Insets(10, 16, 10, 16));
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-border-color: transparent transparent #e3e2db transparent; -fx-border-width: 0 0 1 0;");

            // Learner entity
            HBox ent = new HBox(10);
            ent.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(ent, Priority.ALWAYS);
            AcAvatar avatar = new AcAvatar(r.name, AcAvatar.Size.SM, "honors".equals(r.flag), "now".equals(r.last) ? "online" : null);
            VBox entTxt = new VBox();
            Label n = new Label(r.name);
            n.setStyle("-fx-font-size: 14px; -fx-font-weight: 500; -fx-text-fill: #1b1d25;");
            Label e = new Label(r.email);
            e.setStyle("-fx-font-size: 11.5px; -fx-text-fill: #8c909e;");
            entTxt.getChildren().addAll(n, e);
            ent.getChildren().addAll(avatar, entTxt);

            // Avg
            Label avgL = new Label(r.avg + "%");
            avgL.setMinWidth(70);
            String avgColor = r.avg >= 85 ? "#2e6b4f" : r.avg >= 72 ? "#985f1f" : "#9b2c2c";
            avgL.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + avgColor + ";");

            // Completion
            HBox compBox = new HBox(6);
            compBox.setMinWidth(140);
            compBox.setAlignment(Pos.CENTER_LEFT);
            AcProgressBar pb = new AcProgressBar(null, r.comp, 100,
                r.comp >= 90 ? AcProgressBar.Variant.SUCCESS : r.comp >= 70 ? AcProgressBar.Variant.BRAND : AcProgressBar.Variant.GOLD,
                AcProgressBar.Size.SM, false, null);
            pb.setMaxWidth(80);
            Label compN = new Label(r.comp + "%");
            compN.setStyle("-fx-font-size: 12px; -fx-text-fill: #6f7484;");
            compBox.getChildren().addAll(pb, compN);

            // Standing
            Node badge;
            if ("honors".equals(r.flag)) badge = new AcBadge("Honor roll", AcBadge.Variant.GOLD, true);
            else if ("risk".equals(r.flag)) badge = new AcBadge("At risk", AcBadge.Variant.DANGER, true);
            else badge = new AcBadge("On track", AcBadge.Variant.NEUTRAL);
            HBox standBox = new HBox(badge);
            standBox.setMinWidth(90);

            // Last active
            Label lastL = new Label(r.last);
            lastL.setMinWidth(80);
            lastL.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c909e;");

            // Action
            AcIconButton viewBtn = new AcIconButton(AcIcon.create("arrow-right", 15, Color.web("#565a68")),
                "View", AcIconButton.Variant.GHOST, AcIconButton.Size.SM);
            HBox actionBox = new HBox(viewBtn);
            actionBox.setMinWidth(40);

            row.getChildren().addAll(ent, avgL, compBox, standBox, lastL, actionBox);
            tbody.getChildren().add(row);
        }
        getChildren().add(tableCard);
    }
}
